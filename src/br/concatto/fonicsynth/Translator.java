package br.concatto.fonicsynth;

public class Translator {
	static class DecodedData {
		public String key;
		public boolean shift;
		public boolean pressed;
		
		public DecodedData(String key, boolean shift, boolean pressed) {
			this.key = key;
			this.shift = shift;
			this.pressed = pressed;
		}
	}
	
	public static int encodePress(String key, boolean shift) {
		//Format: <press bit> <shift bit> <index bits>
		int data = 1 << 7;
		if (shift) {
			data = data | (1 << 6);
		}
		
		return data | encodeRelease(key);
	}
	
	public static int encodeRelease(String key) {
		return Keyboard.searchKey(Character.toLowerCase(key.charAt(0)));
	}
	
	public static DecodedData decode(int data) {
		int press = data >> 7;
		int shift = (data >> 6) & 0b01;
		int index = data & 0b00111111;
		return new DecodedData(String.valueOf(Keyboard.key(index)), shift == 1, press == 1);
	}
}
