package br.concatto.fonicsynth;

public class Scale {
	public enum ScaleType {
		MAJOR, MINOR;
		
		@Override
		public String toString() {
			String original = super.toString();
			
			return original.charAt(0) + original.toLowerCase().substring(1);
		}
	};
	
	private String tonic;
	private ScaleType type;
	private int tempo; // In bpm
	private int octaves;
	private int startingOctave;
	
	public Scale(String tonic, ScaleType type, int tempo, int octaves, int startingOctave) {
		this.tonic = tonic;
		this.type = type;
		this.tempo = tempo;
		this.octaves = octaves;
		this.startingOctave = startingOctave;
	}
	
	public String getTonic() {
		return tonic;
	}
	
	public ScaleType getType() {
		return type;
	}
	
	public int getTempo() {
		return tempo;
	}
	
	public int getOctaves() {
		return octaves;
	}
	
	public int getStartingOctave() {
		return startingOctave;
	}
}
