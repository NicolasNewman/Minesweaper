package game.save_data;

import java.util.ArrayList;
import java.util.HashMap;

import game.enums.Difficulty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Contains the structure for each leaderboard entry
 * Used for the table in game.windows.Leaderboard
 * @author QuantumPie
 *
 */
public class PlayerScore {
	
	private final StringProperty name;
	private final IntegerProperty easyScore;
	private final IntegerProperty mediumScore;
	private final IntegerProperty hardScore;
	
	public PlayerScore(String name) {
		this.name = new SimpleStringProperty(name);
		this.easyScore = new SimpleIntegerProperty(-1);
		this.mediumScore = new SimpleIntegerProperty(-1);
		this.hardScore = new SimpleIntegerProperty(-1);
	}
	
	public String getName() {
		return name.get();
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public int getEasyScore() {
		return easyScore.get();
	}
	
	public IntegerProperty getScorePropForDifficulty(Difficulty diff) {
		if(diff.equals(Difficulty.EASY)) {
			return easyScore;
		} else if(diff.equals(Difficulty.MEDIUM)) {
			return mediumScore;
		} else {
			return hardScore;
		}
	}
	
	public int getScoreIntForDifficulty(Difficulty diff) {
		if(diff.equals(Difficulty.EASY)) {
			return easyScore.get();
		} else if(diff.equals(Difficulty.MEDIUM)) {
			return mediumScore.get();
		} else {
			return hardScore.get();
		}
	}
	
	public IntegerProperty easyScoreProperty() {
		return easyScore;
	}
	
	public void setEasyScore(int score) {
		this.easyScore.set(score);
	}
	
	public int getMediumScore() {
		return mediumScore.get();
	}
	
	public IntegerProperty mediumScoreProperty() {
		return mediumScore;
	}
	
	public void setMediumScore(int score) {
		this.mediumScore.set(score);
	}
	
	public int getHardScore() {
		return hardScore.get();
	}
	
	public IntegerProperty hardScoreProperty() {
		return hardScore;
	}
	
	public void setHardScore(int score) {
		this.hardScore.set(score);
	}
}
