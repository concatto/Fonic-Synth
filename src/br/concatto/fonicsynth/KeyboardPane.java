package br.concatto.fonicsynth;

import javax.sound.midi.MidiUnavailableException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class KeyboardPane extends StackPane {
	private Keyboard keyboard;
	private HBox naturals = new HBox(1);
	private HBox sharps = new HBox();
	
	public KeyboardPane() {
		getChildren().addAll(naturals, sharps);
		
		try {
			keyboard = new Keyboard(3, Instruments.CHURCH_ORGAN, 3);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
		setAlignment(Pos.TOP_LEFT);
		setPadding(new Insets(0, 1, 0, 0)); //Borda
		initializeKeys();
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
		
		sharps.maxHeightProperty().bind(heightProperty().divide(1.9));
	}
	
	public void resizeKeys(int width) {
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
		
		setMaxWidth(keyWidth * keyboard.getNaturalKeyCount());
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}
	
	public void setKeyTextShown(boolean keyTextShown) {
		for (Key k : keyboard) {
			k.setTextShown(keyTextShown);
		}
	}
}
