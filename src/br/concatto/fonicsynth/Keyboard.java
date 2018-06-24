package br.concatto.fonicsynth;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

import javax.sound.midi.MidiUnavailableException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

@SuppressWarnings("serial")
public class Keyboard extends ArrayList<Key> {
	private static final char[] KEY_CHARS = {
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
			'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
			'z', 'x', 'c', 'v', 'b', 'n'
	};
	public static final int NATURAL_TONES = 7;
	private static final int NOTE_COUNT = 12;
	private static final int KEY_COUNT = 60;
	
	private IntegerProperty transposition = new SimpleIntegerProperty(0);
	private IntegerProperty instrument = new InstrumentProperty(0);
	private SoundController sound;
	private Echo[] echoes;
	private Consumer<KeyboardEvent> consumer;
	
	public Keyboard(int initialTransposition, Instruments initialInstrument, int echoQuantity) throws MidiUnavailableException {
		sound = new SoundController();
		echoes = new Echo[echoQuantity];
		for (int i = 0; i < echoes.length; i++) {
			echoes[i] = new Echo();
		}
		
		instrument.addListener((obs, o, n) -> {
			sound.changeInstrument(n.intValue());
			
			sendEvent(KeyboardEvent.INSTRUMENT, n.intValue());
		});
		
		transposition.addListener((obs, o, n) -> sendEvent(KeyboardEvent.TRANSPOSITION, n.intValue()));
		
		transposition.set(initialTransposition);
		instrument.set(initialInstrument.ordinal());
		
		for (int i = 0, k = 0; i < KEY_COUNT; i++) {
			Key key = isSharp(i) ? new SharpKey(k - 1) : new NaturalKey(k++);
			key.numberProperty().bind(this.transposition.multiply(NOTE_COUNT).add(12 + i).asString());
			add(key);
		}
	}
	
	public static boolean isSharp(int index) {
		index = index % NOTE_COUNT;
		return (index % 2 == (index <= 4 ? 1 : 0));
	}
	
	public static int searchKey(char key) {
		for (int i = 0; i < KEY_CHARS.length; i++) {
			if (KEY_CHARS[i] == key) return i;
		}
		
		return -1;
	}
	
	public static char key(int index) {
		return KEY_CHARS[index];
	}
	
	private Optional<Key> findByMIDI(int value) {
		for (Key key : this) {
			if (key.getNumber() == value) {
				return Optional.of(key);
			}
		}
		
		return Optional.empty();
	}
	
	public void pressByMIDI(int value) {
		findByMIDI(value).ifPresent(key -> {
			pressKey(key);
		});
	}
	
	private int findIndex(Key key) {
		for (int i = 0; i < size(); i++) {
			if (get(i) == key) return i;
		}
		
		return -1;
	}

	private Optional<Key> findKey(String text, boolean shift) {
		if (text.length() > 1) return Optional.empty();
		text = shift ? SharpKey.adjustKey(text.charAt(0)) : text.toLowerCase();
		
		for (Key key : this) {
			if (key.getKeyDigit().equals(text)) {
				return Optional.of(key);
			}
		}
		
		return Optional.empty();
	}
	
	public void press(int index) {
		pressKey(get(index));
	}
	
	public void press(String text, boolean shift) {
		findKey(text, shift).ifPresent(this::pressKey);
	}
	
	public void release(int index) {
		releaseKey(get(index));
	}
	
	public void release(String text) {
		findKey(text, true).ifPresent(this::releaseKey);
		findKey(text, false).ifPresent(this::releaseKey);
	}
	
	private void pressKey(Key key) {
		int nextIndex = findIndex(key) + 1;
		Key next = nextIndex < size() ? get(nextIndex) : key;
		int previousIndex = findIndex(key) - 1;
		Key previous = previousIndex > 0 ? get(previousIndex) : key;
		
		if (!key.pressed() && !(next instanceof SharpKey && next.pressed())
  				&& !(key instanceof SharpKey && previous instanceof NaturalKey && previous.pressed())) {
			key.press();
			sound.on(key.getNumber());
			playEchoes(key.getNumber());
			
			sendEvent(KeyboardEvent.PRESS, findIndex(key));
		}
	}
	
	private void releaseKey(Key key) {
		key.release();
		sound.off(key.getNumber());
		stopEchoes(key.getNumber());
		
		sendEvent(KeyboardEvent.RELEASE, findIndex(key));
	}

	public void changeInstrument(boolean increment) {
		if (KeyboardLimits.isInstrumentChangeWithinLimits(instrument.get(), increment)) {
			instrument.set(instrument.get() + (increment ? 1 : -1));
		}
	}
	
	public void changeTransposition(boolean increment) {
		if (KeyboardLimits.isTranspositionChangeWithinLimits(transposition.get(), increment)) {
			transposition.set(transposition.get() + (increment ? 1 : -1));
		}
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
		return stream().filter(key -> key instanceof NaturalKey).mapToInt(key -> 1).sum();
	}
	
	public void setInstrument(int instrument) {
		this.instrument.set(instrument);
	}
	
	public int getInstrument() {
		return instrument.get();
	}
	
	public IntegerProperty instrumentProperty() {
		return instrument;
	}
	
	public Echo[] getEchoes() {
		return echoes;
	}
	
	private void playEchoes(int note) {
		for (int i = 0; i < echoes.length; i++) {
			if (!echoes[i].isEnabled()) continue;
			
			sound.changeInstrument(echoes[i].getInstrument(), i + 1);
			sound.on(echoes[i].getOctave() * NOTE_COUNT + note, i + 1);
		}
	}
	
	private void stopEchoes(int note) {
		for (int i = 0; i < echoes.length; i++) {
			if (!echoes[i].isEnabled()) continue;
			
			sound.off(echoes[i].getOctave() * NOTE_COUNT + note, i + 1);
		}
	}

	public boolean isAnyPressed() {
		return stream().anyMatch(Key::pressed);
	}
	
	public void releaseAll() {
		stream().filter(key -> key.pressed()).forEach(this::releaseKey);
	}
	
	public void playDrum() {
		sound.on(49, 9);
	}

	public void switchMode() {
		int channel = sound.getDefaultChannel();
		sound.setDefaultChannel(channel == 0 ? 9 : 0);
	}
	
	public void sendEvent(byte type, int number) {
		if (consumer != null) {
			consumer.accept(new KeyboardEvent(type, (byte) number));
		}
	}
	
	public void setEventConsumer(Consumer<KeyboardEvent> consumer) {
		this.consumer = consumer;
	}
}
