package game.windows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import game.enums.Difficulty;
import game.enums.TileState;
import game.helper.Debugger;
import game.helper.Global;
import game.save_data.SecureDataManager;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Minesweeper {
	
	private Stage stage;
	private VBox vbox = new VBox();
	private GridPane grid = new GridPane();
	private GameInfo info;
	private ImageView[][] tiles;
	private TileState[][] state;
	private SecureDataManager dataManager = new SecureDataManager(Global.DATA_PATH);
	
	private int H = 10;
	private int W = 10;
	private int mineCount = 10;
	private int flagsRemaining = 10;
	private int nullRegionSize = 1;
	private double tempMineCount;
	private boolean gameOver = false;
	private boolean firstClick = true;
	private boolean hasCheated = false;
	private final Difficulty diff;
	
	public Minesweeper(int W, int H, int tempMineCount, Difficulty diff) {
		this.W = W;
		this.H = H;
		this.tempMineCount = tempMineCount;
		this.diff = diff;
		createWindow();
	}
	
	private void createWindow() {
		stage = new Stage();
		stage.setTitle("Minesweeper");
		
//		TextInputDialog lengthDialog = new TextInputDialog("10");
//		lengthDialog.setTitle("Length");
//		lengthDialog.setHeaderText("Please enter the length of the field");
//		lengthDialog.setContentText("Length:");
//					
//		Optional<String> lengthResult = lengthDialog.showAndWait();
//		lengthResult.ifPresent(letter -> {
//			H = Integer.parseInt(letter);
//		});
//		
//		TextInputDialog widthDialog = new TextInputDialog("10");
//		widthDialog.setTitle("Width");
//		widthDialog.setHeaderText("Please enter the width of the field");
//		widthDialog.setContentText("Width:");
//					
//		Optional<String> widthResult = widthDialog.showAndWait();
//		widthResult.ifPresent(letter -> {
//			W = Integer.parseInt(letter);
//		});
//		
//		TextInputDialog densityDialog = new TextInputDialog("10");
//		densityDialog.setTitle("Percent");
//		densityDialog.setHeaderText("Please enter the percent of field that should be mines");
//		densityDialog.setContentText("Percent:");
//		
//		Optional<String> densityResult = densityDialog.showAndWait();
//		densityResult.ifPresent(letter -> {
//			tempMineCount = Double.parseDouble(letter);
//		});
		
		mineCount = (int) ((W*H) * (tempMineCount/100.0));

		flagsRemaining = mineCount;
		tiles = new ImageView[W][H];
		state = new TileState[W][H];
		
		if(W == H) {
			for(int i = 32; i > 0; i--) {
				if(i*(W+2) <= 800) {
					Global.FRAME_WIDTH = i*(W+2);
					Global.FRAME_HEIGHT = i*(H+2);
					Global.TILE_HEIGHT = i;
					Global.TILE_WIDTH = i;
					break;
				}
			}
		}
		System.out.println(Global.FRAME_WIDTH);
		System.out.println(Global.TILE_WIDTH);
		System.out.println(100/(W+2));
		System.out.println(100.0/(W+2));
		System.out.println((100/(W+2))*(W+2));
		System.out.println("----------------------");
		Debugger.DEBUG_print("Create Field", "W set to " + W, true);
		Debugger.DEBUG_print("Create Field", "FRAME_WIDTH set to " + Global.FRAME_WIDTH, true);
		Debugger.DEBUG_print("Create Field", "TILE_WIDTH set to " + Global.TILE_WIDTH, true);
		Debugger.DEBUG_print("Create Field", "H set to " + H, true);
		Debugger.DEBUG_print("Create Field", "FRAME_HEIGHT set to " + Global.FRAME_HEIGHT, true);
		Debugger.DEBUG_print("Create Field", "TILE_HEIGHT set to " + Global.TILE_HEIGHT, true);
//		for(int i = 0; i < W+2; i++) {
//			ColumnConstraints column = new ColumnConstraints();
//			column.setPercentWidth(100/(W+2));
//			grid.getColumnConstraints().add(column);
//		}
//		for(int i = 0; i < H+2; i++) {
//			RowConstraints row = new RowConstraints();
//			row.setPercentHeight(100.0/(H+2));
//			grid.getRowConstraints().add(row);
//		}
		grid.setPrefSize(Global.FRAME_WIDTH, Global.FRAME_HEIGHT);
		grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		
		info = new GameInfo(W, H);
		
		info.updateMines(((flagsRemaining / 1000) % 10), 
				(flagsRemaining / 100) % 10, 
				(flagsRemaining / 10) % 10, 
				flagsRemaining % 10);
		
		vbox.getChildren().add(info.getGrid());
		vbox.getChildren().add(grid);
		Scene scene = new Scene(vbox, Global.FRAME_WIDTH, Global.FRAME_HEIGHT+(Global.TILE_HEIGHT*3));
		
		generateField();

		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Generates the field and sets the listener for each tile
	 */
	private void generateField() {
		for(int i = 0; i < W+2; i++) {
			if (i > 0 && i < W+1) {
				grid.add(new ImageView(Global.BORDER_BOTTOM_SIDE){{
					setFitWidth(Global.FRAME_WIDTH/(W+2));
					setFitHeight(Global.FRAME_HEIGHT/(H+2));
				}}, i, 0);
				grid.add(new ImageView(Global.BORDER_BOTTOM_SIDE){{
					setFitWidth(Global.FRAME_WIDTH/(W+2));
					setFitHeight(Global.FRAME_HEIGHT/(H+2));
				}}, i, H+1);
			}
		}
		for(int j = 0; j < H+2; j++) {
			if (j > 0 && j < H+1) {
				grid.add(new ImageView(Global.BORDER_LEFT_SIDE){{
					setFitWidth(Global.FRAME_WIDTH/(W+2));
					setFitHeight(Global.FRAME_HEIGHT/(H+2));
				}}, 0, j);
				grid.add(new ImageView(Global.BORDER_RIGHT_SIDE){{
					setFitWidth(Global.FRAME_WIDTH/(W+2));
					setFitHeight(Global.FRAME_HEIGHT/(H+2));
				}}, W+1, j);
			}
		}
		grid.add(new ImageView(Global.BORDER_LEFT_T){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
			setOnMouseClicked((event) -> {
				if(!firstClick) {
					DEBUG_flagMines();
				}
			});
		}}, 0, 0);
		grid.add(new ImageView(Global.BORDER_RIGHT_T){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, W+1, 0);
		grid.add(new ImageView(Global.BORDER_CORNER_BL){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
			setOnMouseClicked((event) -> {
				if(!firstClick) {
					DEBUG_showMines();
				}
			});
		}}, 0, H+1);
		grid.add(new ImageView(Global.BORDER_CORNER_BR){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
			setOnMouseClicked((event) -> {
				DEBUG_printHighscore();
			});
		}}, W+1, H+1);
		
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = new ImageView(Global.TILE_UNCLICKED);
				tiles[i][j].setUserData(new int[] {i, j});
				state[i][j] = TileState.UNCLICKED;
				grid.add(tiles[i][j], i+1, j+1);
				
				tiles[i][j].setFitWidth(Global.FRAME_WIDTH/(W+2));
				tiles[i][j].setFitHeight(Global.FRAME_HEIGHT/(H+2));
				
				tiles[i][j].setOnMousePressed((event) -> {
					if (firstClick) {
						Debugger.DEBUG_print("Click Event", "User made first click", true);
						Object img = event.getSource();
						if(img instanceof ImageView) {
							int[] cords = (int[]) ((ImageView) img).getUserData();
							Debugger.DEBUG_print("Click Event", "User clicked " + Debugger.DEBUG_getCordsString(cords[0], cords[1]), true);
							generateMines(cords[0], cords[1]);
							updateTile(cords[0], cords[1], Global.TILE_EMPTY, TileState.EMPTY);
							checkSurroundingUnclicked(cords[0], cords[1]);
						}
						firstClick = false;
					} else if(!gameOver) {
						Object img = event.getSource();
						if(img instanceof ImageView) {
							int[] cords = (int[]) ((ImageView) img).getUserData();
							TileState s = state[cords[0]][cords[1]];
							Debugger.DEBUG_print("Click Event", "User " + (event.isPrimaryButtonDown() ? "clicked " : "flaged ") + Debugger.DEBUG_getCordsString(cords[0], cords[1]) + " with state " + s.toString(), true);
							// On left click, reveal tile
							if(event.isPrimaryButtonDown()) {
								if(s.equals(TileState.UNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_EMPTY));
									checkSurroundingUnclicked(cords[0], cords[1]);
									state[cords[0]][cords[1]] = TileState.EMPTY;
								} else if(s.equals(TileState.ONEUNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_ONE));
									state[cords[0]][cords[1]] = TileState.ONE;
								} else if(s.equals(TileState.TWOUNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_TWO));
									state[cords[0]][cords[1]] = TileState.TWO;
								} else if(s.equals(TileState.THREEUNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_THREE));
									state[cords[0]][cords[1]] = TileState.THREE;
								} else if(s.equals(TileState.FOURUNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_FOUR));
									state[cords[0]][cords[1]] = TileState.FOUR;
								} else if(s.equals(TileState.FIVEUNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_FIVE));
									state[cords[0]][cords[1]] = TileState.FIVE;
								} else if(s.equals(TileState.SIXUNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_SIX));
									state[cords[0]][cords[1]] = TileState.SIX;
								} else if(s.equals(TileState.SEVENUNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_SEVEN));
									state[cords[0]][cords[1]] = TileState.SEVEN;
								} else if(s.equals(TileState.EIGHTUNCLICKED)) {
									((ImageView) img).setImage(new Image(Global.TILE_EIGHT));
									state[cords[0]][cords[1]] = TileState.EIGHT;
								} else if(s.equals(TileState.MINE)) {
									Debugger.DEBUG_print("Game Event", "User clicked a mine, game over", true);
									((ImageView) img).setImage(new Image(Global.TILE_MINE));
									info.stopClock();
									gameOver = true;
									for(int x = 0; x < tiles.length; x++) {
										for(int y = 0; y < tiles[0].length; y++) {
											if(state[x][y].equals(TileState.UNCLICKED)) {
												updateTile(x, y, Global.TILE_EMPTY, TileState.EMPTY);
											} else if(state[x][y].equals(TileState.ONEUNCLICKED)) {
												updateTile(x, y, Global.TILE_ONE, TileState.ONE);
											} else if(state[x][y].equals(TileState.TWOUNCLICKED)) {
												updateTile(x, y, Global.TILE_TWO, TileState.TWO);
											} else if(state[x][y].equals(TileState.THREEUNCLICKED)) {
												updateTile(x, y, Global.TILE_THREE, TileState.THREE);
											} else if(state[x][y].equals(TileState.FOURUNCLICKED)) {
												updateTile(x, y, Global.TILE_FOUR, TileState.FOUR);
											} else if(state[x][y].equals(TileState.FIVEUNCLICKED)) {
												updateTile(x, y, Global.TILE_FIVE, TileState.FIVE);
											} else if(state[x][y].equals(TileState.SIXUNCLICKED)) {
												updateTile(x, y, Global.TILE_SIX, TileState.SIX);
											} else if(state[x][y].equals(TileState.SEVENUNCLICKED)) {
												updateTile(x, y, Global.TILE_SEVEN, TileState.SEVEN);
											} else if(state[x][y].equals(TileState.EIGHTUNCLICKED)) {
												updateTile(x, y, Global.TILE_EIGHT, TileState.EIGHT);
											} else if(state[x][y].equals(TileState.MINE)) {
												updateTile(x, y, Global.TILE_MINE, TileState.MINE);
											} else if(state[x][y].equals(TileState.ONEFLAG)) {
												updateTile(x, y, Global.TILE_ONE, TileState.ONE);
											} else if(state[x][y].equals(TileState.TWOFLAG)) {
												updateTile(x, y, Global.TILE_TWO, TileState.TWO);
											} else if(state[x][y].equals(TileState.THREEFLAG)) {
												updateTile(x, y, Global.TILE_THREE, TileState.THREE);
											} else if(state[x][y].equals(TileState.FOURFLAG)) {
												updateTile(x, y, Global.TILE_FOUR, TileState.FOUR);
											} else if(state[x][y].equals(TileState.FIVEFLAG)) {
												updateTile(x, y, Global.TILE_FIVE, TileState.FIVE);
											} else if(state[x][y].equals(TileState.SIXFLAG)) {
												updateTile(x, y, Global.TILE_SIX, TileState.SIX);
											} else if(state[x][y].equals(TileState.SEVENFLAG)) {
												updateTile(x, y, Global.TILE_SEVEN, TileState.SEVEN);
											} else if(state[x][y].equals(TileState.EIGHTFLAG)) {
												updateTile(x, y, Global.TILE_EIGHT, TileState.EIGHT);
											} else if(state[x][y].equals(TileState.FLAG)) {
												updateTile(x, y, Global.TILE_EMPTY, TileState.EMPTY);
											}
										}
									}
								}
							// On right click, place flag and check for win
							} else if(event.isSecondaryButtonDown()) {
								// If tile is not flagged
								if(s.equals(TileState.UNCLICKED) || TileState.isNumberUnclicked(s) || s.equals(TileState.MINE)) {
									if(flagsRemaining > 0) {
										((ImageView) img).setImage(new Image(Global.TILE_FLAG));
										if(s.equals(TileState.ONEUNCLICKED)) {
											state[cords[0]][cords[1]] = TileState.ONEFLAG;
										} else if(s.equals(TileState.TWOUNCLICKED)) {
											state[cords[0]][cords[1]] = TileState.TWOFLAG;
										} else if(s.equals(TileState.THREEUNCLICKED)) {
											state[cords[0]][cords[1]] = TileState.THREEFLAG;
										} else if(s.equals(TileState.FOURUNCLICKED)) {
											state[cords[0]][cords[1]] = TileState.FOURFLAG;
										} else if(s.equals(TileState.FIVEUNCLICKED)) {
											state[cords[0]][cords[1]] = TileState.FIVEFLAG;
										} else if(s.equals(TileState.SIXUNCLICKED)) {
											state[cords[0]][cords[1]] = TileState.SIXFLAG;
										} else if(s.equals(TileState.SEVENUNCLICKED)) {
											state[cords[0]][cords[1]] = TileState.SEVENFLAG;
										} else if(s.equals(TileState.EIGHTUNCLICKED)) {
											state[cords[0]][cords[1]] = TileState.EIGHTFLAG;
										} else if(s.equals(TileState.MINE)) {
											state[cords[0]][cords[1]] = TileState.MINEFLAG;
										} else {
											state[cords[0]][cords[1]] = TileState.FLAG;
										}
										flagsRemaining--;
										Debugger.DEBUG_print("Variable Tracker", "Used flag, " + flagsRemaining + " flags left", true);
										info.updateMines(((flagsRemaining / 1000) % 10), 
												(flagsRemaining / 100) % 10, 
												(flagsRemaining / 10) % 10, 
												flagsRemaining % 10);
										// Checks if the player has won
										if(flagsRemaining == 0) {
											Debugger.DEBUG_print("Game Event", "All flags used, checking for win", true);
											int correctCount = 0;
											for(int x = 0; x < tiles.length; x++) {
												for(int y = 0; y < tiles[0].length; y++) {
													if(state[x][y].equals(TileState.MINEFLAG)) {
														correctCount++;
													}
												}
											}
											if(correctCount == mineCount) {
												Debugger.DEBUG_print("Game Event", "All flags are correct, game won", true);
												gameOver = true;
												info.stopClock();
												int time = info.getTimeInSeconds();
												Debugger.DEBUG_print("Game Event", "hasCheated is " + hasCheated + (hasCheated ? ". High score can't be recorded" : ". High score can be recorded"), true);
												if(!hasCheated && diff != Difficulty.CUSTOM) {
													TextInputDialog usernameDialog = new TextInputDialog("name");
													usernameDialog.setTitle("Leaderboard");
													usernameDialog.setHeaderText("Please enter your name");
													usernameDialog.setContentText("Name:");
																
													Optional<String> usernameResult = usernameDialog.showAndWait();
													usernameResult.ifPresent(name -> {
														Debugger.DEBUG_print("Game Event", "Saving score of " + time + " under alias " + name, true);
														dataManager.write(name, diff.toString(), Integer.toString(time));
													});
												} else if(diff == Difficulty.CUSTOM) {
													Alert alert = new Alert(AlertType.WARNING);
													alert.setTitle("Warning");
													alert.setHeaderText("Since this is a custom game, a high score cannot be recorded");
													alert.showAndWait();
												} else {
													Alert alert = new Alert(AlertType.WARNING);
													alert.setTitle("Warning");
													alert.setHeaderText("Since cheats were used, a high score can't be recorded");
													alert.showAndWait();
												}
												 
											}
										}
									}
								// If tile is already flagged, remove the flag
								} else if(s.equals(TileState.FLAG) || TileState.isNumberFlag(s) || s.equals(TileState.MINEFLAG)) {
									((ImageView) img).setImage(new Image(Global.TILE_UNCLICKED));
									if(s.equals(TileState.ONEFLAG)) {
										state[cords[0]][cords[1]] = TileState.ONEUNCLICKED;
									} else if(s.equals(TileState.TWOFLAG)) {
										state[cords[0]][cords[1]] = TileState.TWOUNCLICKED;
									} else if(s.equals(TileState.THREEFLAG)) {
										state[cords[0]][cords[1]] = TileState.THREEUNCLICKED;
									} else if(s.equals(TileState.FOURFLAG)) {
										state[cords[0]][cords[1]] = TileState.FOURUNCLICKED;
									} else if(s.equals(TileState.FIVEFLAG)) {
										state[cords[0]][cords[1]] = TileState.FIVEUNCLICKED;
									} else if(s.equals(TileState.SIXFLAG)) {
										state[cords[0]][cords[1]] = TileState.SIXUNCLICKED;
									} else if(s.equals(TileState.SEVENFLAG)) {
										state[cords[0]][cords[1]] = TileState.SEVENUNCLICKED;
									} else if(s.equals(TileState.EIGHTFLAG)) {
										state[cords[0]][cords[1]] = TileState.EIGHTUNCLICKED;
									} else if(s.equals(TileState.MINEFLAG)) {
										state[cords[0]][cords[1]] = TileState.MINE;
									} else {
										state[cords[0]][cords[1]] = TileState.UNCLICKED;
									}
									flagsRemaining++;
									Debugger.DEBUG_print("Variable Tracker", "Gained flag, " + flagsRemaining + " flags left", true);
									info.updateMines(((flagsRemaining / 1000) % 10), 
											(flagsRemaining/ 100) % 10, 
											(flagsRemaining / 10) % 10, 
											flagsRemaining % 10);
									//stage.setTitle("Flags remaining: " + flagsRemaining);
								}
							}
						}
					}
				});
			}
		}
	}
	
	/**
	 * After the user makes their first click, generates the mines
	 * This is done after the first click to make sure it won't be a mine
	 * @param x position of the first click
	 * @param y position of the first click
	 */
	private void generateMines(int x, int y) {
		// Region around the first click where no mines can be placed
		ArrayList<int[]> nullRegion = new ArrayList<>();
		for(int i = -(nullRegionSize); i <= nullRegionSize; i++) {
			for(int j = -(nullRegionSize); j <= nullRegionSize; j++) {
				if(i+x >= 0 && i+x < W && j+y >= 0 && j+y < H) {
					nullRegion.add(new int[] {x+i, y+j});
					Debugger.DEBUG_print("Mine Generation", "Added " + Debugger.DEBUG_getCordsString(x+i, y+j) + " to the null region", true);
				}
			}
		}
		int i = 0;
		int j = 0;
		int placedMines = 0;
		// Generates the location of the mines
		while(placedMines < mineCount) {
			if(i > tiles.length-1) {
				i = 0;
				j++;
			}
			if(j > tiles[0].length-1) {
				j = 0;
			}
			if(Math.random() < 0.05) {
				if(!state[i][j].equals(TileState.MINE) && !Global.containsCord(nullRegion, new int[] {i, j})) {
					state[i][j] = TileState.MINE;
					placedMines++;
					Debugger.DEBUG_print("Mine Generation", "Placed mine at " + Debugger.DEBUG_getCordsString(i, j) + " for a total of " + placedMines, true);
				}
			}
			i++;
		}

		// Places the numbers around the mines
		for(i = 0; i < tiles.length; i++) {
			for(j = 0; j < tiles[0].length; j++) {
				int mCount = 0;
				if(!state[i][j].equals(TileState.MINE)) {
					for(int w = -1; w <= 1; w++) {
						for(int h = -1; h <= 1; h++) {
							if((w+i) >= 0 && (h+j) >= 0 && (w+i) < tiles.length && (h+j) < tiles[0].length) {
								if(state[i+w][j+h].equals(TileState.MINE)) {
									mCount++;
								}
							}
						}
					}
					Debugger.DEBUG_print("Mine Generation", "At tile " + Debugger.DEBUG_getCordsString(i, j) + ", there are " + mCount + " surrounding mines", true);
					if(mCount == 1) {
						state[i][j] = TileState.ONEUNCLICKED;
					} else if(mCount == 2) {
						state[i][j] = TileState.TWOUNCLICKED;
					} else if(mCount == 3) {
						state[i][j] = TileState.THREEUNCLICKED;
					} else if(mCount == 4) {
						state[i][j] = TileState.FOURUNCLICKED;
					} else if(mCount == 5) {
						state[i][j] = TileState.FIVEUNCLICKED;
					} else if(mCount == 6) {
						state[i][j] = TileState.SIXUNCLICKED;
					} else if(mCount == 7) {
						state[i][j] = TileState.SEVENUNCLICKED;
					} else if(mCount == 8) {
						state[i][j] = TileState.EIGHTUNCLICKED;
					}
				}
			}
		}
	}
	
	/**
	 * Checks if the surrounding tiles are empty. If so, uncover them
	 * @param x position of the click
	 * @param y position of the click
	 */
	public void checkSurroundingUnclicked(int x, int y) {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(x+i >= 0 && x+i < tiles.length && y+j >= 0 && y+j < tiles[0].length) {
					if(state[x+i][y+j].equals(TileState.UNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_EMPTY, TileState.EMPTY);
						Debugger.DEBUG_print("Uncover Empty", "Found empty tile surrounding " + Debugger.DEBUG_getCordsString(x, y) + " at " +
						Debugger.DEBUG_getCordsString(i, j), true);
						checkSurroundingUnclicked(x+i,y+j);
					} else if(state[x+i][y+j].equals(TileState.ONEUNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_ONE, TileState.ONE);
					} else if(state[x+i][y+j].equals(TileState.TWOUNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_TWO, TileState.TWO);
					} else if(state[x+i][y+j].equals(TileState.THREEUNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_THREE, TileState.THREE);
					} else if(state[x+i][y+j].equals(TileState.FOURUNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_FOUR, TileState.FOUR);
					} else if(state[x+i][y+j].equals(TileState.FIVEUNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_FIVE, TileState.FIVE);
					} else if(state[x+i][y+j].equals(TileState.SIXUNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_SIX, TileState.SIX);
					} else if(state[x+i][y+j].equals(TileState.SEVENUNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_SEVEN, TileState.SEVEN);
					} else if(state[x+i][y+j].equals(TileState.EIGHTUNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_EIGHT, TileState.EIGHT);
					}
				}
			}
		}
	}
	
	/**
	 * Updates a tile at (x,y) with img and state
	 * @param x
	 * @param y
	 * @param img
	 * @param state
	 */
	public void updateTile(int x, int y, String img, TileState state) {
		Debugger.DEBUG_print("Update Tile Event", "Updating tile at " + Debugger.DEBUG_getCordsString(x, y) + " to " + state.toString(), true);
		this.state[x][y] = state;
		tiles[x][y].setImage(new Image(img));
	}
	
	/**
	 * Reveals the location of the mines
	 */
	public void DEBUG_showMines() {
		if(Global.CHEATS_DISABLE_SCORE) {
			hasCheated = true;
		}
		Debugger.DEBUG_print("Cheat Event", "Show mines activated", true);
		if(Global.DEBUG_MODE) {
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[0].length; j++) {
					if(state[i][j].equals(TileState.UNCLICKED)) {
						updateTile(i, j, Global.TILE_EMPTY, TileState.EMPTY);
					} else if(state[i][j].equals(TileState.ONEUNCLICKED)) {
						updateTile(i, j, Global.TILE_ONE, TileState.ONE);
					} else if(state[i][j].equals(TileState.TWOUNCLICKED)) {
						updateTile(i, j, Global.TILE_TWO, TileState.TWO);
					} else if(state[i][j].equals(TileState.THREEUNCLICKED)) {
						updateTile(i, j, Global.TILE_THREE, TileState.THREE);
					} else if(state[i][j].equals(TileState.FOURUNCLICKED)) {
						updateTile(i, j, Global.TILE_FOUR, TileState.FOUR);
					} else if(state[i][j].equals(TileState.FIVEUNCLICKED)) {
						updateTile(i, j, Global.TILE_FIVE, TileState.FIVE);
					} else if(state[i][j].equals(TileState.SIXUNCLICKED)) {
						updateTile(i, j, Global.TILE_SIX, TileState.SIX);
					} else if(state[i][j].equals(TileState.SEVENUNCLICKED)) {
						updateTile(i, j, Global.TILE_SEVEN, TileState.SEVEN);
					} else if(state[i][j].equals(TileState.EIGHTUNCLICKED)) {
						updateTile(i, j, Global.TILE_EIGHT, TileState.EIGHT);
					}
				}
			}
		}
	}
	
	/**
	 * Flags each mine
	 */
	public void DEBUG_flagMines() {
		if(Global.CHEATS_DISABLE_SCORE) {
			hasCheated = true;
		}
		Debugger.DEBUG_print("Cheat Event", "Flag mines activated", true);
		if(Global.DEBUG_MODE) {
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[0].length; j++) {
					if(state[i][j].equals(TileState.MINE)) {
						updateTile(i, j, Global.TILE_FLAG, TileState.MINEFLAG);
						flagsRemaining--;
						info.updateMines(((flagsRemaining / 1000) % 10), 
								(flagsRemaining / 100) % 10, 
								(flagsRemaining / 10) % 10, 
								flagsRemaining % 10);
					}
				}
			}
		}
	}
	
	public void DEBUG_printHighscore() {
		if(Global.DEBUG_MODE) {
			dataManager.readToObservableList();
			dataManager.DEBUG_printMap();
		}
	}
}
