package game.save_data;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import game.helper.Global;

@XmlRootElement
public class SettingManager {
	
	private boolean debugMode;
	
	public SettingManager() {
	}
	
	public SettingManager(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	@XmlElement
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	public boolean getDebugMode() {
		return debugMode;
	}
	
	public static void writeFile(SettingManager settings) {
		File f = new File(Global.SETTING_PATH);
		try {
			JAXBContext context = JAXBContext.newInstance(SettingManager.class);
			Marshaller marshaller = context.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(settings, f);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static SettingManager readFile() {
		File f = new File(Global.SETTING_PATH);
		try {
			JAXBContext context = JAXBContext.newInstance(SettingManager.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			return (SettingManager) unmarshaller.unmarshal(f);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}
