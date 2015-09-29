package br.concatto.fonicsynth;

import javafx.application.Application;
import javafx.stage.Stage;

public class Controller extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Keyboard keyboard = new Keyboard(3, 19, 3);
		MainScene mainScene = new MainScene(keyboard);		

		//Gambiarrinha
		primaryStage.setOnShown(e -> {
			primaryStage.setWidth(primaryStage.getMinWidth() + 1);
			primaryStage.setHeight(primaryStage.getMinHeight() + 1);
		});
		
		primaryStage.setMinWidth(keyboard.getNaturalKeyCount() * 30 + 1);
		primaryStage.setMinHeight(380);
		primaryStage.setTitle("Fonic Synth");
		primaryStage.setScene(mainScene);
		
		mainScene.startListening();
		primaryStage.show();
		
		
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
