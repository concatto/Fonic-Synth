package br.concatto.fonicsynth;

import javafx.application.Application;
import javafx.stage.Stage;

public class Controller extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Keyboard keyboard = new Keyboard(3, 3);
		MainScene mainScene = new MainScene(keyboard);		

		primaryStage.setTitle("Fonic Synth");
		primaryStage.setMinWidth(keyboard.getNaturalKeyCount() * 30 + 1);
		primaryStage.setMinHeight(380);
		primaryStage.setScene(mainScene);
		mainScene.startListening();
		primaryStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
