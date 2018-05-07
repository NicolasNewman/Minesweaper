package game;

import java.util.HashMap;

public class Global {
	public static int FRAME_WIDTH; // 1770
	public static int FRAME_HEIGHT; // 885
	public static int TILE_WIDTH;
	public static int TILE_HEIGHT;
	
	public final static String TILE_ONE = "res/tile_one.jpg";
	public final static String TILE_TWO = "res/tile_two.jpg";
	public final static String TILE_THREE = "res/tile_three.jpg";
	public final static String TILE_FOUR = "res/tile_four.jpg";
	public final static String TILE_FIVE = "res/tile_five.jpg";
	public final static String TILE_SIX = "res/tile_six.jpg";
	public final static String TILE_SEVEN = "res/tile_seven.jpg";
	public final static String TILE_EIGHT = "res/tile_eight.jpg";
	
	public final static String TILE_MINE = "res/tile_mine.jpg";
	public final static String TILE_FLAG = "res/tile_flag.jpg";
	public final static String TILE_UNCLICKED = "res/tile_unclicked.jpg";
	public final static String TILE_EMPTY = "res/tile_empty.jpg";
	
	
	public final static String BORDER_HUD = "res/border/border_hud.jpg";
	
	public final static String BORDER_BOTTOM_SIDE = "res/border/border_bottom_side.jpg";
	public final static String BORDER_TOP_SIDE = "res/border/border_top_side.jpg";
	
	public final static String BORDER_LEFT_SIDE = "res/border/border_left_side.jpg";
	public final static String BORDER_LEFT_T = "res/border/border_left_T.jpg";
	
	public final static String BORDER_RIGHT_SIDE = "res/border/border_right_side.jpg";
	public final static String BORDER_RIGHT_T = "res/border/border_right_T.jpg";
	
	public final static String BORDER_CORNER_BL = "res/border/border_corner_BL.jpg";
	public final static String BORDER_CORNER_BR = "res/border/border_corner_BR.jpg";
	public final static String BORDER_CORNER_TL = "res/border/border_corner_TL.jpg";
	public final static String BORDER_CORNER_TR = "res/border/border_corner_TR.jpg";
	
	public static HashMap<Integer, String> number = new HashMap<>();
	public final static String NUMBER_ZERO = "res/number/number_0.jpg";
	public final static String NUMBER_ONE = "res/number/number_1.jpg";
	public final static String NUMBER_TWO = "res/number/number_2.jpg";
	public final static String NUMBER_THREE = "res/number/number_3.jpg";
	public final static String NUMBER_FOUR = "res/number/number_4.jpg";
	public final static String NUMBER_FIVE = "res/number/number_5.jpg";
	public final static String NUMBER_SIX = "res/number/number_6.jpg";
	public final static String NUMBER_SEVEN = "res/number/number_7.jpg";
	public final static String NUMBER_EIGHT = "res/number/number_8.jpg";
	public final static String NUMBER_NINE = "res/number/number_9.jpg";
	
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
	
	//public final static int MAX_WIDTH = 320, MIN_WIDTH = 400; //1000 500
	//public final static int MAX_HEIGHT = 320, MIN_HEIGHT = 400;
}
