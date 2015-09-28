package br.concatto.fonicsynth;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
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
import javafx.scene.text.Font;

public class MainScene extends Scene {	
	private Keyboard keyboard;
	private HBox naturals = new HBox(1);
	private HBox sharps = new HBox();
	private HBox octaves = new HBox();
	private StackPane keyContainer = new StackPane(naturals, sharps);
	private StackPane octaveContainer = new StackPane();
	private VBox keyboardWrapper = new VBox(octaveContainer, keyContainer);
	private Label instrumentName = new Label();
	private HBox instrumentContainer = new HBox(instrumentName);
	private VBox root;
	private Label increaseTransposition;
	private Label decreaseTransposition;
	private EchoesPane echoes;
	
	public MainScene(Keyboard keyboard) {
		super(new VBox(10));
		this.keyboard = keyboard;
		echoes = new EchoesPane(keyboard.getEchoes());
		
		root = (VBox) getRoot();
		root.getChildren().addAll(keyboardWrapper, instrumentContainer, echoes);
		root.setPadding(new Insets(10));
		root.setAlignment(Pos.TOP_CENTER);
		
		initializeOctaves();
		initializeKeys();
		initializeInstrumentControls();
		
		for (int i = 0; i < 42; i++) {
			changeInstrument(true);
		}
	}
	
	private void initializeInstrumentControls() {
		instrumentContainer.setAlignment(Pos.CENTER);
		instrumentName.setFont(Font.font(18));
	}

	private void initializeOctaves() {
		for (int i = 0; i < 5; i++) {
			Label octave = new Label();
			octave.setAlignment(Pos.CENTER);
			octave.textProperty().bind(keyboard.transpositionProperty().add(i + 1).asString("%dª oitava"));
			octave.prefWidthProperty().bind(keyContainer.maxWidthProperty().divide(5));
			
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
		
		EventHandler<KeyEvent> listener = e -> {
			KeyCode code = e.getCode();
			boolean pressed = e.getEventType() == KeyEvent.KEY_PRESSED;
			
			if (code.isLetterKey() || code.isDigitKey()) {
				String name = code.getName();
				if (pressed) {
					keyboard.press(name, e.isShiftDown());
				} else {
					keyboard.release(name);
				}
			}
			
			if (code.isArrowKey() && pressed) {
				if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
					if (!keyboard.isAnyPressed()) {
						if (e.isControlDown()) {
							echoes.changeDifference(code == KeyCode.RIGHT);
						} else {
							transpose(code == KeyCode.RIGHT);
						}
					}
				} else if (code == KeyCode.UP || code == KeyCode.DOWN) {
					if (e.isControlDown()) {
						echoes.changeInstrument(code == KeyCode.UP);
					} else {
						changeInstrument(code == KeyCode.UP);
					}
				}
			}
			
			if (code.isFunctionKey() && pressed) {
				int index = Integer.parseInt(code.getName().substring(1)) - 1;
				if (index < echoes.getQuantity()) {
					echoes.invertSelection(index);
				}
			}
			
			if (code == KeyCode.TAB && pressed) {
				echoes.invertEnabled();
			}
		};
		
		keyboardWrapper.setOnKeyPressed(listener);
		keyboardWrapper.setOnKeyReleased(listener);
	}

	private void changeInstrument(boolean increment) {
		if (KeyboardLimits.isInstrumentChangeWithinLimits(keyboard.getInstrument(), increment)) {
			keyboard.setInstrument(keyboard.getInstrument() + (increment ? 1 : -1));
			instrumentName.setText(Instruments.get(keyboard.getInstrument()));
		}
	}
	
	private void transpose(boolean increment) {
		if (KeyboardLimits.isTranspositionChangeWithinLimits(keyboard.getTransposition(), increment)) {
			keyboard.setTransposition(keyboard.getTransposition() + (increment ? 1 : -1));
		}
		
		Color disabled = Color.SILVER;
		Color enabled = Color.BLACK;
		decreaseTransposition.setTextFill(keyboard.getTransposition() == KeyboardLimits.MIN_TRANSPOSITION ? disabled : enabled);
		increaseTransposition.setTextFill(keyboard.getTransposition() == KeyboardLimits.MAX_TRANSPOSITION ? disabled : enabled);
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
