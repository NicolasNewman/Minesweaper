package game;

import java.io.IOException;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameInfo extends VBox {

	private boolean clockStarted = false;
	@FXML private Label minesLeft, time;
	
	public GameInfo() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameInfo.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		startClock();
	}
	
	public void setMinesLeft(int minesLeft) {
		this.minesLeft.setText(Integer.toString(minesLeft));
	}
	
	public void startClock() {
		if(!clockStarted) {
			clockStarted = true;

			Task t = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Timer t = new Timer();
					while(true) {
						Thread.sleep(1000);
						time.setText(t.getFormatedTime("mm:ss"));
					}
				}
			};
			//new Thread(t).start();
		}
	}
}
