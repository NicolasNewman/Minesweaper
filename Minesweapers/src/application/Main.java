package application;
	
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
			if(new String(Files.readAllBytes(Paths.get(Global.IV_PATH))).equals("")) {
				DataEncrypter.generateIV(Global.IV_PATH);
			}
			if(new String(Files.readAllBytes(Paths.get(Global.KEY_PATH))).equals("")) {
				DataEncrypter.generateKey(Global.KEY_PATH);
			}
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
}
