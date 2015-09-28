package br.concatto.fonicsynth;

public enum Instruments {
	STEREO_GRAND_PIANO("﻿Piano de cauda estéreo"),
	BRIGHT_GRAND_PIANO("Piano de cauda claro"),
	ELECTRIC_GRAND_PIANO("Piano de cauda elétrico"),
	HONKY_TONK("Honky-tonk"),
	TINE_PIANO("Piano elétrico (tine)"),
	FM_PIANO("Piano elétrico (FM)"),
	HARPSICHORD("Cravo"),
	CLAVINET("Clavinete"),
	CELESTA("Celesta"),
	GLOCKENSPIEL("Glockenspiel"),
	MUSIC_BOX("Caixa de música"),
	VIBRAPHONE("Vibrafone"),
	MARIMBA("Marimba"),
	XYLOPHONE("Xilofone"),
	TUBULAR_BELL("Sino tubular"),
	DULCIMER("Dulcimer"),
	TONEWHEEL_ORGAN("Órgão Hammond"),
	PERCUSSIVE_ORGAN("Órgão percussivo"),
	ROCK_ORGAN("Órgão de rock"),
	CHURCH_ORGAN("Órgão de igreja"),
	REED_ORGAN("Órgão palhetado"),
	ACCORDION("Acordeão"),
	HARMONICA("Gaita de boca"),
	BANDONEON("Bandoneón"),
	NYLON_GUITAR("Violão de nylon"),
	STEEL_GUITAR("Violão de aço"),
	JAZZ_GUITAR("Violão de jazz"),
	CLEAN_GUITAR("Violão agudo"),
	MUTED_GUITAR("Violão abafado"),
	OVERDRIVE_GUITAR("Guitarra amplificada"),
	DISTORTION_GUITAR("Guitarra distorcida"),
	GUITAR_HARMONICS("Harmônicos"),
	ACOUSTIC_BASS("Baixo acústico"),
	FINGERED_BASS("Baixo dedilhado"),
	PICKED_BASS("Baixo palhetado"),
	FRETLESS_BASS("Baixo fretless"),
	SLAP_BASS_1("Baixo de tapa 1"),
	SLAP_BASS_2("Baixo de tapa 2"),
	SYNTH_BASS_1("Baixo sintetizado 1"),
	SYNTH_BASS_2("Baixo sintetizado 2"),
	VIOLIN("Violino"),
	VIOLA("Viola"),
	CELLO("Violoncelo"),
	CONTRABASS("Contrabaixo"),
	TREMOLO_STRINGS("Tremolo"),
	PIZZICATO_STRINGS("Pizzicato"),
	HARP("Harpa"),
	TIMPANI("Tímpanos"),
	FAST_STRINGS("Cordas rápidas"),
	SLOW_STRINGS("Cordas lentas"),
	SYNTH_STRINGS_1("Cordas sintetizadas 1"),
	SYNTH_STRINGS_2("Cordas sintetizadas 2"),
	ORCHESTRA_CHOIR("Coral de concerto"),
	VOICE("Voz"),
	SYNTH_VOX("Voz sintetizada"),
	ORCHESTRA_HIT("Orchestra Hit"),
	TRUMPET("Trompete"),
	TROMBONE("Trombone"),
	TUBA("Tuba"),
	MUTED_TRUMPET("Trompete abafado"),
	FRENCH_HORNS("Trompa"),
	BRASS_SECTION("Seção dos Metais"),
	SYNTH_BRASS_1("Metal 1"),
	SYNTH_BRASS_2("Metal 2"),
	SOPRANO_SAX("Saxofone soprano"),
	ALTO_SAX("Saxofone alto"),
	TENOR_SAX("Saxofone tenor"),
	BARITONE_SAX("Saxofone barítono"),
	OBOE("Oboé"),
	ENGLISH_HORN("Corne inglês"),
	BASSOON("Fagote"),
	CLARINET("Clarinete"),
	PICCOLO("Flautim"),
	FLUTE("Flauta"),
	RECORDER("Flauta doce"),
	PAN_FLUTE("Flauta de pã"),
	BOTTLE_BLOW("Sopro em garrafa"),
	SHAKUHACHI("Shakuhachi"),
	WHISTLE("Assovio"),
	OCARINA("Ocarina"),
	SQUARE_WAVE("Onda quadrada"),
	SAW_WAVE("Onda dente-de-serra"),
	SYNTH_CALLIOPE("Caliope sintetizado"),
	CHIFFER_LEAD("Chiffer"),
	CHARANG("Charang"),
	SOLO_VOX("Voz solo"),
	FIFTH_SAW_WAVE("5ª onda dente-de-serra"),
	BASS_AND_LEAD("Baixo e líder"),
	FANTASIA("Fantasia"),
	WARM_PAD("Pedal quente"),
	POLYSYNTH("Polissintetizador"),
	SPACE_VOICE("Voz no espaço"),
	BOWED_GLASS("Vidro"),
	METAL_PAD("Pedal de metal"),
	HALO_PAD("Pedal de auréola"),
	SWEEP_PAD("Pedal sweep"),
	ICE_RAIN("Chuva de gelo"),
	SOUNDTRACK("Trilha sonora"),
	CRYSTAL("Cristal"),
	ATMOSPHERE("Atmosfera"),
	BRIGHTNESS("Clareza"),
	GOBLIN("Goblin"),
	ECHO_DROPS("Gotas em eco"),
	STAR_THEME("Tema das estrelas"),
	SITAR("Sitar"),
	BANJO("Banjo"),
	SHAMISEN("Shamisen"),
	KOTO("Koto"),
	KALIMBA("Kalimba"),
	BAGPIPE("Gaita de foles"),
	FIDDLE("Rabeca"),
	SHANAI("Shanai"),
	TINKLE_BELL("Jingle Bell"),
	AGOGO("Agogô"),
	STEEL_DRUMS("Bateria de aço"),
	WOODBLOCK("Bloco de madeira"),
	TAIKO_DRUM("Tambor Taiko"),
	MELODIC_TOM("Tom melódico"),
	SYNTH_DRUM("Bateria sintetizada"),
	REVERSE_CYMBAL("Chimbau reverso"),
	FRETNOISE("Fret"),
	BREATH_NOISE("Respiração"),
	SEASHORE("Praia"),
	BIRD("Pássaro"),
	TELEPHONE("Telefone"),
	HELICOPTER("Helicóptero"),
	APPLAUSE("Palmas"),
	GUN_SHOT("Tiro");
	
	private String name;
	Instruments(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static String get(int index) {
		return values()[index].name;
	}
}