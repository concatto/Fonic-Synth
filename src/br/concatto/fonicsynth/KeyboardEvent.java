package br.concatto.fonicsynth;

public class KeyboardEvent {
	public static final byte PRESS = 0;
	public static final byte RELEASE = 1;
	public static final byte INSTRUMENT = 2;
	public static final byte TRANSPOSITION = 3;
	public static final byte COMBINATION = 4;
	
	private byte eventType;
	private byte eventData;
	
	public KeyboardEvent(byte eventType, byte eventData) {
		this.eventType = eventType;
		this.eventData = eventData;
	}
	
	public byte getEventType() {
		return eventType;
	}
	
	public byte getEventData() {
		return eventData;
	}
}
