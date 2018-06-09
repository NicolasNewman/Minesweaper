package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;

import game.helper.Debugger;
import game.helper.Global;
import game.save_data.SecureDataManager;
import game.save_data.SettingManager;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;

public class InfoController {
	
	@FXML Hyperlink infoLink;
	@FXML CheckBox debugCheckbox;
	
	public InfoController() {

	}
	
	@FXML
	public void initialize() {
		debugCheckbox.setSelected(Global.settings.getDebugMode());
	}
	
	public void infoLinkClicked() {
		String link = infoLink.getText();
		debugCheckbox.setSelected(true);
		try {
			Desktop.getDesktop().browse(new URL(link).toURI());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void settingDebugChanged() {
		if(debugCheckbox.isSelected()) {
			Global.DEBUG_MODE = true;
			Global.settings.setDebugMode(true);
			SettingManager.writeFile(Global.settings);
			try {
				File log = new File(Global.LOG_PATH);
				Debugger.DEBUG_print("Setting Event", "DEBUG_MODE is now " + Global.DEBUG_MODE + ". Should be true", true);
				if(!log.exists()) {
					log.createNewFile();
					Debugger.initLogWriter();
					Debugger.DEBUG_print("Setting Event", "Log file doesn't exist, creating now", true);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Global.DEBUG_MODE = false;
			Global.settings.setDebugMode(false);
			SettingManager.writeFile(Global.settings);
			Debugger.DEBUG_print("Setting Event", "DEBUG_MODE is now " + Global.DEBUG_MODE + ". Should be false", true);
		}
	}
}
