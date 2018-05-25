package game.helper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Global {
	public static int FRAME_WIDTH;
	public static int FRAME_HEIGHT;
	public static int TILE_WIDTH;
	public static int TILE_HEIGHT;
	
	public static final String version = "1.1.2";
	
	public static boolean DEBUG_MODE = false;
	public static boolean cheatsDisableScore = false;
	public static final boolean SECURE_DATA = true;
	
	public static String bootTime;
	public static DateTimeFormatter fileFormat = DateTimeFormatter.ofPattern("MM_dd_yyyy-HH_mm_ss");
	public static DateTimeFormatter logFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public final static String GAME_PATH = getOSPath();
	public final static String GAME_DATA_PATH = GAME_PATH + File.separator + "data" + File.separator;
	public final static String GAME_LOG_PATH = GAME_PATH + File.separator + "logs" + File.separator;

	public final static String DATA_PATH = GAME_DATA_PATH + "data.dat";
	public final static String IV_PATH = GAME_DATA_PATH + "iv.dat";
	public final static String KEY_PATH = GAME_DATA_PATH + "key.dat";
	public static String LOG_PATH = GAME_LOG_PATH;
	
	public final static String TILE_ONE = "file:resources/images/tile/tile_one.jpg";
	public final static String TILE_TWO = "file:resources/images/tile/tile_two.jpg";
	public final static String TILE_THREE = "file:resources/images/tile/tile_three.jpg";
	public final static String TILE_FOUR = "file:resources/images/tile/tile_four.jpg";
	public final static String TILE_FIVE = "file:resources/images/tile/tile_five.jpg";
	public final static String TILE_SIX = "file:resources/images/tile/tile_six.jpg";
	public final static String TILE_SEVEN = "file:resources/images/tile/tile_seven.jpg";
	public final static String TILE_EIGHT = "file:resources/images/tile/tile_eight.jpg";
	
	public final static String TILE_MINE = "file:resources/images/tile/tile_mine.jpg";
	public final static String TILE_FLAG = "file:resources/images/tile/tile_flag.jpg";
	public final static String TILE_UNCLICKED = "file:resources/images/tile/tile_unclicked.jpg";
	public final static String TILE_EMPTY = "file:resources/images/tile/tile_empty.jpg";
	
	
	public final static String BORDER_HUD = "file:resources/images/border/border_hud.jpg";
	
	public final static String BORDER_BOTTOM_SIDE = "file:resources/images/border/border_bottom_side.jpg";
	public final static String BORDER_TOP_SIDE = "file:resources/images/border/border_top_side.jpg";
	
	public final static String BORDER_LEFT_SIDE = "file:resources/images/border/border_left_side.jpg";
	public final static String BORDER_LEFT_T = "file:resources/images/border/border_left_T.jpg";
	
	public final static String BORDER_RIGHT_SIDE = "file:resources/images/border/border_right_side.jpg";
	public final static String BORDER_RIGHT_T = "file:resources/images/border/border_right_T.jpg";
	
	public final static String BORDER_CORNER_BL = "file:resources/images/border/border_corner_BL.jpg";
	public final static String BORDER_CORNER_BR = "file:resources/images/border/border_corner_BR.jpg";
	public final static String BORDER_CORNER_TL = "file:resources/images/border/border_corner_TL.jpg";
	public final static String BORDER_CORNER_TR = "file:resources/images/border/border_corner_TR.jpg";
	
	public static HashMap<Integer, String> number = new HashMap<>();
	public final static String NUMBER_ZERO = "file:resources/images/number/number_0.jpg";
	public final static String NUMBER_ONE = "file:resources/images/number/number_1.jpg";
	public final static String NUMBER_TWO = "file:resources/images/number/number_2.jpg";
	public final static String NUMBER_THREE = "file:resources/images/number/number_3.jpg";
	public final static String NUMBER_FOUR = "file:resources/images/number/number_4.jpg";
	public final static String NUMBER_FIVE = "file:resources/images/number/number_5.jpg";
	public final static String NUMBER_SIX = "file:resources/images/number/number_6.jpg";
	public final static String NUMBER_SEVEN = "file:resources/images/number/number_7.jpg";
	public final static String NUMBER_EIGHT = "file:resources/images/number/number_8.jpg";
	public final static String NUMBER_NINE = "file:resources/images/number/number_9.jpg";
	
	/**
	 * Loads the hashmap containing the int number mapped to the number image path
	 */
	public static void loadNumbers() {
		number.put(0, NUMBER_ZERO);
		number.put(1, NUMBER_ONE);
		number.put(2, NUMBER_TWO);
		number.put(3, NUMBER_THREE);
		number.put(4, NUMBER_FOUR);
		number.put(5, NUMBER_FIVE);
		number.put(6, NUMBER_SIX);
		number.put(7, NUMBER_SEVEN);
		number.put(8, NUMBER_EIGHT);
		number.put(9, NUMBER_NINE);
	}
	
	/**
	 * Gets the current time. Used for logs and the in-game clock
	 * @return
	 */
	public static LocalDateTime getNow() {
		return LocalDateTime.now();
	}
	
	/**
	 * Checks if an int array is contained in an arraylist of intarrays
	 * @param list list of int arrays. Each int array must have a length of 2
	 * @param cords cords to check. Must have a length of two
	 * @return true if cords is contained in list
	 */
	public static boolean containsCord(ArrayList<int[]> list, int[] cords) {
		if(cords.length == 2) {
			for(int[] cord : list) {
				if(cord.length != 2) {
					return false;
				}
				if(cord[0] == cords[0] && cord[1] == cords[1]) {
					return true;
				}
				
			}
		}
		return false;
	}
	
	/**
	 * Gets the game data path based on which OS the user is on
	 * @return correct path for the game data
	 */
	public static String getOSPath() {
		String OS = System.getProperty("os.name");
		if(OS.toLowerCase().indexOf("win") >= 0) {
			return System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "Minesweeper_QP";
		} else if(OS.toLowerCase().indexOf("mac") >= 0) {
			return System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "Minesweeper_QP";
		}
		return null;
	}
}
