package game.save_data;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import game.enums.Difficulty;
import game.helper.Debugger;
import game.helper.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manages the read/write events of the data file
 * @author QuantumPie
 *
 */
public class SecureDataManager {
	
	private String path;
	private DataEncrypter enc;
	private ObservableList<PlayerScore> scores = FXCollections.observableArrayList();
	private HashMap<String, PlayerScore> nameMap = new HashMap<>();
	private int key = 5;
	
	/**
	 * Initalize the DataEncrypter and path of the data file
	 * @param path of the data file
	 */
	public SecureDataManager(String path) {
		Debugger.DEBUG_print("Instance Created", "Instance of SecureDataManager created", true);
		try {
			enc = new DataEncrypter(Global.IV_PATH, Global.KEY_PATH);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchPaddingException | IOException e) {
			e.printStackTrace();
		}
		this.path = path;
	}
	
	/**
	 * Writes a score entry to the data file
	 * @param name of the user
	 * @param diff difficulty the score was set on
	 * @param score time the user took to win
	 */
	public void write(String name, String diff, String score) {
		try {
			enc.encryptString((name + ":" + diff + ":" + score + "\n"), path, true);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				| InvalidAlgorithmParameterException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Wipes the data in the event it is no longer readable 
	 * (asks for confirmation in game.windows.Leaderboard)
	 */
	public void wipe() {
		try(PrintWriter writer = new PrintWriter(path)) {
			writer.print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the ObservableList that is needed for the table in game.windows.Leaderboard
	 * @return
	 */
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
					if(l.contains("EASY") || l.contains("MEDIUM") || l.contains("HARD")) {
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
