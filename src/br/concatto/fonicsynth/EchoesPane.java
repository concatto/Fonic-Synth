package br.concatto.fonicsynth;

import javafx.scene.layout.HBox;

public class EchoesPane extends HBox {
	private EchoControl[] echoControls;
	
	public EchoesPane(Echo[] echoes) {
		super();
		
		echoControls = new EchoControl[echoes.length];
		setMaxWidth(Double.MAX_VALUE);		
		for (int i = 0; i < echoes.length; i++) {
			EchoControl echo = new EchoControl(echoes[i]);
			echo.prefWidthProperty().bind(widthProperty().divide(echoes.length));
			echoControls[i] = echo;
			getChildren().add(echo);
		}
	}

	public void changeDifference(boolean increment) {
		for (EchoControl echo : echoControls) {
			if (echo.isSelected()) {
				echo.changeDifference(increment);
			}
		}
	}

	public void changeInstrument(boolean increment) {
		for (EchoControl echo : echoControls) {
			if (echo.isSelected()) {
				echo.changeInstrument(increment);
			}
		}
	}
	
	public void invertEnabled() {
		for (EchoControl echo : echoControls) {
			if (echo.isSelected()) {
				echo.setEnabled(!echo.isEnabled());
			}
		}
	}

	public int getQuantity() {
		return echoControls.length;
	}

	public void invertSelection(int index) {
		echoControls[index].setSelected(!echoControls[index].isSelected());
	}
	
	public EchoControl[] getEchoes() {
		return echoControls;
	}
}
