/**
 /**
 * @author Joshua
 * @author Daniel
 * @author Alan
 * The Board class is responsible for keeping track of the location of every piece on the board,
 * determining if a move is valid, and carrying out any moving and capturing functions.
 */

import java.util.ArrayList;

public class Board {

	private ArrayList<Piece> pieces;

	private final int NUM_ROWS = 8;
	private final int NUM_COLS = 8;

	private final int DOWN_MOVE_RIGHT = 7;
	private final int DOWN_MOVE_LEFT = 9;
	private final int UP_MOVE_RIGHT = -9;
	private final int UP_MOVE_LEFT = -7;

	private int arrayListStore;
	private int captureLocationStore;
	private int shiftStore;

	/**
	 * Initializes pieces, the ArrayList that stores all pieces on the board, dead or alive.
	 * Creates all 64 (NUM_ROWS * NUM_COLS) pieces as necessary and inserts them into the pieces ArrayList.
	 */
	public Board() {

		pieces = new ArrayList<>();

		for (int i = 0; i < (NUM_ROWS * NUM_COLS); i++) {

			if (i < 8) { // Creates top row of pieces, start of red pieces
				if (i % 2 == 0) { // Add alive red piece to even squares
					pieces.add(new Piece(true, true, false, i));
				}
				else { // Add dead red piece (color doesn't matter for dead pieces)
					pieces.add(new Piece(false, true, false, i));
				}
			}

			else if (i < 16) { // Creates second row
				if (i % 2 == 1) { // Add alive red piece to odd squares
					pieces.add(new Piece(true, true, false, i));
				}
				else { // Add dead piece
					pieces.add(new Piece(false, true, false, i));
				}
			}

			else if (i < 24) { // Creates third row
				if (i % 2 == 0) { // Add alive red piece to even squares
					pieces.add(new Piece(true, true, false, i));
				}
				else { // Add dead piece
					pieces.add(new Piece(false, true, false, i));
				}
			}

			else if (i < 40) { // Creates fourth and fifth rows of empty squares
				pieces.add(new Piece(false, true, false, i)); // Add dead piece
			}

			else if (i < 48) { // Creates sixth row, start of black pieces
				if (i % 2 == 1) { // Add alive black piece to odd squares
					pieces.add(new Piece(true, false,false,i));
				}
				else { // Add dead piece
					pieces.add(new Piece(false, true, false, i));
				}
			}

			else if (i < 56) { // Creates seventh row
				if (i % 2 == 0) { // Add alive black piece to even squares
					pieces.add(new Piece(true, false,false,i));
				}
				else { // Add dead piece
					pieces.add(new Piece(false, true, false, i));
				}
			}

			else if (i < 64) { // Creates eighth and final row
				if (i % 2 == 1) { // Add alive black piece to odd squares
					pieces.add(new Piece(true, false,false,i));
				}
				else { // Add dead piece
					pieces.add(new Piece(false, true, false, i));
				}
			}
		}
	}


	/**
	 * Creates a deep copy of the board by copying every piece in the pieces ArrayList.
	 * @return deep copy of the board
	 */
	public Board copyBoard() {

		Board temp = new Board();

		temp.pieces.clear();

		for(int i = 0; i < (NUM_ROWS * NUM_COLS); i++) {
			temp.pieces.add(this.pieces.get(i).copyPiece());
		}

		return temp;
	}


	/**
	 * Gives back the piece at a specific location in the pieces ArrayList
	 * @param location
	 * @return Piece at pieces index location
	 */
	public Piece getPiece(int location) {
		return pieces.get(location);
	}


	/**
	 * Runs through the pieces ArrayList and checks the location of every piece. If a piece of the appropriate color
	 * is at the opposite end of the board, the piece's status is changed to be a king, giving it forwards and
	 * backwards movement.
	 */
	public void checkForKing() {

		// Checks to see if any red pieces are on the far black row
		for (int i = 0; i < 8; i++) {

			int locationCheck = (i % 8) + 8 * (7 - (i / 8)); 
			// Function required to translate board location to one-dimensional ArrayList

			// If a piece fits the requirements, changes it to a king
			for (int j = 0; j < pieces.size(); j++) {

				if (pieces.get(j).getLocationNum() == locationCheck
						&& pieces.get(j).isRed()) {

					pieces.get(j).setKing(true);
					break;

				}
			}
		}

		// Repeats the above process, but for black pieces and the far red row
		for (int i = 56; i < 64; i++) {

			int locationCheck = (i % 8) + 8 * (7 - (i / 8));

			for (int j = 0; j < pieces.size(); j++) {

				if (pieces.get(j).getLocationNum() == locationCheck
						&& !pieces.get(j).isRed()) {

					pieces.get(j).setKing(true);
					break;

				}
			}
		}
	}


	/**
	 * Method responsible for moving a piece to a new location.
	 * @param originalLocation
	 * @param movementLocation
	 */
	public void move(int originalLocation, int movementLocation) {

		// Functions required to translate piece's original location and movement location to one-dimensional ArrayList
		int adjOriginalLocation = (originalLocation % 8) + 8 * (7 - (originalLocation / 8));
		int adjMovementLocation = (movementLocation % 8) + 8 * (7 - (movementLocation / 8));

		// Runs through the pieces ArrayList to find the appropriate piece
		for (int i = 0; i < pieces.size(); i++) {

			// Checks for the piece that is to be moved
			if (pieces.get(i).getLocationNum() == adjOriginalLocation) {

				// Deep copies the piece
				Piece e = pieces.get(i).copyPiece();
				e.setLocationNum(adjMovementLocation);

				// If the original piece location is less than/lower on the board than the movement location
				if (adjOriginalLocation < adjMovementLocation) { // Removal and addition of pieces must be in a certain order

					pieces.add(i, new Piece(false, true, false, adjOriginalLocation));
					pieces.remove(i + 1);
					pieces.add(adjMovementLocation, e);
					pieces.remove(adjMovementLocation + 1);

					break;

				} else {

					pieces.add(adjMovementLocation, e);
					pieces.remove(adjMovementLocation + 1);
					pieces.add(i, new Piece(false, true, false, adjOriginalLocation));
					pieces.remove(i + 1);

					break;

				}
			}
		}
	}


	/**
	 * Method responsible for recording the capture of a piece. A captured piece is replaced with a dead piece
	 * at its exact location, and is removed from the pieces ArrayList.
	 */
	public void capture() {

		if (arrayListStore > -1) { // Prevents any capturing from occurring on the first move

			// Creates a dead piece and removes the captured piece.
			pieces.add(arrayListStore, new Piece(false, true, false, captureLocationStore - shiftStore));
			pieces.remove(arrayListStore + 1);

			// Resets the variables
			arrayListStore = 0;
			captureLocationStore = 0;
			shiftStore = 0;
		}

	}


	/**
	 * Checks to see if a desired move is possible or not through a series of if-else checks.
	 * @param originalLocation
	 * @param movementLocation
	 * @param correctTurn
	 * @return True if the move is valid, false if not
	 */
	public boolean isValidMove(int originalLocation, int movementLocation, boolean correctTurn) {
		// Values initialized here because isValidMove also checks for capturing
		arrayListStore = -1;
		captureLocationStore = 0;
		shiftStore = 0;

		// Functions store the location as the ARRAYLIST sees it
		int adjOriginalLocation = (originalLocation % 8) + 8 * (7 - (originalLocation / 8));
		int adjMovementLocation = (movementLocation % 8) + 8 * (7 - (movementLocation / 8));

		// If it's the appropriate player's turn
		if (correctTurn) {

			// Runs through the pieces ArrayList to find the appropriate piece
			for (int i = 0; i < pieces.size(); i++) {

				// If the selected piece matches the location of the piece selected
				if (pieces.get(i).getLocationNum() == adjOriginalLocation) {

					// Halts any attempted backwards movement if the piece is not a king
					if ((pieces.get(i).isRed() 
							&& originalLocation < movementLocation
							&& !pieces.get(i).isKing())
							|| (!pieces.get(i).isRed() 
									&& originalLocation > movementLocation
									&& !pieces.get(i).isKing())) {
						return false;
					}

					// If branch checking for a red or king piece moving down the board
					/* Yes, all of these checks are unfortunately necessary to prevent pieces from taking a magic portal from one 
					 * side of the board to the other
					 */
					if ((pieces.get(i).isRed() 
							&& (pieces.get(i).getLocationNum() % 8 != 0 && pieces.get(i).getLocationNum() % 8 != 7)
							&& (originalLocation - DOWN_MOVE_RIGHT == movementLocation || originalLocation - DOWN_MOVE_LEFT == movementLocation))
							// Statement one checks if Red, if not on an edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isRed())
							&& (pieces.get(i).getLocationNum() % 8 == 0)
							&& (originalLocation - DOWN_MOVE_RIGHT == movementLocation)
							// Statement two checks if Red, if on the LEFT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isRed())
							&& (pieces.get(i).getLocationNum() % 8 == 7)
							&& (originalLocation - DOWN_MOVE_LEFT == movementLocation)
							// Statement three checks if Red, if on the RIGHT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 != 0 && pieces.get(i).getLocationNum() % 8 != 7)
							&& (originalLocation - DOWN_MOVE_RIGHT == movementLocation || originalLocation - DOWN_MOVE_LEFT == movementLocation)
							// Statement four checks if King, if not on an edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 == 0)
							&& (originalLocation - DOWN_MOVE_RIGHT == movementLocation)
							// Statement five checks if King, if on the LEFT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 == 7)
							&& (originalLocation - DOWN_MOVE_LEFT == movementLocation))
						// Statement six checks if King, if on the RIGHT edge, and if the desired movement location is appropriate
					{

						// Runs through the ArrayList to look for the piece at the desired movement location
						for (int j = 0; j < pieces.size(); j++) {
							if (pieces.get(j).getLocationNum() == adjMovementLocation) {
								if (pieces.get(j).isAlive()) { // If the desired move spot is occupied
									return false;
								} else {
									return true;
								}
							}
						}
					} 
					// If branch checking for a red or king piece capturing down the board
					else if ((pieces.get(i).isRed()
							&& (pieces.get(i).getLocationNum() % 8 != 0 && pieces.get(i).getLocationNum() % 8 != 7)
							&& (originalLocation - (2 * DOWN_MOVE_LEFT) == movementLocation || originalLocation - (2 * DOWN_MOVE_RIGHT) == movementLocation))
							// Statement one checks if Red, if not on an edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isRed())
							&& (pieces.get(i).getLocationNum() % 8 == 0)
							&& (originalLocation - (2 * DOWN_MOVE_RIGHT) == movementLocation)
							// Statement two checks if Red, if on the LEFT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isRed())
							&& (pieces.get(i).getLocationNum() % 8 == 7)
							&& (originalLocation - (2 * DOWN_MOVE_LEFT) == movementLocation)
							// Statement three checks if Red, if on the RIGHT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 != 0 && pieces.get(i).getLocationNum() % 8 != 7)
							&& (originalLocation - (2 * DOWN_MOVE_LEFT) == movementLocation || originalLocation - (2 * DOWN_MOVE_RIGHT) == movementLocation)
							// Statement four checks if King, if not on an edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 == 0)
							&& (originalLocation - (2 * DOWN_MOVE_RIGHT) == movementLocation)
							// Statement five checks if King, if on the LEFT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 == 7)
							&& (originalLocation - (2 * DOWN_MOVE_LEFT) == movementLocation))
						// Statement six checks if King, if on the RIGHT edge, and if the desired movement location is appropriate
					{

						// Checks to see if a piece is in between the desired location and the piece selected
						for (int j = 0; j < pieces.size(); j++) {

							// Checking for a capture to the down-right
							if (originalLocation - (2 * DOWN_MOVE_RIGHT) == movementLocation) {
								if (pieces.get(j).getLocationNum() == adjMovementLocation - DOWN_MOVE_LEFT) {
									if (pieces.get(j).isAlive()
											&& (pieces.get(j).isRed() != pieces.get(i).isRed())) { // If the piece between is NOT the same color
										for (int k = 0; k < pieces.size(); k++) {
											if (pieces.get(k).getLocationNum() == adjMovementLocation
													&& pieces.get(k).isAlive()) { // If a piece is located at the desired move spot
												return false;
											}
										}
										// Values are set for the capture function to perform the piece change
										arrayListStore = j; // Remembers the piece to be captured
										captureLocationStore = adjMovementLocation; // Remembers where the new piece is
										shiftStore = DOWN_MOVE_LEFT; // Remembers the direction the piece moved
										return true;
									} else {
										return false;
									}
								}
							} 								
							// Checking for a capture to the down-left
							else {
								if (pieces.get(j).getLocationNum() == adjMovementLocation - DOWN_MOVE_RIGHT) {
									if (pieces.get(j).isAlive()
											&& (pieces.get(j).isRed() != pieces.get(i).isRed())) {
										for (int k = 0; k < pieces.size(); k++) {
											if (pieces.get(k).getLocationNum() == adjMovementLocation
													&& pieces.get(k).isAlive()) {
												return false;
											}
										}
										arrayListStore = j;
										captureLocationStore = adjMovementLocation;
										shiftStore = DOWN_MOVE_RIGHT;
										return true;
									} else {
										return false;
									}
								}
							}
						}
					}

					// If branch checking for a black or king piece moving up the board
					else if ((!pieces.get(i).isRed() 
							&& (pieces.get(i).getLocationNum() % 8 != 0 && pieces.get(i).getLocationNum() % 8 != 7)
							&& (originalLocation - UP_MOVE_RIGHT == movementLocation || originalLocation - UP_MOVE_LEFT == movementLocation))
							// Statement one checks if Black, if not on an edge, and if the desired movement location is appropriate

							|| (!pieces.get(i).isRed())
							&& (pieces.get(i).getLocationNum() % 8 == 0)
							&& (originalLocation - UP_MOVE_RIGHT == movementLocation)
							// Statement two checks if Black, if on the LEFT edge, and if the desired movement location is appropriate

							|| (!pieces.get(i).isRed())
							&& (pieces.get(i).getLocationNum() % 8 == 7)
							&& (originalLocation - UP_MOVE_LEFT == movementLocation)
							// Statement three checks if Black, if on the RIGHT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 != 0 && pieces.get(i).getLocationNum() % 8 != 7)
							&& (originalLocation - UP_MOVE_RIGHT == movementLocation || originalLocation - UP_MOVE_LEFT == movementLocation)
							// Statement four checks if King, if not on an edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 == 0)
							&& (originalLocation - UP_MOVE_RIGHT == movementLocation)
							// Statement five checks if King, if on the LEFT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 == 7)
							&& (originalLocation - UP_MOVE_LEFT == movementLocation))
						// Statement six checks if King, if on the RIGHT edge, and if the desired movement location is appropriate 
					{

						for (int j = 0; j < pieces.size(); j++) {
							if (pieces.get(j).getLocationNum() == adjMovementLocation) {
								if (pieces.get(j).isAlive()) {
									return false;
								} else {
									return true;
								}
							}
						}

						// If branch checking for a black or king piece capturing up the board
					} else if ((!pieces.get(i).isRed()
							&& (pieces.get(i).getLocationNum() % 8 != 0 && pieces.get(i).getLocationNum() % 8 != 7)
							&& (originalLocation - (2 * UP_MOVE_LEFT) == movementLocation || originalLocation - (2 * UP_MOVE_RIGHT) == movementLocation))
							// Statement one checks if Black, if not on an edge, and if the desired movement location is appropriate

							|| (!pieces.get(i).isRed())
							&& (pieces.get(i).getLocationNum() % 8 == 0)
							&& (originalLocation - (2 * UP_MOVE_RIGHT) == movementLocation)
							// Statement two checks if Black, if on the LEFT edge, and if the desired movement location is appropriate

							|| (!pieces.get(i).isRed())
							&& (pieces.get(i).getLocationNum() % 8 == 7)
							&& (originalLocation - (2 * UP_MOVE_LEFT) == movementLocation)
							// Statement three checks if Black, if on the RIGHT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 != 0 && pieces.get(i).getLocationNum() % 8 != 7)
							&& (originalLocation - (2 * UP_MOVE_LEFT) == movementLocation || originalLocation - (2 * UP_MOVE_RIGHT) == movementLocation)
							// Statement four checks if King, if not on an edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 == 0)
							&& (originalLocation - (2 * UP_MOVE_RIGHT) == movementLocation)
							// Statement five checks if King, if on the LEFT edge, and if the desired movement location is appropriate

							|| (pieces.get(i).isKing())
							&& (pieces.get(i).getLocationNum() % 8 == 7)
							&& (originalLocation - (2 * UP_MOVE_LEFT) == movementLocation)) 
						// Statement six checks if King, if on the RIGHT edge, and if the desired movement location is appropriate
					{

						for (int j = 0; j < pieces.size(); j++) {
							// Checking for a capture to the up-right
							if (originalLocation - (2 * UP_MOVE_RIGHT) == movementLocation) {
								if (pieces.get(j).getLocationNum() == adjMovementLocation - UP_MOVE_LEFT) {
									if (pieces.get(j).isAlive()
											&& (pieces.get(j).isRed() != pieces.get(i).isRed())) {
										for (int k = 0; k < pieces.size(); k++) {
											if (pieces.get(k).getLocationNum() == adjMovementLocation
													&& pieces.get(k).isAlive()) {
												return false;
											}
										}
										arrayListStore = j;
										captureLocationStore = adjMovementLocation;
										shiftStore = UP_MOVE_LEFT;
										return true;
									} else {
										return false;
									}
								}
							} 
							// Checking for a capture to the up-right
							else {
								if (pieces.get(j).getLocationNum() == adjMovementLocation - UP_MOVE_RIGHT) {
									if (pieces.get(j).isAlive()
											&& (pieces.get(j).isRed() != pieces.get(i).isRed())) {
										for (int k = 0; k < pieces.size(); k++) {
											if (pieces.get(k).getLocationNum() == adjMovementLocation
													&& pieces.get(k).isAlive()) {
												return false;
											}
										}
										arrayListStore = j;
										captureLocationStore = adjMovementLocation;
										shiftStore = UP_MOVE_RIGHT;
										return true;
									} else {
										return false;
									}
								}
							}
						}
					} 
				}
			}
		}

		// If every case fails, return false
		return false;

	}

}

