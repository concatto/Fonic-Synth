package br.concatto.fonicsynth;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class EchoCombinationCell extends Label {
	private static final Background NORMAL = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	private static final Background SELECTED = new Background(new BackgroundFill(Color.valueOf("#EAEAEA"), CornerRadii.EMPTY, Insets.EMPTY));
	private static final Background HEADER = new Background(new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY));
	
	private boolean header;
	
	public EchoCombinationCell(String text) {
		this(text, false);
	}
	
	public EchoCombinationCell(String text, boolean header) {
		super(text);
		this.header = header;
		
		setPadding(new Insets(3, 5, 3, 5));
		setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT, new Insets(-1))));
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		setMinWidth(50);
		setAlignment(Pos.CENTER);
		
		deselect();
	}
	
	public void select() {
		setBackground(SELECTED);
	}
	
	public void deselect() {
		setBackground(header ? HEADER : NORMAL);
	}
}
