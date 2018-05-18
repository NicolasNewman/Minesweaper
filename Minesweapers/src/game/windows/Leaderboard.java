package game.windows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import game.enums.Difficulty;
import game.helper.Global;
import game.save_data.DataManager;
import game.save_data.PlayerScore;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Leaderboard {
	
	private Parent root;
	private Stage stage;
	private TabPane tabPane = new TabPane();
	private TableView<PlayerScore>[] tables = new TableView[3];
	private DataManager m;
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
		
		m = new DataManager(Global.DATA_PATH);
		
		try {
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
				
				ObservableList<PlayerScore> observableList = m.readToObservableList();
				SortedList<PlayerScore> sortedList = new SortedList<>(observableList,
						(PlayerScore s1, PlayerScore s2) -> {
							if(s1.getScoreIntForDifficulty(diffOrder[indx]) > s2.getScoreIntForDifficulty(diffOrder[indx])) {
								return 1;
							} else if (s1.getScoreIntForDifficulty(diffOrder[indx]) < s2.getScoreIntForDifficulty(diffOrder[indx])) {
								return -1;
							} else {
								return 0;
							}
						});
				
				tables[i].setItems(sortedList);
				tabs[i].setContent(tables[i]);
				
				ArrayList<PlayerScore> scoresToRemove = new ArrayList<PlayerScore>();
				Consumer<PlayerScore> consumer = name -> {
					if(name.getScoreIntForDifficulty(diffOrder[indx]) == -1) {
						scoresToRemove.add(name);
					}
				};
				tables[i].getItems().forEach(consumer);
	
				scoresToRemove.forEach((item) -> observableList.remove(item));
			}
			
			tabPane.getTabs().add(easyTab);
			tabPane.getTabs().add(mediumTab);
			tabPane.getTabs().add(hardTab);
			vbox.getChildren().add(tabPane);
			Scene scene = new Scene(vbox, 200, 400);
			
			stage.setScene(scene);
			stage.show();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			dataCurrupted();
		} catch (NullPointerException e) {
			e.printStackTrace();
			dataCurrupted();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			dataCurrupted();
		}
	}
	
	public void dataCurrupted() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("The score data is courrupted. Would you like to reset it? (the leaderboards will not function until this issue is resolved)");
		alert.setHeaderText("Error");
		alert.showAndWait().ifPresent(response -> {
			if(response == ButtonType.OK) {
				m.wipe();
			}
		});
	}
}
