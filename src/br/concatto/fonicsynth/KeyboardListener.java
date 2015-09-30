package br.concatto.fonicsynth;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardListener implements EventHandler<KeyEvent> {
	private Keyboard keyboard;
	private InstrumentPane echoes;

	public KeyboardListener(Keyboard keyboard, InstrumentPane echoes) {
		this.keyboard = keyboard;
		this.echoes = echoes;
	}
	
	@Override
	public void handle(KeyEvent e) {
		KeyCode code = e.getCode();
		boolean pressed = e.getEventType() == KeyEvent.KEY_PRESSED;
		
		if (code.isLetterKey() || code.isDigitKey()) {
			String name = code.getName();
			if (pressed) {
				keyboard.press(name, e.isShiftDown());
			} else {
				keyboard.release(name);
			}
		}
		
		if (code.isArrowKey() && pressed) {
			keyboard.releaseAll();
			if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
				if (e.isControlDown()) {
					echoes.changeDifference(code == KeyCode.RIGHT);
				} else {
					keyboard.changeTransposition(code == KeyCode.RIGHT);
				}
			} else if (code == KeyCode.UP || code == KeyCode.DOWN) {
				if (e.isControlDown()) {
					echoes.changeInstrument(code == KeyCode.UP);
				} else {
					keyboard.changeInstrument(code == KeyCode.UP);
				}
			}
		}
		
		if (code == KeyCode.ESCAPE && pressed) {
			keyboard.releaseAll();
			echoes.disableAll();                                                                        
		}
		
		if (code.isFunctionKey() && pressed) {
			//Mapeia F1-12 para 0-11
			int index = Integer.parseInt(code.getName().substring(1)) - 1;
			EchoCombination[] combinations = EchoCombination.values();
			if (index < combinations.length) {
				keyboard.releaseAll();
				echoes.loadCombination(combinations[index]);
			}
		}
		
		if (code == KeyCode.TAB && pressed) {
			echoes.invertEnabled();
		}
	};
}
