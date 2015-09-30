package br.concatto.fonicsynth;

import static br.concatto.fonicsynth.Instruments.*;

public enum EchoCombination {
	PIANO("Pianos",
		new Echo(-1, BRIGHT_GRAND_PIANO),
		new Echo(-1, ELECTRIC_GRAND_PIANO),
		new Echo(1, BRIGHT_GRAND_PIANO)
	), CHROMATIC("Progressão cromática",
		new Echo(-1, CELESTA),
		new Echo(1, GLOCKENSPIEL),
		new Echo(1, DULCIMER)
	), CHURCH("Igreja",
		new Echo(-2,CHURCH_ORGAN),
		new Echo(-1, CHURCH_ORGAN),
		new Echo(1, ORCHESTRA_CHOIR)
	), GAUCHO("Gaúcho",
		new Echo(-1, ACCORDION),
		new Echo(0, ACCORDION),
		new Echo(1, HARMONICA)
	), GUITAR("Violões",
		new Echo(-1, NYLON_GUITAR),
		new Echo(1, STEEL_GUITAR),
		new Echo(0, CLEAN_GUITAR)
	), HEAVY_METAL("Heavy metal",
		new Echo(0, DISTORTION_GUITAR),
		new Echo(1, DISTORTION_GUITAR),
		new Echo(0, OVERDRIVE_GUITAR)
	), VIOLINS("Violinos",
		new Echo(1, FAST_STRINGS),
		new Echo(0, VIOLIN),
		new Echo(-1, CONTRABASS)
	), CHOIR_AND_STRINGS("Coral e cordas",
		new Echo(1, FAST_STRINGS),
		new Echo(-1, SLOW_STRINGS),
		new Echo(0, ORCHESTRA_CHOIR)
	), BRASS("Trompetes e trombones",
		new Echo(-2, TROMBONE),
		new Echo(0, TRUMPET),
		new Echo(-2, FRENCH_HORNS)
	), SAX("Saxofones",
		new Echo(0, ALTO_SAX),
		new Echo(-1, BARITONE_SAX)
	);
	
	private String name;
	private Echo[] echoes;
	EchoCombination(String name, Echo... echoes) {
		this.name = name;
		this.echoes = echoes;
	}
	
	public String getName() {
		return name;
	}
	
	public Echo[] getEchoes() {
		return echoes;
	}
}