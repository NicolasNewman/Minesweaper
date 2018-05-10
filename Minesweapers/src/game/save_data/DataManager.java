package game.save_data;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import game.enums.Difficulty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataManager {
	
	private String path;
	private ObservableList<PlayerScore> scores = FXCollections.observableArrayList();
	private HashMap<String, PlayerScore> nameMap = new HashMap<>();
	private int key = 5;
	
	public DataManager(String path) {
		this.path = path;
	}
	
	public void write(String name, String diff, String score) {
		//try(BufferedWriter writer = Files.newBufferedWriter(path)) {
		try(FileWriter writer = new FileWriter(path, true)) {
			String toWrite = name + ":" + diff + ":" + score + "\n";
			writer.write(DataEncrypter.encrypt(toWrite, key));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<PlayerScore> readToObservableList() {
		scores = FXCollections.observableArrayList();
		try(Stream<String> stream = Files.lines(Paths.get(path))) {
			stream.forEach((x) -> {
				x = DataEncrypter.decrypt(x, key);
				String[] lines = x.split("\n");
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
						nameMap.get(name).setEasyScore(Integer.parseInt(score));
					} else if(Difficulty.valueOf(diff).equals(Difficulty.MEDIUM)) {
						nameMap.get(name).setMediumScore(Integer.parseInt(score));
					} else if(Difficulty.valueOf(diff).equals(Difficulty.HARD)) {
						nameMap.get(name).setHardScore(Integer.parseInt(score));
					}
				}
				nameMap.forEach((name, score) -> {
					scores.add(score);
				});
			});
			return scores;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void DEBUG_printMap() {
		nameMap.forEach((s, p) -> {
			System.out.println(s + ": " + p.getEasyScore());
		});
	}

}
