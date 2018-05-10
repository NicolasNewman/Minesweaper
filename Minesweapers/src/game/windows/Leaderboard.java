package game.windows;

import java.util.ArrayList;
import java.util.function.Consumer;

import game.enums.Difficulty;
import game.helper.Global;
import game.save_data.DataManager;
import game.save_data.PlayerScore;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Leaderboard {
	
	private Parent root;
	private Stage stage;
	private TabPane tabPane = new TabPane();
	private TableView[] tables = new TableView[3];
	private VBox vbox = new VBox();
	
	public Leaderboard() {
		stage = new Stage();
		stage.setTitle("Minesweaper");
		
		Tab easyTab = new Tab("Easy");
		easyTab.setClosable(false);
		Tab mediumTab = new Tab("Medium");
		mediumTab.setClosable(false);
		Tab hardTab = new Tab("Hard");
		hardTab.setClosable(false);
		Tab[] tabs = new Tab[] {easyTab, mediumTab, hardTab};
		Difficulty[] diffOrder = {Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD};
		
		DataManager m = new DataManager(Global.DATA_PATH);
		
		for(int i = 0; i < tables.length; i++) {
			final int indx = i;
			tables[i] = new TableView<PlayerScore>();
			TableColumn<PlayerScore, String> nameCol = new TableColumn<>("Name");
			nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			nameCol.setPrefWidth(100);
			TableColumn<PlayerScore, Number> scoreCol = new TableColumn<>("Score");
			scoreCol.setCellValueFactory(cellData -> cellData.getValue().getScorePropForDifficulty(diffOrder[indx]));
			scoreCol.setPrefWidth(100);
			tables[i].getColumns().add(nameCol);
			tables[i].getColumns().add(scoreCol);
			tables[i].setItems(m.readToObservableList());
			tabs[i].setContent(tables[i]);
			
			ArrayList<PlayerScore> scoresToRemove = new ArrayList<PlayerScore>();
			Consumer<PlayerScore> consumer = name -> {
				System.out.println(name.getName() + " : " + name.getScoreIntForDifficulty(diffOrder[indx]));
				if(name.getScoreIntForDifficulty(diffOrder[indx]) == -1) {
					scoresToRemove.add(name);
				}
			};
			tables[i].getItems().forEach(consumer);
			scoresToRemove.forEach((item) -> tables[indx].getItems().remove(item));
		}
		
		tabPane.getTabs().add(easyTab);
		tabPane.getTabs().add(mediumTab);
		tabPane.getTabs().add(hardTab);
		vbox.getChildren().add(tabPane);
		Scene scene = new Scene(vbox, 200, 400);
		
		stage.setScene(scene);
		stage.show();
	}
}
