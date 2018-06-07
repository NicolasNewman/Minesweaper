package game.windows;

import game.helper.Global;
import game.helper.Timer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Element that contains the HUD information (flags remaining and clock)
 * @author QuantumPie
 *
 */
public class GameInfo extends VBox {

	private boolean clockStarted = false;
	private int time = 0;
	private GridPane grid = new GridPane();
	private int W, H;
	private Thread t;
	
	public GameInfo(int W, int H) {
		this.W = W;
		this.H = H;
		
		generateGrid();
		updateTime(0, 0, 0, 0);
		updateMines(0, 0, 0, 0);
		startClock();
		
	}
	
	public GridPane getGrid() {
		return grid;
	}
	
	/**
	 * Updates the HUD with the number of flags remaining
	 * @param th thousands place of flags remaining
	 * @param h hundreds place of the flags remaining
	 * @param t tens place of the flags remaining
	 * @param o ones place of the flags remaining
	 */
	public void updateMines(int th, int h, int t, int o) {
		String thS = Global.number.get(th);
		String hS = Global.number.get(h);
		String tS = Global.number.get(t);
		String oS = Global.number.get(o);
		
		grid.add(new ImageView(thS){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
		}}, W-3, 1, 1, 2);
		grid.add(new ImageView(hS){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
		}}, W-2, 1, 1, 2);
		grid.add(new ImageView(tS){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
		}}, W-1, 1, 1, 2);
		grid.add(new ImageView(oS){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
		}}, W, 1, 1, 2);
	}
	
	/**
	 * Updates the HUD with the time the game has been running for
	 * @param th thousands place of the time
	 * @param h hundreds place of the time
	 * @param t tens place of the time
	 * @param o ones palce of the time
	 */
	public void updateTime(int th, int h, int t, int o) {
		String thS = Global.number.get(th);
		String hS = Global.number.get(h);
		String tS = Global.number.get(t);
		String oS = Global.number.get(o);
		
		grid.add(new ImageView(thS){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
		}}, 1, 1, 1, 2);
		grid.add(new ImageView(hS){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
		}}, 2, 1, 1, 2);
		grid.add(new ImageView(tS){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
		}}, 3, 1, 1, 2);
		grid.add(new ImageView(oS){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
		}}, 4, 1, 1, 2);
	}
	
	/**
	 * Starts counting the time the game has been running for and updates the HUD
	 */
	public void startClock() {
		if(!clockStarted) {
			clockStarted = true;

			Task<Void> t = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Timer t = new Timer();
					while(clockStarted) {
						Thread.sleep(1000);
						time = (int) t.getTimeInSeconds();
						
						String thS = Global.number.get((time / 1000) % 10);
						String hS = Global.number.get((time / 100) % 10);
						String tS = Global.number.get((time / 10) % 10);
						String oS = Global.number.get(time % 10);
						
						
						Platform.runLater(() -> grid.add(new ImageView(thS){{
							setFitWidth(Global.FRAME_WIDTH/(W+2));
							setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
						}}, 1, 1, 1, 2));
						Platform.runLater(() -> grid.add(new ImageView(hS){{
							setFitWidth(Global.FRAME_WIDTH/(W+2));
							setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
						}}, 2, 1, 1, 2));
						Platform.runLater(() -> grid.add(new ImageView(tS){{
							setFitWidth(Global.FRAME_WIDTH/(W+2));
							setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
						}}, 3, 1, 1, 2));
						Platform.runLater(() -> grid.add(new ImageView(oS){{
							setFitWidth(Global.FRAME_WIDTH/(W+2));
							setFitHeight(Global.FRAME_HEIGHT/(H+2)*2);
						}}, 4, 1, 1, 2));
					}
					return null;
				}
			};
			this.t = new Thread(t);
			this.t.start();
		}
	}
	
	public int getTimeInSeconds() {
		return time;
	}
	
	public void stopClock() {
		clockStarted = false;
	}
	
	/**
	 * Generates the tiles in the grid for the HUD
	 */
	private void generateGrid() {
//		for(int i = 0; i < W; i++) {
//			ColumnConstraints column = new ColumnConstraints();
//			column.setPercentWidth(100.0/(W+2));
//			grid.getColumnConstraints().add(column);
//		}
		
		grid.setPrefSize(Global.FRAME_WIDTH, (Global.FRAME_HEIGHT/(H+2))*3);
		grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		
		for(int i = 1; i < (W+1); i++) {
			grid.add(new ImageView(Global.BORDER_TOP_SIDE){{
				setFitWidth(Global.FRAME_WIDTH/(W+2));
				setFitHeight(Global.FRAME_HEIGHT/(H+2));
			}}, i, 0);
			grid.add(new ImageView(Global.BORDER_HUD){{
				setFitWidth(Global.FRAME_WIDTH/(W+2));
				setFitHeight(Global.FRAME_HEIGHT/(H+2));
			}}, i, 1);
			grid.add(new ImageView(Global.BORDER_HUD){{
				setFitWidth(Global.FRAME_WIDTH/(W+2));
				setFitHeight(Global.FRAME_HEIGHT/(H+2));
			}}, i, 2);
		}
		grid.add(new ImageView(Global.BORDER_CORNER_TL){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, 0, 0);
		grid.add(new ImageView(Global.BORDER_CORNER_TR){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, W+1, 0);
		grid.add(new ImageView(Global.BORDER_LEFT_SIDE){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, 0, 1);
		grid.add(new ImageView(Global.BORDER_LEFT_SIDE){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, 0, 2);
		grid.add(new ImageView(Global.BORDER_RIGHT_SIDE){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, W+1, 1);
		grid.add(new ImageView(Global.BORDER_RIGHT_SIDE){{
			setFitWidth(Global.FRAME_WIDTH/(W+2));
			setFitHeight(Global.FRAME_HEIGHT/(H+2));
		}}, W+1, 2);
	}
}
