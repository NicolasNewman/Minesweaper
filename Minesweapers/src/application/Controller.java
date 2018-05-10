package application;

import game.enums.Difficulty;
import game.windows.Leaderboard;
import game.windows.Minesweaper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
	
	public @FXML Button easy, medium, hard;
	
	public void easyClicked() {
		Minesweaper game = new Minesweaper(10, 10, 10, Difficulty.EASY);
	}
	
	public void mediumClicked() {
		Minesweaper game = new Minesweaper(25, 25, 10, Difficulty.MEDIUM);
	}
	
	public void hardClicked() {
		Minesweaper game = new Minesweaper(50, 50, 10, Difficulty.HARD);
	}
	
	public void leaderboardClicked() {
		Leaderboard board = new Leaderboard();
	}

}
