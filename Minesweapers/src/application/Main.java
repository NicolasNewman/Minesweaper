package application;
	
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import game.helper.Global;
import game.save_data.DataEncrypter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Global.loadNumbers();
			verifyDataExists();
			if(new String(Files.readAllBytes(Paths.get(Global.IV_PATH))).equals("")) {
				DataEncrypter.generateIV(Global.IV_PATH);
			}
			if(new String(Files.readAllBytes(Paths.get(Global.KEY_PATH))).equals("")) {
				DataEncrypter.generateKey(Global.KEY_PATH);
			}
			InputStream stream = Main.class.getResourceAsStream(Global.IV_PATH);
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
			Scene scene = new Scene(root);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Minesweaper");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void verifyDataExists() {
		File gameDir = new File(Global.GAME_PATH);
		File dataDir = new File(Global.GAME_DATA_PATH);
		if(!gameDir.exists()) {
			gameDir.mkdirs();
		}
		if(!dataDir.exists()) {
			dataDir.mkdirs();
		}
		File iv = new File(Global.IV_PATH);
		File key = new File(Global.KEY_PATH);
		File data = new File(Global.DATA_PATH);
		try {
			iv.createNewFile();
			key.createNewFile();
			data.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
