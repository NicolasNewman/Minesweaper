package application;

import java.util.Optional;

import game.enums.Difficulty;
import game.helper.Debugger;
import game.helper.Global;
import game.windows.Info;
import game.windows.Leaderboard;
import game.windows.Minesweeper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

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
	
	public void customClicked() {
		TextInputDialog size = new TextInputDialog();
		size.setTitle("Dimensions");
		size.setHeaderText("Please enter the size of the field (10 <= size <= 100)");
		size.setContentText("Size:");
		
		TextInputDialog density = new TextInputDialog();
		density.setTitle("Mine Percent");
		density.setHeaderText("Please enter the percent of the field that should be mines (10 <= % <= 50)");
		density.setContentText("Percent:");
		
		// Show dialog asking for density
		Optional<String> densityResult = density.showAndWait();
		densityResult.ifPresent((percent) -> {
			Debugger.DEBUG_print("Custom Generation", "Received " + percent + " as the density", true);
			try {
				// Convert density to an int
				int percentInt = Integer.parseInt(percent);
				if(percentInt < 10 || percentInt > 50) {
					Global.showError("InvalidNumberException", "Number not within specified range");
					Debugger.DEBUG_print("Custom Generation", "Density not within range 10 <= x <= 50. Asking again", true);
					customClicked();
				} else {
					// Show dialog asking for the board size
					Optional<String> result = size.showAndWait();
					result.ifPresent((dim) -> {
						Debugger.DEBUG_print("Custom Generation", "Received " + dim + " as the size of the board", true);
						try {
							int dimInt = Integer.parseInt(dim);
							if(dimInt > 100) {
								Debugger.DEBUG_print("Custom Generation", "Size greater than 100. Warning user", true);
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Warning");
								alert.setHeaderText("A size greater then 100 could cause the universe as we know it to end. Are you sure you would like to continue?");
								alert.showAndWait().ifPresent(selection -> {
									if(selection == ButtonType.OK) {
										Debugger.DEBUG_print("Custom Generation", "Generating " + dimInt + "x" + dimInt + " board with " + percentInt + "% of tiles being mines", true);
										Minesweeper game = new Minesweeper(dimInt, dimInt, percentInt, Difficulty.CUSTOM);
									} else {
										customClicked();
									}
								});
							} else if(dimInt >= 10) {
								Debugger.DEBUG_print("Custom Generation", "Generating " + dimInt + "x" + dimInt + " board with " + percentInt + "% of tiles being mines", true);
								Minesweeper game = new Minesweeper(dimInt, dimInt, percentInt, Difficulty.CUSTOM);
							} else {
								Debugger.DEBUG_print("Custom Generation", "Size not within range 10 <= x <= 100. Asking again", true);
								Global.showError("InvalidNumberException", "Number not within specified range");
								customClicked();
							}
						} catch(NumberFormatException e) {
							Debugger.DEBUG_print("Custom Generation", "Input received is NaN", true);
							Global.showError("NumberFormatException", "Please enter a valid number");
							customClicked();
						}
					});
				}
			} catch(NumberFormatException e) {
				Debugger.DEBUG_print("Custom Generation", "Input received is NaN", true);
				Global.showError("NumberFormatException", "Please enter a valid number");
				customClicked();
			}
		});
		
		
//		Optional<String> result = size.showAndWait();
//		result.ifPresent((dim) -> {
//			try {
//				int dimInt = Integer.parseInt(dim);
//				if(dimInt > 100) {
//					Alert alert = new Alert(AlertType.CONFIRMATION);
//					alert.setTitle("Warning");
//					alert.setHeaderText("A size greater then 100 could cause the universe as we know it to end. Are you sure you would like to continue?");
//					alert.showAndWait().ifPresent(selection -> {
//						if(selection == ButtonType.OK) {
//							Minesweeper game = new Minesweeper(dimInt, dimInt, 10, Difficulty.CUSTOM);
//						} else {
//							customClicked();
//						}
//					});
//				} else if(dimInt >= 10) {
//					Minesweeper game = new Minesweeper(dimInt, dimInt, 10, Difficulty.CUSTOM);
//				} else {
//					Global.showError("InvalidNumberException", "Number not within specified range");
//					customClicked();
//				}
//			} catch(NumberFormatException e) {
//				Global.showError("NumberFormatException", "Please enter a valid number");
//				customClicked();
//			}
//		});
	}
	
	public void leaderboardClicked() {
		Leaderboard board = new Leaderboard();
	}
	
	public void infoClicked() {
		Info info = new Info();
	}

}
