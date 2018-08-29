/**
 * @author Joshua
 * @author Daniel
 * @author Alan
 * Piece class responsible for keeping a single piece's variables together, such as alive status, color, kingship,
 * and location.
 */

public class Piece {
	
	private boolean alive;
	private boolean red;
	private boolean king;
	private int locationNum;

	/**
	 * Initializes the Piece class
	 */	
	public Piece(boolean alive, boolean red, boolean king, int locationNum) {	
		this.alive = alive;
		this.red = red;
		this.king = king;
		this.locationNum = locationNum;
	}
	
	/**
	 * Method creates a deep copy of a piece
	 * @return new Piece object with the same variable values as the copied piece
	 */
	public Piece copyPiece() {
		return(new Piece(this.isAlive(), this.isRed(), this.isKing(), this.getLocationNum()));
	}

	/**
	 * Method returns a piece's alive status
	 * @return alive (if not alive/dead, returns false)
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * Method returns a piece's color
	 * @return red (if not red/black, returns false)
	 */
	public boolean isRed() {
		return red;
	}
	
	/**
	 * Method returns a piece's king status
	 * @return king (if not king, returns false)
	 */
	public boolean isKing() {
		return king;
	}
	
	/**
	 * Method sets the status of a king; used in the Board checkForKing method to turn non-kings into kings
	 * @param king
	 */
	public void setKing(boolean king) {
		this.king = king;
	}
	
	/**
	 * Method returns a piece's location
	 * @return locationNum
	 */
	public int getLocationNum() {
		return locationNum;
	}
	
	/**
	 * Method sets the piece's location; used in creating deep copies of pieces
	 * @param locationNum
	 */
	public void setLocationNum(int locationNum) {
		this.locationNum = locationNum;
	}
}
