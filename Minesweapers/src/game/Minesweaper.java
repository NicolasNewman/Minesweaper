package game;

import java.util.ArrayList;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Minesweaper {
	
	private Parent root;
	private Stage stage;
	private VBox vbox = new VBox();
	private GridPane grid = new GridPane();
	private GameInfo info;
	private ImageView[][] tiles;
	private TileState[][] state;
	
	private int H = 10;
	private int W = 10;
	private int mineCount = 10;
	private int flagsRemaining = 10;
	private double tempMineCount;
	private boolean gameOver = false;
	private boolean firstClick = true;
	
	public Minesweaper(int W, int H, int tempMineCount) {
		this.W = W;
		this.H = H;
		this.tempMineCount = tempMineCount;
		createWindow();
	}
	
	private void createWindow() {
		stage = new Stage();
		stage.setTitle("Minesweaper");
		
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
				if(i*W <= 800) {
					Global.FRAME_WIDTH = i*(W+2);
					Global.FRAME_HEIGHT = i*(H+2);
					Global.TILE_HEIGHT = i;
					Global.TILE_WIDTH = i;
					break;
				}
			}
		}
		
		for(int i = 0; i < W+2; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(100/(W+2));
			grid.getColumnConstraints().add(column);
		}
		for(int i = 0; i < H+2; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100/(H+2));
			grid.getRowConstraints().add(row);
		}
		grid.setPrefSize(Global.FRAME_WIDTH, Global.FRAME_HEIGHT);
		grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		
		info = new GameInfo(W, H);
		
		info.updateMines((mineCount / 100) % 10, 
				(mineCount / 10) % 10, 
				mineCount % 10);
		
		vbox.getChildren().add(info.getGrid());
		vbox.getChildren().add(grid);
		Scene scene = new Scene(vbox, Global.FRAME_WIDTH, Global.FRAME_HEIGHT+(Global.TILE_HEIGHT*3));
		
		generateField();
		//generateMines();
		
		//grid.add(info, 0, 0);

		stage.setScene(scene);
		stage.show();
	}
	
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
		}}, 0, 0);
		grid.add(new ImageView(Global.BORDER_RIGHT_T){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, W+1, 0);
		grid.add(new ImageView(Global.BORDER_CORNER_BL){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, 0, H+1);
		grid.add(new ImageView(Global.BORDER_CORNER_BR){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
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
						Object img = event.getSource();
						if(img instanceof ImageView) {
							int[] cords = (int[]) ((ImageView) img).getUserData();
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
									((ImageView) img).setImage(new Image(Global.TILE_MINE));
									stage.setTitle("You lost");
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
							} else if(event.isSecondaryButtonDown()) {
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
										info.updateMines((flagsRemaining/ 100) % 10, 
												(flagsRemaining / 10) % 10, 
												flagsRemaining % 10);
										stage.setTitle("Flags remaining: " + flagsRemaining);
										if(flagsRemaining == 0) {
											int correctCount = 0;
											for(int x = 0; x < tiles.length; x++) {
												for(int y = 0; y < tiles[0].length; y++) {
													if(state[x][y].equals(TileState.MINEFLAG)) {
														correctCount++;
													}
												}
											}
											if(correctCount == mineCount) {
												gameOver = true;
												stage.setTitle("You win!");
												info.stopClock();
											}
										}
									}
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
									info.updateMines((flagsRemaining/ 100) % 10, 
											(flagsRemaining / 10) % 10, 
											flagsRemaining % 10);
									stage.setTitle("Flags remaining: " + flagsRemaining);
								}
							}
						}
					}
				});
			}
		}
	}
	
	private void generateMines(int x, int y) {
		ArrayList<int[]> nullRegion = new ArrayList<>();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(i >= 0 && i < W && j >= 0 && j < H) {
					printCords(i,j);
					nullRegion.add(new int[] {i, j});
				}
			}
		}
		int i = 0;
		int j = 0;
		int placedMines = 0;
		while(placedMines < mineCount) {
			if(i > tiles.length-1) {
				i = 0;
				j++;
			}
			if(j > tiles[0].length-1) {
				j = 0;
			}
			if(Math.random() < 0.05) {
				if(!state[i][j].equals(TileState.MINE) && !nullRegion.contains(new int[] {i, j})) {
					state[i][j] = TileState.MINE;
					placedMines++;
				}
			}
			i++;
		}

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
	
	/*private void uncoverTiles(int startX, int startY) {
		int uncoverAmount = (int) ((H*W)*0.1);
		int r = 0;
		while(uncoverAmount > 0) {
			//System.out.println(uncoverAmount);
			for(int i = -1-r; i <= 1+r; i++) {
				for(int j = -1-r; j < 1+r; j++) {
					if(startX+i >= 0 && startX+i < tiles.length && startY+j >= 0 && startY+j < tiles[0].length) {
						//printCords(startX+i, startY+j);
						if(state[startX+i][startY+j].equals(TileState.UNCLICKED)) {
							tiles[startX+i][startY+j].setImage(new Image(Global.TILE_EMPTY));
							uncoverAmount--;
						} else if(state[startX+i][startY+j].equals(TileState.ONEUNCLICKED)) {
							state[startX+i][startY+j]= TileState.ONE;
							tiles[startX+i][startY+j].setImage(new Image(Global.TILE_ONE));
							uncoverAmount--;
						} else if(state[startX+i][startY+j].equals(TileState.TWOUNCLICKED)) {
							state[startX+i][startY+j]= TileState.TWO;
							tiles[startX+i][startY+j].setImage(new Image(Global.TILE_TWO));
							uncoverAmount--;
						} else if(state[startX+i][startY+j].equals(TileState.THREEUNCLICKED)) {
							state[startX+i][startY+j]= TileState.THREE;
							tiles[startX+i][startY+j].setImage(new Image(Global.TILE_THREE));
							uncoverAmount--;
						}
					}
				}
			}
			r++;
		}
	}*/
	
	public void checkSurroundingUnclicked(int x, int y) {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(x+i >= 0 && x+i < tiles.length && y+j >= 0 && y+j < tiles[0].length) {
					if(state[x+i][y+j].equals(TileState.UNCLICKED) && (Math.abs(i) != Math.abs(j))) {
						updateTile(x+i, y+j, Global.TILE_EMPTY, TileState.EMPTY);
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
	
	public void printCords(int x, int y) {
		System.out.println("(" + x + "," + y + ")");
	}
	
	public void updateTile(int x, int y, String img, TileState state) {
		this.state[x][y] = state;
		tiles[x][y].setImage(new Image(img));
	}

}
