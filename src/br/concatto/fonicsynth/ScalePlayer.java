package br.concatto.fonicsynth;

import java.util.Arrays;

import br.concatto.fonicsynth.Scale.ScaleType;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public class ScalePlayer {
	private Keyboard keyboard;

	public ScalePlayer(Keyboard keyboard) {
		this.keyboard = keyboard;
		
	}

	public int toMIDI(String noteName, int octave) {
		char note = noteName.charAt(0);
		int[] values = {9, 11, 0, 2, 4, 5, 7};
		
		int baseNumber = values[Character.toUpperCase(note) - 'A'];
		boolean sharp = noteName.length() > 1 && noteName.charAt(1) == '#';
		boolean flat = noteName.length() > 1 && noteName.charAt(1) == 'b';
		
		if (sharp) baseNumber++;
		if (flat) baseNumber--;
		
		return baseNumber + (12 * (octave + 1));
	}
	
	private int[] getIntervals(ScaleType type) {
		if (type == ScaleType.MAJOR) {
			return new int[] {0, 2, 2, 1, 2, 2, 2};
		} else if (type == ScaleType.MINOR) {
			return new int[] {0, 2, 1, 2, 2, 2, 1};
		}
		
		return null;
	}
	
	public int getScaleNote(Scale scale, int index) {
		int octave = index / 7;
		int base = toMIDI(scale.getTonic(), scale.getStartingOctave() + octave);
		
		int[] intervals = getIntervals(scale.getType());
		int offset = Arrays.stream(intervals, 0, (index % 7) + 1).sum();
		
		return base + offset;
	}
	
	public void play(Scale scale) {
		int start = 0;
		int end = scale.getOctaves() * 7 + start;
		
		SimpleIntegerProperty pitch = new SimpleIntegerProperty(start);
		SimpleBooleanProperty ascending = new SimpleBooleanProperty(true);
		
		int originalInstrument = keyboard.getInstrument();
		Duration duration = Duration.seconds(60.0 / scale.getTempo());
		
		KeyFrame scaleFrame = new KeyFrame(duration, event -> {
			keyboard.setInstrument(originalInstrument);
			keyboard.releaseAll();

			int note = getScaleNote(scale, pitch.get());
			
			keyboard.pressByMIDI(note);
			
			if (pitch.get() == end) {
				ascending.set(false);
			}
			
			pitch.set(pitch.get() + (ascending.getValue() ? 1 : -1));
		});
		
		Timeline scaleTimeline = new Timeline(scaleFrame);
		scaleTimeline.setCycleCount(2 * 7 * scale.getOctaves() + 1);
		
		KeyFrame beatFrame = new KeyFrame(duration, event -> {
			keyboard.setInstrument(Instruments.CLEAN_GUITAR.ordinal());
			keyboard.releaseAll();
			keyboard.pressByMIDI(toMIDI("A", 4));
		});
		
		Timeline beatTimeline = new Timeline(beatFrame);
		beatTimeline.setCycleCount(4);
		
		Timeline endTimeline = new Timeline(new KeyFrame(Duration.ONE, e -> keyboard.releaseAll()));
		endTimeline.setDelay(duration.multiply(2));
		
		SequentialTransition sequence = new SequentialTransition(beatTimeline, scaleTimeline, endTimeline);
		sequence.play();
	}
}
