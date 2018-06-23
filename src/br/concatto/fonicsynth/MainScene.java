package br.concatto.fonicsynth;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainScene extends Scene {
	private static final Color DISABLED = Color.SILVER;
	private static final Color ENABLED = Color.BLACK;
	
	private Keyboard keyboard;
	private ScalePlayer scalePlayer;
	private HBox naturals = new HBox(1);
	private HBox sharps = new HBox();
	private HBox octaves = new HBox();
	private StackPane keyContainer = new StackPane(naturals, sharps);
	private StackPane octaveContainer = new StackPane();
	private MainMenuBar menuBar = new MainMenuBar();
	private VBox keyboardWrapper = new VBox(octaveContainer, keyContainer);
	private VBox root;
	private Label increaseTransposition;
	private Label decreaseTransposition;
	private InstrumentPane instrumentPane;
	
	public MainScene(Keyboard keyboard) {
		super(new VBox(15));
		this.keyboard = keyboard;
		scalePlayer = new ScalePlayer(keyboard);
		
		instrumentPane = new InstrumentPane(keyboard);
		
		menuBar.onScaleRequested(scale -> {
			scalePlayer.play(scale);
		});
		
		root = (VBox) getRoot();
		root.getChildren().addAll(menuBar, keyboardWrapper, instrumentPane);
		root.setPadding(new Insets(0, 0, 15, 0));
		root.setAlignment(Pos.TOP_CENTER);
		
		initializeOctaves();
		initializeKeys();
		
		decreaseTransposition.textFillProperty().bind(Bindings
				.when(keyboard.transpositionProperty().isEqualTo(KeyboardLimits.MIN_TRANSPOSITION))
				.then(DISABLED).otherwise(ENABLED));
		
		increaseTransposition.textFillProperty().bind(Bindings
				.when(keyboard.transpositionProperty().isEqualTo(KeyboardLimits.MAX_TRANSPOSITION))
				.then(DISABLED).otherwise(ENABLED));	
	}

	private void initializeOctaves() {
		
		for (int i = 0; i < 5; i++) {
			Label octave = new Label();
			octave.setAlignment(Pos.CENTER);
			octave.textProperty().bind(keyboard.transpositionProperty().add(i).asString("Oitava %d"));
			octave.prefWidthProperty().bind(keyContainer.maxWidthProperty());
			
			octaves.getChildren().add(octave);
			if (i < 4) {
				Separator separator = new Separator(Orientation.VERTICAL);
				separator.setPadding(new Insets(0, -3, 0, -3));
				octaves.getChildren().add(separator);
			}
		}
		
		decreaseTransposition = new Label("<-");
		increaseTransposition = new Label("->");
		
		octaveContainer.setAlignment(Pos.BOTTOM_CENTER);
		octaveContainer.getChildren().addAll(decreaseTransposition, increaseTransposition, octaves);
		octaveContainer.maxWidthProperty().bind(keyContainer.maxWidthProperty());
		
		StackPane.setAlignment(decreaseTransposition, Pos.CENTER_LEFT);
		StackPane.setAlignment(increaseTransposition, Pos.CENTER_RIGHT);
	}
	
	private void initializeKeys() {
		for (int i = 0; i < 60; i++) {
			HBox box = (HBox) (Keyboard.isSharp(i) ? sharps : naturals);
			box.getChildren().add(keyboard.get(i));
			
			//Espaço extra entre sustenidas
			if (i < 59 && (i % 12 == 4 || i % 12 == 11)) {
				sharps.getChildren().add(new SharpKey(false));
			}
		}
		
		naturals.setBackground(new Background(new BackgroundFill(Color.DIMGRAY, null, null)));
		naturals.setBorder(new Border(new BorderStroke(Color.DIMGRAY, BorderStrokeStyle.SOLID, null, null)));
		
		keyContainer.setAlignment(Pos.TOP_LEFT);
		keyContainer.setPadding(new Insets(0, 1, 0, 0)); //Borda
		keyboardWrapper.setAlignment(Pos.TOP_CENTER);
		
		VBox.setVgrow(keyboardWrapper, Priority.ALWAYS);
		VBox.setVgrow(keyContainer, Priority.ALWAYS);
		
		keyboardWrapper.widthProperty().addListener((obs, o, n) -> resizeKeys(n.intValue()));
		sharps.maxHeightProperty().bind(keyContainer.heightProperty().divide(1.9));
	}
	
	public void startListening() {
		getWindow().focusedProperty().addListener((obs, o, n) -> {
			if (n) keyboardWrapper.requestFocus();
		});
		
		EventHandler<KeyEvent> listener = new KeyboardListener(keyboard, instrumentPane);
		keyboardWrapper.setOnKeyPressed(listener);
		keyboardWrapper.setOnKeyReleased(listener);
	}
	
	private void resizeKeys(int width) {
		double keyWidth = Math.floor(width / keyboard.getNaturalKeyCount());
		
		for (Node node : naturals.getChildren()) {
			Key natural = (Key) node;
			natural.setPrefWidth(keyWidth);
			natural.setFontSize(keyWidth / 2);
		}
		
		double sharpWidth = keyWidth / 1.4;
		for (Node node : sharps.getChildren()) {
			Key sharp = (Key) node;
			sharp.setPrefWidth(sharpWidth);
			sharp.setFontSize(sharpWidth / 2);
		}
		
		sharps.setSpacing(keyWidth - Math.ceil(sharpWidth));
		sharps.setPadding(new Insets(0, 0, 0, keyWidth - (sharpWidth / 2f)));
		
		keyContainer.setMaxWidth(keyWidth * keyboard.getNaturalKeyCount());
	}
}
