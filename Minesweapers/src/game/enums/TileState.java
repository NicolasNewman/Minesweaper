package game.enums;

/**
 * Contains each possible state a tile can be in
 * @author QuantumPie
 *
 */
public enum TileState {
	UNCLICKED, EMPTY, FLAG, MINE, 
	ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, 
	ONEUNCLICKED, TWOUNCLICKED, THREEUNCLICKED, FOURUNCLICKED, FIVEUNCLICKED, SIXUNCLICKED, SEVENUNCLICKED, EIGHTUNCLICKED,
	ONEFLAG, TWOFLAG, THREEFLAG, FOURFLAG, FIVEFLAG, SIXFLAG, SEVENFLAG, EIGHTFLAG,
	MINEFLAG;
	
	/**
	 * Returns true if the given tile is a number and is unclicked
	 * @param s state of the tile to verify
	 * @return weather or not the tile is a number and unclicked
	 */
	public static boolean isNumberUnclicked(TileState s) {
		return (s.equals(ONEUNCLICKED) || s.equals(TWOUNCLICKED) || s.equals(THREEUNCLICKED) ||
				s.equals(FOURUNCLICKED) || s.equals(FIVEUNCLICKED) || s.equals(SIXUNCLICKED) ||
				s.equals(SEVENUNCLICKED) || s.equals(EIGHTUNCLICKED));
	}
	
	/**
	 * Returns true if a tile is a number and a flag
	 * @param s state of the tile to verify
	 * @return weather or not the tile is a number and a flag
	 */
	public static boolean isNumberFlag(TileState s) {
		return (s.equals(ONEFLAG) || s.equals(TWOFLAG) || s.equals(THREEFLAG) ||
				s.equals(FOURFLAG) || s.equals(FIVEFLAG) || s.equals(SIXFLAG) ||
				s.equals(SEVENFLAG) || s.equals(EIGHTFLAG));
	}
}
