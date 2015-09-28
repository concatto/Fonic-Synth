package br.concatto.fonicsynth;

import java.io.IOException;
import java.net.URL;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class SoundController {
	private Synthesizer synth;
	private MidiChannel channels[];
	private Instrument[] instruments;
	
	public SoundController() throws MidiUnavailableException {
		synth = MidiSystem.getSynthesizer();
		synth.open();
		
		try {
			URL url = SoundController.class.getClassLoader().getResource("genusrmusescore.sf2");
			instruments = MidiSystem.getSoundbank(url).getInstruments();
		} catch (InvalidMidiDataException | IOException e) {
			instruments = synth.getDefaultSoundbank().getInstruments();
			e.printStackTrace();
		}
		
		channels = synth.getChannels();
	}
	
	public void on(int note) {
		on(note, 0);
	}
	
	public void off(int note) {
		off(note, 0);
	}
	
	public void changeInstrument(int instrument) {
		changeInstrument(instrument, 0);
	}
	
	public void on(int note, int channel) {
		if (KeyboardLimits.isWithinLimits(note, KeyboardLimits.MIN_NOTE, KeyboardLimits.MAX_NOTE)) {
			channels[channel].noteOn(note, 127);
		}
	}
	
	public void off(int note, int channel) {
		channels[channel].noteOff(note);
	}
	
	public void changeInstrument(int instrument, int channel) {
		int currentInstrument = channels[channel].getProgram();
		if (currentInstrument != instrument) {
			synth.loadInstrument(instruments[instrument]);
			channels[channel].programChange(instrument);
		}
	}
}
