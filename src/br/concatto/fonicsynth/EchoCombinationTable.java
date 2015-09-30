package br.concatto.fonicsynth;

import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class EchoCombinationTable extends GridPane {
	public EchoCombinationTable() {
		super();
		setBorder(new Border(new BorderStroke(Color.DIMGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.5))));
		
		fillTable();
		selectCombination(-1);
	}
	
	private void fillTable() {
		add(new EchoCombinationCell("Tecla", true), 0, 0);
		add(new EchoCombinationCell("Combinação", true), 1, 0);
		
		add(new EchoCombinationCell("Esc"), 0, 1);
		add(new EchoCombinationCell("Nenhum"), 1, 1);
		
		EchoCombination[] combinations = EchoCombination.values();
		for (int i = 0; i < combinations.length; i++) {
			add(new EchoCombinationCell("F" + (i + 1)), 0, i + 2);
			add(new EchoCombinationCell(combinations[i].getName()), 1, i + 2);
		}
	}
	
	public void selectCombination(int index) {
		for (Node node : getChildren()) {
			EchoCombinationCell cell = (EchoCombinationCell) node;
			if (GridPane.getRowIndex(node) == index + 2) {
				cell.select();
			} else {
				cell.deselect();
			}
		}
	}
}
