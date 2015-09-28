package br.concatto.fonicsynth;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Key extends VBox {
	private SimpleBooleanProperty pressed = new SimpleBooleanProperty(false);
	private Background regularBackground;
	private Background pressedBackground;
	private Label keyLabel;
	private Label noteLabel;
	private Label numberLabel;
	
	public Key(String key, String note, Color regularColor, Color pressedColor) {
		super(3);
		
		keyLabel = new Label(key);
		noteLabel = new Label(note);
		numberLabel = new Label();
		getChildren().addAll(keyLabel, noteLabel, numberLabel);
		regularBackground = new Background(new BackgroundFill(regularColor, null, null));
		pressedBackground = new Background(new BackgroundFill(pressedColor, null, null));
		
		getChildren().forEach(node -> ((Label) node).setTextFill(Color.valueOf("#9F9F9F")));
		
		//Tirar foto disso
		backgroundProperty().bind(Bindings.when(pressed).then(pressedBackground).otherwise(regularBackground));
		setAlignment(Pos.BOTTOM_CENTER);
		setPadding(new Insets(0, 0, 3, 0));
	}
	
	public void setFontSize(double size) {
		getChildren().forEach(node -> ((Label) node).setFont(Font.font(size)));
	}
	
	public boolean pressed() {
		return pressed.get();
	}
	
	public void press() {
		pressed.set(true);
	}
	
	public void release() {
		pressed.set(false);
	}
	
	public StringProperty numberProperty() {
		return numberLabel.textProperty();
	}
	
	public String getKeyDigit() {
		return keyLabel.getText();
	}

	public int getNumber() {
		return Integer.parseInt(numberLabel.getText());
	}
}
