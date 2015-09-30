package br.concatto.fonicsynth;

public class Echo {
	private int octave;
	private int instrument;
	private boolean enabled;

	public Echo() {
		enabled = false;
	}
	
	public Echo(int octave, Instruments instrument) {
		this(octave, instrument.ordinal());
	}
	
	public Echo(int octave, int instrument) {
		setOctave(octave);
		setInstrument(instrument);
		enabled = true;
	}
	
	public void changeOctave(boolean increment) {
		setOctave(getOctave() + (increment ? 1 : -1));
	}
	
	public void changeInstrument(boolean increment) {
		if (KeyboardLimits.isInstrumentChangeWithinLimits(getInstrument(), increment)) {
			setInstrument(getInstrument() + (increment ? 1 : -1));
		}
	}
	
	public void setOctave(int octave) {
		this.octave = octave;
	}
	
	public int getOctave() {
		return octave;
	}
	
	public void setInstrument(int instrument) {
		this.instrument = instrument;
	}
	
	public int getInstrument() {
		return instrument;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setTo(Echo echo) {
		setInstrument(echo.getInstrument());
		setOctave(echo.getOctave());
	}
}
