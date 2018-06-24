package br.concatto.fonicsynth;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Controller extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ConnectionManager manager = new ConnectionManager();
		MainScene mainScene = new MainScene(manager);
		Keyboard onlineKeyboard = mainScene.getOnlinePane().getKeyboard();
		
		mainScene.getKeyboard().setEventConsumer(event -> {
			if (manager.isConnected()) manager.writeEvent(event);
		});
		
		manager.setOnReceive(event -> {			
			int data = event.getEventData() & 0xFF;
			System.out.println(data);
			switch (event.getEventType()) {
			case KeyboardEvent.PRESS:
				onlineKeyboard.press(data);
				break;
			case KeyboardEvent.RELEASE:
				onlineKeyboard.release(data);
				break;
			case KeyboardEvent.INSTRUMENT:
				onlineKeyboard.setInstrument(data);
				break;
			case KeyboardEvent.TRANSPOSITION:
				Platform.runLater(() -> onlineKeyboard.setTransposition(data));
				break;
			case KeyboardEvent.COMBINATION:
				Echo[] combination = EchoCombination.values()[data].getEchoes();
				Echo[] echoes = onlineKeyboard.getEchoes();
				for (int i = 0; i < echoes.length; i++) {
					if (i < combination.length) {
						echoes[i].setTo(combination[i]);
						echoes[i].setEnabled(true);
					} else {
						echoes[i].setEnabled(false);
					}
				}
			}
		});

		//Gambiarrinha
		primaryStage.setOnShown(e -> {
			primaryStage.setWidth(primaryStage.getMinWidth() + 1);
			primaryStage.setHeight(primaryStage.getMinHeight() + 1);
		});
		
		primaryStage.setMinWidth((Keyboard.NATURAL_TONES * 5) * 32 + 1);
		primaryStage.setMinHeight(650);
		primaryStage.setTitle("Fonic Synth");
		primaryStage.setScene(mainScene);
		
		mainScene.startListening();
		primaryStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
