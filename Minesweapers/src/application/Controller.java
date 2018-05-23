package application;

import game.enums.Difficulty;
import game.windows.Leaderboard;
import game.windows.Minesweeper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
	
	public @FXML Button easy, medium, hard;
	
	public void easyClicked() {
		Minesweeper game = new Minesweeper(10, 10, 10, Difficulty.EASY);
	}
	
	public void mediumClicked() {
		Minesweeper game = new Minesweeper(25, 25, 10, Difficulty.MEDIUM);
	}
	
	public void hardClicked() {
		Minesweeper game = new Minesweeper(50, 50, 10, Difficulty.HARD);
	}
	
	public void leaderboardClicked() {
		Leaderboard board = new Leaderboard();
	}

}
