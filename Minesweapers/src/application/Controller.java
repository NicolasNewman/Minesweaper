package application;

import game.Minesweaper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
	
	public @FXML Button easy, medium, hard;
	
	public void easyClicked() {
		Minesweaper game = new Minesweaper(10, 10, 10);
	}
	
	public void mediumClicked() {
		Minesweaper game = new Minesweaper(25, 25, 10);
	}
	
	public void hardClicked() {
		Minesweaper game = new Minesweaper(50, 50, 10);
	}

}
