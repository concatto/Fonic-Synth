package br.concatto.fonicsynth;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
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
	private ConnectionManager connectionManager;
	private HBox octaves = new HBox();
	private StackPane octaveContainer = new StackPane();
	private MainMenuBar menuBar = new MainMenuBar();
	private KeyboardPane keyboardPane = new KeyboardPane();
	private KeyboardPane onlinePane = new KeyboardPane();
	private VBox keyboardWrapper = new VBox(octaveContainer, keyboardPane);
	private VBox mainContainer = new VBox(15);
	private VBox root;
	private Label increaseTransposition;
	private Label decreaseTransposition;
	private InstrumentPane instrumentPane;
	
	public MainScene(ConnectionManager manager) {
		super(new VBox(15));
		this.connectionManager = manager;
		
		keyboard = keyboardPane.getKeyboard();
		
		scalePlayer = new ScalePlayer(keyboard);
		instrumentPane = new InstrumentPane(keyboardPane.getKeyboard());
		
		menuBar.onScaleRequested(scale -> {
			scalePlayer.play(scale);
		});
		
		root = (VBox) getRoot();
		root.setPadding(new Insets(0, 0, 15, 0));
		root.setAlignment(Pos.TOP_CENTER);
		root.getChildren().addAll(menuBar, mainContainer);
		mainContainer.getChildren().addAll(keyboardWrapper, instrumentPane);
		mainContainer.setPadding(new Insets(15));
		mainContainer.setAlignment(Pos.TOP_CENTER);
		
		configureMenu();
		initializeOctaves();
		
		decreaseTransposition.textFillProperty().bind(Bindings
				.when(keyboard.transpositionProperty().isEqualTo(KeyboardLimits.MIN_TRANSPOSITION))
				.then(DISABLED).otherwise(ENABLED));
		
		increaseTransposition.textFillProperty().bind(Bindings
				.when(keyboard.transpositionProperty().isEqualTo(KeyboardLimits.MAX_TRANSPOSITION))
				.then(DISABLED).otherwise(ENABLED));	
		
		keyboardWrapper.setAlignment(Pos.TOP_CENTER);
		
		VBox.setVgrow(mainContainer, Priority.ALWAYS);
		VBox.setVgrow(keyboardWrapper, Priority.ALWAYS);
		VBox.setVgrow(keyboardPane, Priority.ALWAYS);
		
		keyboardWrapper.widthProperty().addListener((obs, o, n) -> {
			keyboardPane.resizeKeys(n.intValue());
			onlinePane.resizeKeys(n.intValue());
		});
		
		onlinePane.setKeyTextShown(false);
		onlinePane.setPrefHeight(100);
	}

	private void configureMenu() {
		Menu onlineMenu = new Menu("Online");
		MenuItem connectItem = new MenuItem("Conectar");
		MenuItem hostItem = new MenuItem("Servir");
		connectItem.setOnAction(e -> showConnectionDialog());
		hostItem.setOnAction(e -> showHostDialog());
		
		onlineMenu.getItems().addAll(connectItem, hostItem);
		
		menuBar.getMenus().add(onlineMenu);
	}

	private void showConnectionDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setContentText("Computador alvo (<ip>:<porta>)");
		dialog.showAndWait().ifPresent(str -> {
			connectionManager.connect(str);
			mainContainer.getChildren().add(onlinePane);
		});
	}
	
	private void showHostDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setContentText("Porta");
		dialog.showAndWait().ifPresent(str -> {
			connectionManager.host(str);
			mainContainer.getChildren().add(onlinePane);
		});
	}

	private void initializeOctaves() {
		
		for (int i = 0; i < 5; i++) {
			Label octave = new Label();
			octave.setAlignment(Pos.CENTER);
			octave.textProperty().bind(keyboard.transpositionProperty().add(i).asString("Oitava %d"));
			octave.prefWidthProperty().bind(keyboardPane.maxWidthProperty());
			
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
		octaveContainer.maxWidthProperty().bind(keyboardPane.maxWidthProperty());
		
		StackPane.setAlignment(decreaseTransposition, Pos.CENTER_LEFT);
		StackPane.setAlignment(increaseTransposition, Pos.CENTER_RIGHT);
	}
	
	public void startListening() {
		getWindow().focusedProperty().addListener((obs, o, n) -> {
			if (n) keyboardWrapper.requestFocus();
		});
		
		EventHandler<KeyEvent> listener = new KeyboardListener(keyboardPane.getKeyboard(), instrumentPane);
		keyboardWrapper.setOnKeyPressed(listener);
		keyboardWrapper.setOnKeyReleased(listener);
	}
	
	public KeyboardPane getOnlinePane() {
		return onlinePane;
	}
	
	public Keyboard getKeyboard() {
		return keyboardPane.getKeyboard();
	}
}
