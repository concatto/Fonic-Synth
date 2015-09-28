package br.concatto.fonicsynth;

import javafx.scene.paint.Color;

public class NaturalKey extends Key {
	private static final String[] NOTES = {"C", "D", "E", "F", "G", "A", "B"};
	
	public NaturalKey(int index) {
		super(String.valueOf(Keyboard.key(index)), NOTES[index % Keyboard.NATURAL_TONES], Color.SNOW, Color.GRAY);
	}

}
