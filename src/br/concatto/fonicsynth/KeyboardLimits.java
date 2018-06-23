package br.concatto.fonicsynth;

public abstract class KeyboardLimits {
	public static final int MAX_TRANSPOSITION = 5;
	public static final int MIN_TRANSPOSITION = -1;
	public static final int MAX_INSTRUMENT = 127;
	public static final int MIN_INSTRUMENT = 0;
	public static final int MAX_NOTE = 127;
	public static final int MIN_NOTE = 0;
	
	public static boolean isInstrumentChangeWithinLimits(int value, boolean increment) {
		return isChangeWithinLimits(value, increment, MIN_INSTRUMENT, MAX_INSTRUMENT);
	}
	
	public static boolean isTranspositionChangeWithinLimits(int value, boolean increment) {
		return isChangeWithinLimits(value, increment, MIN_TRANSPOSITION, MAX_TRANSPOSITION);
	}
	
	public static boolean isChangeWithinLimits(int value, boolean increment, int min, int max) {
		return increment ? value < max : value > min;
	}
	
	public static boolean isWithinLimits(int value, int min, int max) {
		return value <= max && value >= min;
	}
}
