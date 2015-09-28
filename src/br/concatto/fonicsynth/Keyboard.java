package br.concatto.fonicsynth;

import java.util.ArrayList;
import java.util.Optional;

import javax.sound.midi.MidiUnavailableException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

@SuppressWarnings("serial")
public class Keyboard extends ArrayList<Key> {
	private static final char[] KEYS = {
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
			'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
			'z', 'x', 'c', 'v', 'b', 'n'
	};
	public static final int NATURAL_TONES = 7;
	
	private IntegerProperty transposition = new SimpleIntegerProperty();
	private int instrument;
	private SoundController sound;
	private Echo[] echoes;
	
	public Keyboard(int transposition, int echoQuantity) throws MidiUnavailableException {
		sound = new SoundController();
		this.transposition.set(transposition);
		
		echoes = new Echo[echoQuantity];
		for (int i = 0; i < echoes.length; i++) {
			echoes[i] = new Echo();
		}
		
		for (int i = 0, k = 0; i < 60; i++) {
			Key key = isSharp(i) ? new SharpKey(k - 1) : new NaturalKey(k++);
			key.numberProperty().bind(this.transposition.multiply(12).add(i).asString());
			add(key);
		}
	}
	
	public static boolean isSharp(int index) {
		index = index % 12;
		return (index % 2 == (index <= 4 ? 1 : 0));
	}
	
	public static char key(int index) {
		return KEYS[index];
	}
	
	private int findIndex(Key key) {
		for (int i = 0; i < size(); i++) {
			if (get(i) == key) return i;
		}
		
		return -1;
	}

	private Optional<Key> find(String text, boolean shift) {
		if (text.length() > 1) return Optional.empty();
		text = shift ? SharpKey.adjustKey(text.charAt(0)) : text.toLowerCase();
		
		for (Key key : this) {
			if (key.getKeyDigit().equals(text)) {
				return Optional.of(key);
			}
		}
		
		return Optional.empty();
	}
	
	public void press(String text, boolean shift) {
		find(text, shift).ifPresent(this::pressKey);
	}
	
	public void release(String text) {
		find(text, true).ifPresent(this::releaseKey);
		find(text, false).ifPresent(this::releaseKey);
	}
	
	private void pressKey(Key key) {
		int nextIndex = findIndex(key) + 1;
		Key next = nextIndex < size() ? get(nextIndex) : key;
		if (!key.pressed() && !(next instanceof SharpKey && next.pressed())) {
			key.press();
			sound.on(key.getNumber());
			playEchoes(key.getNumber());
		}
	}
	
	private void releaseKey(Key key) {
		key.release();
		sound.off(key.getNumber());
		stopEchoes(key.getNumber());
	}

	public void setTransposition(int transposition) {
		this.transposition.set(transposition);
	}
	
	public int getTransposition() {
		return transposition.get();
	}
	
	public IntegerProperty transpositionProperty() {
		return transposition;
	}
	
	public int getNaturalKeyCount() {
		return (int) stream().filter(key -> key instanceof NaturalKey).count();
	}
	
	public void setInstrument(int instrument) {
		this.instrument = instrument;
		sound.changeInstrument(instrument);
	}
	
	public int getInstrument() {
		return instrument;
	}
	
	public Echo[] getEchoes() {
		return echoes;
	}
	
	private void playEchoes(int note) {
		for (int i = 0; i < echoes.length; i++) {
			if (!echoes[i].isEnabled()) continue;
			
			sound.changeInstrument(echoes[i].getInstrument(), i + 1);
			sound.on(echoes[i].getOctave() * 12 + note, i + 1);
		}
	}
	
	private void stopEchoes(int note) {
		for (int i = 0; i < echoes.length; i++) {
			if (!echoes[i].isEnabled()) continue;
			
			sound.off(echoes[i].getOctave() * 12 + note, i + 1);
		}
	}

	public boolean isAnyPressed() {
		for (Key key : this) {
			if (key.pressed()) return true;
		}
		return false;
	}
}
