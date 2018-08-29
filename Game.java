import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

/**
 * @author Joshua
 * @author Daniel
 * @author Alan
 * Responsible for running the game with a possible loop function and getting everything to work correctly
 */	
public class Game implements MouseListener {

	static boolean isRunning;
	private static Stack<Board> boards = new Stack<>();
	private static Board currentBoard = new Board();
	private static boolean isP1Turn = true;
	private boolean additionalJumpingTime = false;
	private boolean canUndo = true;
	private static boolean justMoved = true;
	private int mouseX;
	private int mouseY;
	private int gridSelectX;
	private int gridSelectY;
	private static int firstGridVal = -1;
	private static int secondGridVal = -1;
	private static int squareSize = 75;
	private static Drawing d;
	private static Game g;
	private static String lastMoveString = "";
	private int lastLocationMoved = -1;

	/**
	 * Initializes Game, creates the drawing object, and begins game loop.
	 * @param args
	 */
	public static void main(String[] args)
	{
		//adds the board so that you can use the undo function
		boards.push(currentBoard.copyBoard());
		g = new Game();
		d = new Drawing(currentBoard,squareSize);
		d.addMouseListener(g); //adds mouse listener to JFrame
		
		while(true) //Game loop - keeps game open until JFrame is closed
		{

		}
	}

	/**
	 * Needed for instantiation of Mouse Listener
	 */
	public Game() 
	{
		
	}
	
	/**
	 * Loops through ever piece of current player's color 
	 * to determine if current player has a valid move.
	 * @param redTurn - true = red's turn, false = black's turn
	 * @return if current player has a valid move
	 */
	public static boolean hasValidMove(boolean redTurn) 
	{
        for(int i = 0; i < 64; i++) 
        {
               //finds an alive piece of the right color
               if(currentBoard.getPiece(i).isRed() == redTurn && currentBoard.getPiece(i).isAlive()) 
               {
                     //checks if it has a valid move
                     for(int j = 0; j < 64; j++) 
                     {
                    	 int pseudoLocationI = (i % 8) + 8 * (7 - (i / 8));
                    	 int pseudoLocationJ = (j % 8) + 8 * (7 - (j / 8));
                            if(currentBoard.isValidMove(pseudoLocationI, pseudoLocationJ, true)) 
                            {
                                   return true;
                            }
                     }
               }
        }
        return false;
	}
	
	/**
	 * Checks whether the current player has a valid move.
	 * @return If game is over (if current player does not have a valid move)
	 */
	public static boolean isWinner() 
	{
		if (hasValidMove(!isP1Turn)==false)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * If there is a winner, set that to be displayed on the JFrame
	 */
	public void handlePossibleWin()
	{
		if(hasValidMove(false)==false)
		{
			lastMoveString = "RED WINS";
		}
		else if (hasValidMove(true)==false)
		{
			lastMoveString = "BLACK WINS";
		}

	}
	

	/**
	 * decides if the player can make a move at the moment
	 * @return boolean on whether it is the players turn or not
	 */
	public boolean isPlayersTurn() {
		return isP1Turn;
	}

	/**
	 * Destroys all other boards on stack and adds a new default board, resetting all other game variables.
	 */
	public static void resetBoard()
	{
		boards.clear();
		isP1Turn = true;
		justMoved = false;
		currentBoard = new Board();
		boards.push(currentBoard.copyBoard());
		firstGridVal = -1;
		secondGridVal = -1;
		lastMoveString ="";
		d.setBoard(currentBoard);
	}
	
	/**
	 * returns first square selected in move sequence
	 * @return firstGridVal
	 */
	public static int getFirstGridVal()
	{
		return firstGridVal;
	}

	/**
	 * Destroys last board, setting current board to be the previous move
	 * @return the previous move (now the current move)'s board
	 */
	public static Board getLastBoard() {
		boards.pop();
		Board temp = (boards.peek());

		return(temp);
	}


	/**
	 * Process clicks on board
	 */
	public void mousePressed(java.awt.event.MouseEvent e) {

		mouseX = e.getX(); //find mouse positions
		mouseY = e.getY();
		//if inside board
		if(isWinner()==true) //if game is over, reset everything once they click the board
		{
			resetBoard();
		}
		//else if game is not over and click is inside board
		else if(mouseX>100 && mouseX<(100+(8*squareSize)) && mouseY > 100 && mouseY<(100+(8*squareSize))&& isWinner()==false)
		{
			
			gridSelectX = (mouseX-100)/squareSize; //translate to squares
			gridSelectY = 7-(mouseY-100)/squareSize;

			if(firstGridVal == -1) //if no square selected
			{
				firstGridVal = (gridSelectY * 8)+gridSelectX ; //set the first square to clicked location
			}
			else //if first square is already selected
			{ 
				secondGridVal = (gridSelectY * 8)+gridSelectX; //set second square to clicked location
				int pseudoLocation = (firstGridVal % 8) + 8 * (7 - (firstGridVal / 8)); //change location to match square with ArrayList position
				
				//if is validMove and piece is correct color - black for player 1 and red for player 2;
				if (currentBoard.isValidMove(firstGridVal,secondGridVal, 
						(isP1Turn != currentBoard.getPiece(pseudoLocation).isRed())) ==true) 
				{
					//if EITHER: 1)This is not your first move and your move would make sense for additional jumps
					//OR: 2) This is your first move on your turn
					if((additionalJumpingTime==true && Math.abs(firstGridVal-secondGridVal)>=10) || additionalJumpingTime==false)
					{
					
					currentBoard.move(firstGridVal, secondGridVal); //moves your piece
					currentBoard.capture(); //removes the opponets piece if you are jumping
					currentBoard.checkForKing(); //turns piece king if it reached the end of the board
					canUndo = false;
						//if this is your first move this turn
						if(additionalJumpingTime ==false)
						{
							additionalJumpingTime=true; //set turn state to additional jumping stage
							lastLocationMoved = secondGridVal; //records where piece ended up
							firstGridVal = lastLocationMoved;//makes sure you do additional jumps with same piece
							secondGridVal = -1;
						}
						else //if you are in the additional jumps stage of your turn
						{
							lastLocationMoved = secondGridVal;
							firstGridVal = lastLocationMoved;
							secondGridVal = -1;
						}
					
					handlePossibleWin(); //checks for win after each valid move
					}
					else //if cannot jump at this time
					{
						if(additionalJumpingTime ==true) //if invalid move was during additional jump stage
						{
							firstGridVal = lastLocationMoved; //makes sure you do additional jumps with same piece
							secondGridVal = -1;
						}
						else // if invalid move was during your first move on your turn
						{
							firstGridVal = -1; //resets clicked squares
							secondGridVal = -1;
						}

						lastMoveString = "Invalid Move";
					}
					
				}
				else //if move was invalid
				{
					firstGridVal = lastLocationMoved;
					secondGridVal = -1;
					lastMoveString = "Invalid Move";

				}

			}
			
		}
		//undo function
		else if(mouseX < squareSize*15 - 15 && mouseX > squareSize*13 && mouseY > squareSize*2 + 30 && mouseY < squareSize*3 + 10 )
		{
			if(canUndo==true) //if user is not in the middle of his turn
			{
				//Undo function
				if(boards.size()== 1) //if no moves have been made
				{
					lastMoveString = "Starting Board";
					currentBoard = boards.peek().copyBoard(); //set currentBoard to only board
					//Makes it black's turn
					isP1Turn=true;
					justMoved = true;
					
				}
	
				else {
					//so you don't have to click the undo button twice
					
					if(isP1Turn==true) //toggles turn
					{
						isP1Turn=false;
					}
					else
					{
						isP1Turn = true;
					}
					
					if(justMoved) //if just moved
					{
						boards.pop();
						
						
					}
					currentBoard = boards.peek().copyBoard(); //sets currentBoard to be the board one move back
					justMoved = true;
					
					
				}
			}
			else //if in middle of his turn
			{
			lastMoveString = "Finish move to undo";	
			
			}
			
		}
		//end turn button
		else if(mouseX>squareSize*13 && mouseX <(squareSize*13) + squareSize*2+60 && mouseY>squareSize*2+130 
				&& mouseY <(squareSize*2+130) + squareSize*3-20)
		{
			//Undo function knows it is the end of a turn
			canUndo = true;
			justMoved = true;
			
			//adds the board so that you can use the undo function
			boards.push(currentBoard.copyBoard());
			if(additionalJumpingTime==true)
			{
				if(isP1Turn==true) //toggles turn
				{
					isP1Turn=false;
	
				}
				else
				{
					isP1Turn = true;
				}
			}
			additionalJumpingTime=false; //resets vars
			lastLocationMoved = -1;
			firstGridVal = -1;
		}
		
		d.setBoard(currentBoard);
		
		d.draw(); //draws board

	}

	/**
	 * @return a string detailing current player move
	 */
	public static String getCurrentPlayerString()
	{
		if(isWinner()==true)
		{
			return "CLICK ON THE BOARD TO PLAY ANOTHER GAME";
		}
		if(isP1Turn==true)
		{
			return "BLACK'S TURN";
		}
		else
		{
			return "RED'S TURN";
		}
		
	}
	/**
	 * Returns winner/last move string
	 * @return
	 */
	public static String getLastMoveString()
	{

		return lastMoveString;
	}

	/**
	 * Need these following functions to satisfy interface
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}


}
