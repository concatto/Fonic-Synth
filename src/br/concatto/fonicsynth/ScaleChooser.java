package br.concatto.fonicsynth;

import br.concatto.fonicsynth.Scale.ScaleType;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ScaleChooser extends Dialog<Scale> {
	public ScaleChooser() {
		setTitle("Choose a scale");
		
		VBox root = new VBox(15);
		GridPane top = new GridPane();
		GridPane bottom = new GridPane();
		
		String[] tonics = {"C", "A", "G", "E", "F", "D", "B", "Bb", "F#", "Eb", "C#", "Ab", "G#", "Db", "D#"};
		ScaleType[] types = ScaleType.values();
		
		ComboBox<String> tonicChooser = new ComboBox<>(FXCollections.observableArrayList(tonics));
		ComboBox<ScaleType> typeChooser = new ComboBox<>(FXCollections.observableArrayList(types));
		
		tonicChooser.setValue(tonics[0]);
		typeChooser.setValue(types[0]);
		
		top.setHgap(10);
		top.add(new Label("Tonic"), 0, 0);
		top.add(tonicChooser, 0, 1);
		top.add(new Label("Type"), 1, 0);
		top.add(typeChooser, 1, 1);
		
		TextField tempoField = new TextField("60");
		ComboBox<Integer> octavesChooser = new ComboBox<>(FXCollections.observableArrayList(1, 2, 3));
		ComboBox<Integer> startChooser = new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6));
		
		tempoField.setPrefWidth(50);
		octavesChooser.setValue(2);
		startChooser.setValue(3);
		
		bottom.setHgap(10);
		bottom.add(new Label("Tempo"), 0, 0);
		bottom.add(tempoField, 0, 1);
		bottom.add(new Label("Octaves"), 1, 0);
		bottom.add(octavesChooser, 1, 1);
		bottom.add(new Label("Starting octave"), 2, 0);
		bottom.add(startChooser, 2, 1);
		root.getChildren().addAll(top, bottom);
		
		getDialogPane().setContent(root);
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				String tonic = tonicChooser.getValue();
				ScaleType type = typeChooser.getValue();
				int tempo = Integer.parseInt(tempoField.getText());
				int octaves = octavesChooser.getValue();
				int startingOctave = startChooser.getValue();
				
				return new Scale(tonic, type, tempo, octaves, startingOctave);
			}
			
			return null;
		});
	}
}
