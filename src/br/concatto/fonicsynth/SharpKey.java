package br.concatto.fonicsynth;

import javafx.scene.paint.Color;

public class SharpKey extends Key {
	private static final String[] NOTES = {"C#", "D#", "F#", "G#", "A#"};
	
	public SharpKey(int index) {
		super(adjustKey(index), adjustNote(index), Color.valueOf("#555555"), Color.valueOf("#0F0F0F"));
	}
	
	private static String adjustNote(int index) {
		index = index % Keyboard.NATURAL_TONES;
		if (index >= 2) index--;
		
		return NOTES[index];
	}

	public static String adjustKey(char key) {
		if (Character.isDigit(key)) {
			char[] chars = {'^', key};
			return String.valueOf(chars);
		}
		
		return String.valueOf(Character.toUpperCase(key));
	}
	
	public static String adjustKey(int index) {
		return adjustKey(Keyboard.key(index));
	}

	public SharpKey(boolean visible) {
		this(0);
		setVisible(visible);
	}
}
