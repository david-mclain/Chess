import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
/*
 * File: Client.java
 * Author: David McLain
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for any UI handling and user input. 
 * Communicates to controllers and servers for sending and receiving information
 * of current game state.
 */
@SuppressWarnings("unused")
public abstract class Client {
	// GUI for processing user input
	protected Square[][] squares = new Square[8][8];
	protected Piece[][] curBoard;
	protected JFrame frame;
	protected JFrame promote;
	// For processing input correctly
	protected Side curColor;
	protected int curRow;
	protected int curCol;
	protected boolean pieceChosen;
	protected List<String> legalMoves;
	protected List<String> checkingPieces;
	protected PieceType promotePiece;
	// Timer fields
	protected boolean competitive;
	protected Timer whiteTimer;
	protected Timer blackTimer;
	protected int whiteTime;
	protected int blackTime;
	protected int curPlayer;
	// Timer display fields
	private JPanel whiteTimerPanel;
	private JLabel whiteTimerDisp;
	private JPanel blackTimerPanel;
	private JLabel blackTimerDisp;
	// Common fields
	protected CapturePanel whiteCapturePanel;
	protected CapturePanel blackCapturePanel;
	protected List<String> whiteCaptured;
	protected List<String> blackCaptured;
	private Font font = new Font("Monospaced", Font.PLAIN, 40);
	protected Border border = BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK);
	protected Border highlightBorder = BorderFactory.createMatteBorder(8, 8, 8, 8, Color.YELLOW);

	/**
	 * Instantiates instance of clients
	 * 
	 * @param competitive - if game mode is competitive or not
	 */
	Client(boolean competitive) {
		this(competitive, 0, 600, 600);
	}
	
	public Client(boolean competitive, int cur, int whiteTime, int blackTime) {
		this.competitive = competitive;
		pieceChosen = false;
		legalMoves = new ArrayList<>();
		checkingPieces = new ArrayList<>();
		whiteCaptured = new ArrayList<>();
		blackCaptured = new ArrayList<>();
		this.whiteTime = whiteTime;
		this.blackTime = blackTime;
		this.curPlayer = cur;
		frame = new JFrame("Chess");
		frame.setLayout(null);
		addLettersAndNumbers();
		createTimerUI();
		createCapturedUI();
		if (competitive) {
			whiteTimer = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					decreaseTime(1);
				}
			});
			blackTimer = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					decreaseTime(2);
				}
			});
			whiteTimer.setInitialDelay(1);
			blackTimer.setInitialDelay(1);
		}
		frame.add(getBoard());
		frame.setVisible(true);
		frame.setSize(1475, 960);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * Creates panels for displaying captured pieces from both players
	 */
	protected void createCapturedUI() {
		whiteCapturePanel = new CapturePanel(Side.BLACK);
		whiteCapturePanel.setBorder(border);
		whiteCapturePanel.setBackground(new Color(207, 185, 151));
		whiteCapturePanel.setBounds(940, 175, 220, 550);
		frame.add(whiteCapturePanel);
		blackCapturePanel = new CapturePanel(Side.WHITE);
		blackCapturePanel.setBorder(border);
		blackCapturePanel.setBackground(new Color(207, 185, 151));
		blackCapturePanel.setBounds(1190, 175, 220, 550);
		frame.add(blackCapturePanel);
	}
	/**
	 * Creates panels for displaying timers for players
	 */
	private void createTimerUI() {
		Color color = new Color(207, 185, 151);
		
		whiteTimerPanel = new JPanel();
		whiteTimerPanel.setBackground(color);
		whiteTimerPanel.setBorder(border);
		whiteTimerPanel.setBounds(940, 25, 220, 100);
		whiteTimerPanel.setLayout(new GridLayout(2, 1));
		
		JLabel white = new JLabel("White", SwingConstants.CENTER);
		white.setForeground(Color.WHITE);
		white.setFont(font);
		whiteTimerDisp = new JLabel((whiteTime / 60) + ":" + (whiteTime % 60 <= 9 ? ("0" + whiteTime % 60) : whiteTime % 60), SwingConstants.CENTER);
		whiteTimerDisp.setFont(font);
		whiteTimerDisp.setForeground(Color.WHITE);
		
		blackTimerPanel = new JPanel();
		blackTimerPanel.setBackground(color);
		blackTimerPanel.setBorder(border);
		blackTimerPanel.setBounds(1190, 25, 220, 100);
		blackTimerPanel.setLayout(new GridLayout(2, 1));
		
		JLabel black = new JLabel("Black", SwingConstants.CENTER);
		black.setForeground(Color.WHITE);
		black.setFont(font);
		blackTimerDisp = new JLabel((blackTime / 60) + ":" + (blackTime % 60 <= 9 ? ("0" + blackTime % 60) : blackTime % 60), SwingConstants.CENTER);
		blackTimerDisp.setFont(font);
		blackTimerDisp.setForeground(Color.WHITE);
		
		whiteTimerPanel.add(white);
		whiteTimerPanel.add(whiteTimerDisp);
		blackTimerPanel.add(black);
		blackTimerPanel.add(blackTimerDisp);
			
		frame.add(whiteTimerPanel);
		frame.add(blackTimerPanel);
	}
	/**
	 * Decrements current players time every 1000 ms
	 * @param player - player to decrement
	 */
	private void decreaseTime(int player) {
		if (player == 1) {
			whiteTimerDisp.setText((whiteTime / 60) + ":" + (whiteTime % 60 <= 9 ? ("0" + whiteTime % 60) : whiteTime % 60));
			if (whiteTime == 0)
				endGame("TIMER");
			whiteTime--;
		}
		else {
			blackTimerDisp.setText((blackTime / 60) + ":" + (blackTime % 60 <= 9 ? ("0" + blackTime % 60) : blackTime % 60));
			if (blackTime == 0)
				endGame("TIMER");
			blackTime--;
		}
	}

	/**
	 * Creates letters and numbers that are displayed on sides and top of board
	 */
	private void addLettersAndNumbers() {
		for (int i = 0; i < 8; i++) {
			JLabel num = new JLabel();
			num.setSize(40, 40);
			num.setFont(font);
			num.setText("" + (8 - i));
			num.setBounds(15, 80 + i * 100, 40, 40);
			num.setForeground(Color.BLACK);
			frame.add(num);

			JLabel num2 = new JLabel();
			num2.setSize(40, 40);
			num2.setFont(font);
			num2.setText("" + (8 - i));
			num2.setBounds(865, 80 + i * 100, 40, 40);
			num2.setForeground(Color.BLACK);
			frame.add(num2);

			JLabel let = new JLabel();
			let.setSize(40, 40);
			let.setFont(font);
			let.setText("" + (char) (i + 65));
			let.setBounds(90 + i * 100, 10, 40, 40);
			let.setForeground(Color.BLACK);
			frame.add(let);

			JLabel let2 = new JLabel();
			let2.setSize(40, 40);
			let2.setFont(font);
			let2.setText("" + (char) (i + 65));
			let2.setBounds(90 + i * 100, 850, 40, 40);
			let2.setForeground(Color.BLACK);
			frame.add(let2);
		}
	}

	/**
	 * Creates JPanel board for displaying chess board
	 * 
	 * @return - board for chess
	 */
	protected JPanel getBoard() {
		JPanel board = new JPanel();
		Color lightBrown = new Color(196, 164, 132);
		Color darkBrown = new Color(233, 205, 171);
		board.setBackground(Color.BLACK);
		board.setBorder(border);
		board.setLayout(new GridLayout(8, 8, 2, 2));
		board.setBounds(50, 50, 800, 800);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Color color;
				Square cur;
				final int row = i;
				final int col = j;
				if ((i + j) % 2 == 0) {
					color = darkBrown;
				} else {
					color = lightBrown;
				}
				cur = new Square(color);
				cur.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						processMove(cur, row, col);
					}
				});
				squares[i][j] = cur;
				board.add(cur);
			}
		}
		return board;
	}
	/**
	 * Returns path to icon of piece on board for displaying
	 * @param i - row of piece
	 * @param j - col of piece
	 * @return path to icon of piece on board for displaying
	 */
	protected String getIcon(int i, int j) {
		return curBoard[i][j].getIcon();
	}
	/**
	 * Ends game and displays cause of game ending
	 * @param cause - reason game ended
	 */
	protected void endGame(String cause) {
		if (competitive) {
			System.out.println("Stopping timers");
			whiteTimer.stop();
			blackTimer.stop();
		}
		End end = null;
		if (cause.equals("CHECKMATE")) {
			end = End.CHECKMATE;
		}
		else if (cause.equals("TIMER")) {
			end = End.TIMER;
		}
		else if (cause.equals("STALEMATE")) {
			end = End.STALEMATE;
		}
		else if (cause.equals("THREEFOLD")) {
			end = End.THREEFOLD;
		}
		else if (cause.equals("FIFTYMOVE")) {
			end = End.FIFTYMOVE;
		}
		PopupFactory pf = new PopupFactory();
		GameOverMessage message = new GameOverMessage(curPlayer, end);
		Popup p = pf.getPopup(frame, message, 350, 450);
		p.show();
	}

	/**
	 * Processes click on square
	 * 
	 * @param cur - square that is clicked on
	 * @param i   - row of square clicked on
	 * @param j   - col of square clicked on
	 */
	private void processMove(Square cur, int i, int j) {
		if (!pieceChosen) {
			try {
				selectPiece(i, j);
			} 
			catch (Exception e) {}
			return;
		} else {
			if (curRow == i && curCol == j) {
				resetSquares();
				pieceChosen = false;
				return;
			} else if (hasPiece(i, j) && curColor == getColor(i, j)) {
				try {
					resetSquares();
					selectPiece(i, j);
				} 
				catch (Exception e) {}
				return;
			} else {
				try {
					move(curRow, curCol, i, j);
					resetSquares();
					checkEnd();
					pieceChosen = false;
				} 
				catch (Exception e) {}
				return;
			}
		}
	}
	/**
	 * Returns color of piece in specified location
	 * @param i - row of piece
	 * @param j - col of piece
	 * @return color of piece in specified location
	 */
	protected Side getColor(int i, int j) {
		return curBoard[i][j].getColor();
	}
	/**
	 * Returns if a piece is in specified location or not
	 * @param i - row of location
	 * @param j - col of location
	 * @return if a piece if in specified location or not
	 */
	protected boolean hasPiece(int i, int j) {
		return curBoard[i][j] != null;
	}
	/**
	 * Flips players when turn is over
	 */
	public void flipPlayers() {
		if (competitive) {
			if (curPlayer % 2 == 0) {
				whiteTimer.stop();
				blackTimer.start();
			}
			else {
				whiteTimer.start();
				blackTimer.stop();
			}
		}
		if (curPlayer % 2 == 0) {
			whiteTimerPanel.setBorder(border);
			blackTimerPanel.setBorder(highlightBorder);
		}
		else {
			whiteTimerPanel.setBorder(highlightBorder);
			blackTimerPanel.setBorder(border);
		}
		curPlayer++;
	}
	
	/**
	 * Reset all squares background colors to default colors
	 */
	private void resetSquares() {
		for (int i = 0; i < 64; i++) {
			squares[i / 8][i % 8].resetLook();
		}
		showCheckingPieces();
	}
		
	/**
	 * Updates client to display new pieces based on move sent in
	 * 
	 * @param fenString - String containing FEN representation of current board
	 */
	public void update(String fenString) {
		curBoard = FEN.toBoard(fenString);
		for (int i = 0; i < 64; i++) {
			squares[i / 8][i % 8].updateIcon(curBoard[i / 8][i % 8] == null ? null : curBoard[i / 8][i % 8].getIcon());
		}
		showCheckingPieces();
	}

	private void showCheckingPieces() {
		for (String s : checkingPieces) {
			int col = s.charAt(0) - 'A';
			int row = Math.abs(Character.getNumericValue(s.charAt(1)) - 8);
			squares[row][col].updateLook(Color.RED);
		}
	}
	
	/**
	 * Starts timer if competitive is selected
	 * @param string 
	 */
	public void start(String string) {
		if (competitive) {
			if (string.equals("white")) {
				whiteTimer.start();
				whiteTimerPanel.setBorder(highlightBorder);
			}
			else {
				blackTimer.start();
				blackTimerPanel.setBorder(highlightBorder);
			}
		}
	}
	/**
	 * Sets checking pieces
	 * @param list - locations of how client is in check
	 */
	public void setCheckingPieces(String list) {
		checkingPieces.clear();
		list = list.substring(1, list.length() - 1);
		if (list.equals(""))
			return;
		for (String s : list.split(",")) {
			checkingPieces.add(s.trim());
		}
	}
	/**
	 * Gets captured pieces for display
	 * @param color - color for of pieces captured
	 * @return list of captured pieces
	 */
	public void setCapturedPieces(String list) {
		whiteCaptured.clear();
		blackCaptured.clear();
		list = list.substring(1, list.length() - 1);
		if (list.equals("")) {
			return;
		}
		for (String s : list.split(",")) {
			s = s.trim();
			if (s.endsWith("black")) {
				blackCaptured.add(s);
			}
			else {
				whiteCaptured.add(s);
			}
		}
		whiteCapturePanel.setCapturedPieces(blackCaptured);
		blackCapturePanel.setCapturedPieces(whiteCaptured);
	}
	/**
	 * Disables client frame if promotion of pieces is needed
	 * @param i - row to promote
	 * @param j - col to promote
	 */
	@SuppressWarnings("deprecation")
	public void promote(int i, int j) {
		promotePiece = null;
		frame.disable();
		promoteFrame(i, j);
	}
	/**
	 * Creates frame for user to select piece to promote to
	 * @param i - row to promote
	 * @param j - col to promote
	 */
	public void promoteFrame(int i, int j) {
		String color = curPlayer % 2 == 0 ? "_white.png" : "_black.png";
		
		promote = new JFrame("Promote Piece");
		promote.setLocationRelativeTo(frame);
		
		JPanel pieces = new JPanel();
		pieces.setLayout(null);
		pieces.setBackground(Color.WHITE);
		
		JButton rook = new JButton(new ImageIcon("src/rook" + color));
		rook.setBorder(border);
		rook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPromote(i, j, 1);
			}
		});
		rook.setBounds(25, 25, 100, 100);
		pieces.add(rook);
		
		JButton knight = new JButton(new ImageIcon("src/knight" + color));
		knight.setBorder(border);
		knight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPromote(i, j, 2);
			}
		});
		knight.setBounds(130, 25, 100, 100);
		pieces.add(knight);
		
		JButton bishop = new JButton(new ImageIcon("src/bishop" + color));
		bishop.setBorder(border);
		bishop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPromote(i, j, 3);
			}
		});
		bishop.setBounds(25, 130, 100, 100);
		pieces.add(bishop);
		
		JButton queen = new JButton(new ImageIcon("src/queen" + color));
		queen.setBorder(border);
		queen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPromote(i, j, 4);
			}
		});
		queen.setBounds(130, 130, 100, 100);
		pieces.add(queen);
		
		promote.setContentPane(pieces);
		promote.setLayout(null);
		promote.setSize(275, 300);
		promote.setResizable(false);
		promote.setVisible(true);
	}
	/**
	 * Sets information of piece to promote when user input is received
	 * @param i - row to promote
	 * @param j - col to promote
	 * @param piece - piece to promote to from user input
	 */
	@SuppressWarnings("deprecation")
	protected void setPromote(int i, int j, int piece) {
		promote.dispose();
		promote = null;
		promote(i, j, piece);
		frame.enable();
	}
	/**
	 * Communicates to game to promote
	 * @param i - row to promote
	 * @param j - col to promote
	 * @param type - type of piece to promote to
	 */
	public abstract void promote(int i, int j, int type);
	/**
	 * Checks if game is over
	 */
	protected abstract void checkEnd();
	/**
	 * Executes move client inputs
	 * @param curRow - row of piece client is moving from
	 * @param curCol - col of piece client is moving from
	 * @param i - row of piece client is moving to
	 * @param j - col of piece client is moving to
	 * @throws Exception
	 */
	protected abstract void move(int curRow, int curCol, int i, int j) throws Exception;
	/**
	 * Selects piece in game
	 * @param i - row of current piece
	 * @param j - col of current piece
	 * @throws Exception
	 */
	public abstract void selectPiece(int i, int j) throws Exception;
	
}
