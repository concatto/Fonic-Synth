package br.concatto.fonicsynth;

import java.util.Optional;
import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenuBar extends MenuBar {
	private Menu accompanimentMenu;
	private Consumer<Scale> scaleHandler;

	public MainMenuBar() {
		accompanimentMenu = new Menu("Accompaniment");
		MenuItem scaleItem = new MenuItem("Play a scale...");
		
		scaleItem.setOnAction(event -> {
			ScaleChooser chooser = new ScaleChooser();
			
			Optional<Scale> scale = chooser.showAndWait();
			
			if (scale.isPresent() && scaleHandler != null) {
				scaleHandler.accept(scale.get());
			}
		});
		
		accompanimentMenu.getItems().add(scaleItem);
		
		getMenus().addAll(accompanimentMenu);
	}

	public void onScaleRequested(Consumer<Scale> scaleHandler) {
		this.scaleHandler = scaleHandler;
		
	}
}
