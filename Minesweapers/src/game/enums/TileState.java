package game.enums;

public enum TileState {
	UNCLICKED, EMPTY, FLAG, MINE, 
	ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, 
	ONEUNCLICKED, TWOUNCLICKED, THREEUNCLICKED, FOURUNCLICKED, FIVEUNCLICKED, SIXUNCLICKED, SEVENUNCLICKED, EIGHTUNCLICKED,
	ONEFLAG, TWOFLAG, THREEFLAG, FOURFLAG, FIVEFLAG, SIXFLAG, SEVENFLAG, EIGHTFLAG,
	MINEFLAG;
	
	public static boolean isNumberUnclicked(TileState s) {
		return (s.equals(ONEUNCLICKED) || s.equals(TWOUNCLICKED) || s.equals(THREEUNCLICKED) ||
				s.equals(FOURUNCLICKED) || s.equals(FIVEUNCLICKED) || s.equals(SIXUNCLICKED) ||
				s.equals(SEVENUNCLICKED) || s.equals(EIGHTUNCLICKED));
	}
	
	public static boolean isNumberFlag(TileState s) {
		return (s.equals(ONEFLAG) || s.equals(TWOFLAG) || s.equals(THREEFLAG) ||
				s.equals(FOURFLAG) || s.equals(FIVEFLAG) || s.equals(SIXFLAG) ||
				s.equals(SEVENFLAG) || s.equals(EIGHTFLAG));
	}
}
