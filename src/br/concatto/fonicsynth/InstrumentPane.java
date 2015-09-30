package br.concatto.fonicsynth;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class InstrumentPane extends HBox {
	private HBox echoControlsContainer = new HBox();
	private Label instrumentName = new Label();
	private VBox instrumentsContainer = new VBox(instrumentName, echoControlsContainer);
	private EchoCombinationTable echoTable = new EchoCombinationTable();
	private EchoControl[] echoControls;
	
	public InstrumentPane(Keyboard keyboard) {
		super();
		
		Echo[] echoes = keyboard.getEchoes();
		echoControls = new EchoControl[echoes.length];
		setMaxWidth(Double.MAX_VALUE);		
		for (int i = 0; i < echoes.length; i++) {
			EchoControl echoControl = new EchoControl(echoes[i]);
			echoControl.prefWidthProperty().bind(echoControlsContainer.widthProperty().divide(echoes.length));
			echoControls[i] = echoControl;
			echoControlsContainer.getChildren().add(echoControl);
		}
		
		instrumentName.setFont(Font.font(19));
		instrumentName.setAlignment(Pos.CENTER);
		instrumentName.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		instrumentName.setPadding(new Insets(15, 0, 10, 0));
		instrumentName.textProperty().bind(keyboard.instrumentProperty().asString());
		
		getChildren().addAll(echoTable, instrumentsContainer);
		HBox.setHgrow(instrumentsContainer, Priority.ALWAYS);
		VBox.setVgrow(echoControlsContainer, Priority.ALWAYS);
	}

	public void changeDifference(boolean increment) {
		for (EchoControl echoControl : echoControls) {
			if (echoControl.isSelected()) {
				echoControl.changeOctave(increment);
			}
		}
	}

	public void changeInstrument(boolean increment) {
		for (EchoControl echoControl : echoControls) {
			if (echoControl.isSelected()) {
				echoControl.changeInstrument(increment);
			}
		}
	}
	
	public void invertEnabled() {
		for (EchoControl echoControl : echoControls) {
			if (echoControl.isSelected()) {
				echoControl.setEnabled(!echoControl.isEnabled());
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
	
	public void loadCombination(EchoCombination combination) {
		echoTable.selectCombination(combination.ordinal());
		Echo[] echoes = combination.getEchoes();
		for (int i = 0; i < echoControls.length; i++) {
			if (i >= echoes.length) {
				echoControls[i].setEnabled(false);
			} else {
				echoControls[i].setEnabled(true); 
				echoControls[i].setEchoTo(echoes[i]);
			}
		}
	}
	
	public void disableAll() {
		echoTable.selectCombination(-1);
		for (EchoControl echoControl : echoControls) {
			echoControl.setEnabled(false);
		}
	}
}
