package game.save_data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import game.enums.Difficulty;
import game.helper.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DataManager {
	
	private String path;
	private DataEncrypter enc;
	private ObservableList<PlayerScore> scores = FXCollections.observableArrayList();
	private HashMap<String, PlayerScore> nameMap = new HashMap<>();
	private int key = 5;
	
	public DataManager(String path) {
		try {
			enc = new DataEncrypter(Global.IV_PATH, Global.KEY_PATH);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchPaddingException | IOException e) {
			e.printStackTrace();
		}
		this.path = path;
	}
	
	public void write(String name, String diff, String score) {
		try {
			enc.encryptString((name + ":" + diff + ":" + score + "\n"), path, true);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				| InvalidAlgorithmParameterException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void wipe() {
		try(PrintWriter writer = new PrintWriter(path)) {
			writer.print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<PlayerScore> readToObservableList() {
		scores = FXCollections.observableArrayList();
		Stream<String> stream = null;
		try {
			stream = Stream.of(enc.decryptString(path));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				| InvalidAlgorithmParameterException | IOException e1) {
			e1.printStackTrace();
		}
		stream.forEach((x) -> {
			String[] lines = x.split("\n");
			if(!lines[0].equals("")) {
				for(String l : lines) {
					// 0=name 1=diff 2=score
					String[] section = l.split(":");
					String name = section[0];
					String diff = section[1];
					String score = section[2];
	
					PlayerScore p = new PlayerScore(name);
					if(!nameMap.containsKey(name)) {
						nameMap.put(name, p);
					}
					
					if(Difficulty.valueOf(diff).equals(Difficulty.EASY)) {
						if(nameMap.get(name).getEasyScore() == -1) {
							nameMap.get(name).setEasyScore(Integer.parseInt(score));
						} else if(nameMap.get(name).getEasyScore() > Integer.parseInt(score)) {
							nameMap.get(name).setEasyScore(Integer.parseInt(score));
						}
					} else if(Difficulty.valueOf(diff).equals(Difficulty.MEDIUM)) {
						if(nameMap.get(name).getMediumScore() == -1) {
							nameMap.get(name).setMediumScore(Integer.parseInt(score));
						} else if(nameMap.get(name).getMediumScore() > Integer.parseInt(score)) {
							nameMap.get(name).setMediumScore(Integer.parseInt(score));
						}
					} else if(Difficulty.valueOf(diff).equals(Difficulty.HARD)) {
						if(nameMap.get(name).getHardScore() == -1) {
							nameMap.get(name).setHardScore(Integer.parseInt(score));
						} else if(nameMap.get(name).getHardScore() > Integer.parseInt(score)) {
							nameMap.get(name).setHardScore(Integer.parseInt(score));
						}
					}
				}
			}
			nameMap.forEach((name, score) -> {
				scores.add(score);
			});
		});
		return scores;
	}
	
	public void DEBUG_printMap() {
		nameMap.forEach((s, p) -> {
			System.out.println(s + ": " + p.getEasyScore());
		});
	}
}
