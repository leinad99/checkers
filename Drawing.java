import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

/**
 * @author Joshua
 * @author Daniel
 * @author Alan
 * Acts as JFrame window and handles all drawing functions
 */	
public class Drawing extends JPanel {

	private static final long serialVersionUID = 1L;
	static Font f;
	static Font fKing;
	static int currentPlayer;
	private final static int NUM_ROWS = 8;
	private final static int NUM_COLS = 8;
	static Drawing c;
	private static Board b;
	int squareSize;
	static JFrame frame;
	String lastMoveString = "";
	
	/**
	 * Instantiates Drawing, setting the current board, and runs init to create JFrame
	 * @param board - currentBoard in Game class (what will be drawn)
	 * @param squareSizeNew - how big the board should be drawn
	 */
	public Drawing(Board board, int squareSizeNew)
	{
		squareSize = squareSizeNew;
		b = board;
		init();
	}
	/**
	 * Sets the board to be drawn
	 * @param board - the board to be drawn
	 */
	public void setBoard(Board board)
	{
		b = board;
	}
	/**
	 * Runs setup, creating JFrame, initializing JFrame variables
	 */
	public void init() //initializes JFrame and variables
	{
		f = new Font("TimesRoman", Font.PLAIN, 40);
		fKing = new Font("TimesRoman", Font.PLAIN, 35);
		
		frame = new JFrame("Checkers Game");
		frame.getContentPane().add(this);
		this.setBackground(Color.WHITE);
		Dimension d = new Dimension (1200,1000);
		this.setPreferredSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Draws board, pieces, and buttons
	 */
	@Override  //equivalent of draw board/pieces function
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g); //Calls parents draw function to init drawing
		
		g.setFont(f);
		g.drawString(Game.getLastMoveString(),100,65); //draw last move string / winner string
		g.drawString(Game.getCurrentPlayerString(),475,65); //draws current players string
		
		//draw undo button
		g.setColor(Color.yellow);
		g.fillRect(squareSize*13, squareSize*2+30, squareSize*2-15, squareSize-20);
		g.setColor(Color.black);
		g.drawString("UNDO",squareSize*13+10,squareSize*3);
		
		//draw end turn button		
		g.setColor(Color.yellow);
		g.fillRect(squareSize*13, squareSize*2+130, squareSize*2+60, squareSize-20);
		g.setColor(Color.black);
		g.drawString("END TURN",squareSize*13+10,(squareSize*3)+100);
		
		
		for(int i =0;i<NUM_COLS;i++) //draw board
		{
			for(int j=0;j<NUM_ROWS; j++) 
			{
				if(i%2 == j%2) //if "every other square" - drawn gray
				{
					g.setColor(Color.gray);
				}
				else //
				{
					g.setColor(Color.WHITE);

				}
				if((j*8)+i==Game.getFirstGridVal()) //if player has currently selected this square
				{
					g.setColor(Color.BLUE);
				}
				
				//draw actual square
				g.fillRect(100+(i*squareSize),(100+squareSize*8)-((j+1)*squareSize), squareSize, squareSize);
				g.drawRect(100, 100, squareSize*8, squareSize*8);
			}
		}
		
		
		for(int i=0;i<64;i++) //draw pieces
		{
			
			int locNum = b.getPiece(i).getLocationNum(); //find where piece is on board

			if (b.getPiece(i).isRed() && b.getPiece(i).isAlive()) //draw red pieces
			{
				
	
				g.setColor(Color.red);
				g.fillOval(100+(generateCol(locNum)*squareSize) + 5, 100+ (generateRow(locNum)*squareSize) +5, squareSize-10, squareSize-10);
				g.setColor(Color.black);
				g.drawOval(100+(generateCol(locNum)*squareSize) + 5, 100+ (generateRow(locNum)*squareSize) +5, squareSize-10, squareSize-10);
				
				if(b.getPiece(i).isKing()==true) //if king
					{
					g.setFont(fKing);
					g.setColor(Color.WHITE);
					g.drawString("K",100+(generateCol(locNum)*squareSize) + 26, 100+ (generateRow(locNum)*squareSize) +50);
					}
			}
			if ((!b.getPiece(i).isRed()) && b.getPiece(i).isAlive()) //draw black pieces
			{
				g.setColor(Color.black);
				g.fillOval(100+(generateCol(locNum)*squareSize) + 5, 100+ (generateRow(locNum)*squareSize) +5, squareSize-10, squareSize-10);
				g.setColor(Color.black);
				g.drawOval(100+(generateCol(locNum)*squareSize) + 5, 100+ (generateRow(locNum)*squareSize) +5, squareSize-10, squareSize-10);
				
				if(b.getPiece(i).isKing()==true) //if king
					{
					g.setFont(fKing);
					g.setColor(Color.WHITE);
					g.drawString("K",100+(generateCol(locNum)*squareSize) + 26, 100+ (generateRow(locNum)*squareSize) + 50);
					}
			}
		}
	}
	/**
	 * Redraws JFrame with Board b
	 */
	public void draw()
	{
		 frame.revalidate(); //after every move redraws board
		 frame.repaint();
	}
	
	/**
	 * @param locationNum - location of Piece in ArrayList
	 * @return Row of piece on Board
	 */
	public static int generateRow(int locationNum) {
		return (locationNum / NUM_ROWS);
	}
	
	/**
	 * @param locationNum - location of Piece in ArrayList
	 * @return Column of piece on Board
	 */
	public static int generateCol(int locationNum) {
		return (locationNum % NUM_COLS);
	}



	
}