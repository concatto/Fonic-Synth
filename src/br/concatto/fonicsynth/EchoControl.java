package br.concatto.fonicsynth;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class EchoControl extends VBox {
	private static final Background SELECTED_BACKGROUND = new Background(new BackgroundFill(Color.valueOf("#E5E5E5"), null, null));
	private static final Background REGULAR_BACKGROUND = null;
	
	private BooleanProperty selected = new SimpleBooleanProperty(false);
	private BooleanProperty enabled = new SimpleBooleanProperty(false);
	private Echo echo;
	
	private Label instrumentLabel = new Label();
	private Label octaveLabel = new Label();
	
	public EchoControl(Echo echo) {
		super(5);
		
		this.echo = echo;
		backgroundProperty().bind(Bindings.when(selected).then(SELECTED_BACKGROUND).otherwise(REGULAR_BACKGROUND));
		enabled.addListener((obs, o, n) -> echo.setEnabled(n.booleanValue()));
		
		ObjectBinding<Color> colorBinding = Bindings.when(enabled).then(Color.BLACK).otherwise(Color.SILVER);
		instrumentLabel.textFillProperty().bind(colorBinding);
		octaveLabel.textFillProperty().bind(colorBinding);
		
		getChildren().addAll(instrumentLabel, octaveLabel);
		setPadding(new Insets(16, 0, 16, 0));
		setAlignment(Pos.CENTER);
		
		updateLabels();
	}
	
	public void updateLabels() {
		instrumentLabel.setText("Instrumento: " + Instruments.get(echo.getInstrument()));
		int octave = echo.getOctave();
		octaveLabel.setText("Oitava: " + (octave > 0 ? "+" : "") + octave);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled.set(enabled);
	}
	
	public boolean isEnabled() {
		return enabled.get();
	}
	
	public Echo getEcho() {
		return echo;
	}
	
	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}
	
	public boolean isSelected() {
		return selected.get();
	}

	public void setEchoTo(Echo echo) {
		this.echo.setTo(echo);
		updateLabels();
	}
	
	public void changeInstrument(boolean increment) {
		echo.changeInstrument(increment);
	}
	
	public void changeOctave(boolean increment) {
		echo.changeOctave(increment);
	}
}
