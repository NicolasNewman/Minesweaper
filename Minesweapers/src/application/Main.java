package application;
	
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import game.helper.Debugger;
import game.helper.Global;
import game.save_data.DataEncrypter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		checkVersion();
		primaryStage.getIcons().add(new Image("file:resources/images/Minesweeper.png"));
		Global.bootTime = Global.getNow().format(Global.fileFormat);
		Global.LOG_PATH += Global.bootTime + ".log";
		primaryStage.setOnCloseRequest(event -> {
			Debugger.DEBUG_print("Exit Event", "Program exiting, saving log", true);
			Debugger.closeLogWriter();
			Platform.exit();
		});
		
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
			primaryStage.setTitle("Minesweeper");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Checks to make sure each needed directory exists. If not, create it
	 * Also creates the log file if debug mode is enabled
	 */
	public static void verifyDataExists() {
		File gameDir = new File(Global.GAME_PATH);
		File dataDir = new File(Global.GAME_DATA_PATH);
		File logDir = new File(Global.GAME_LOG_PATH);
		if(!gameDir.exists()) {
			gameDir.mkdirs();
		}
		if(!dataDir.exists()) {
			dataDir.mkdirs();
		}
		if(!logDir.exists()) {
			logDir.mkdirs();
		}
		try {
			File iv = new File(Global.IV_PATH);
			File key = new File(Global.KEY_PATH);
			File data = new File(Global.DATA_PATH);
			iv.createNewFile();
			key.createNewFile();
			data.createNewFile();
			if(Global.DEBUG_MODE) {
				File log = new File(Global.LOG_PATH);
				log.createNewFile();
				Debugger.initLogWriter();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: find a better way to do this
	/**
	 * Checks if there is an updated version of the game.
	 * If there is, ask the user if they would like to update
	 */
	public static void checkVersion() {
		RepositoryService service = new RepositoryService();
		try {
			Repository repo = service.getRepository("NicolasNewman", "Minesweeper");
			String version = repo.getDescription();
			boolean versionMatch = version.equals(Global.VERSION) ? true : false;
			if(!versionMatch) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Warning");
				alert.setContentText("An updated version is available. Would you like to update?");
				ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
				ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
				alert.getButtonTypes().setAll(okButton, noButton);
	
				alert.showAndWait().ifPresent(option -> {
					if(option == ButtonType.OK) {
						try {
							Desktop.getDesktop().browse(new URL("https://github.com/NicolasNewman/Minesweeper/releases").toURI());
						} catch (IOException | URISyntaxException e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
