import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

@SuppressWarnings("unused")
public abstract class Client {

	protected Game game;
	protected Square[][] squares = new Square[8][8];
	protected JFrame frame;
	protected Piece curPiece;
	protected boolean pieceChosen;
	protected Set<Move> legalMoves;
	protected boolean competitive;

	Client(boolean competitive) {
		pieceChosen = false;
		legalMoves = new HashSet<>();
		curPiece = null;
		frame = new JFrame("Chess");
		frame.setLayout(null);
		addLettersAndNumbers();
		frame.setVisible(true);
		frame.setSize(925, 950);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println(competitive);
	}
	
	public Client(List<String> toLoad) {
		for (String s : toLoad) {
			System.out.println(s);
		}
	}

	private void addLettersAndNumbers() {
		Font font = new Font("Monospaced", Font.PLAIN, 40);
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

	protected JPanel getBoard() {
		JPanel board = new JPanel();
		Color lightBrown = new Color(112, 79, 50);
		Color darkBrown = new Color(191, 153, 114);
		board.setBackground(Color.BLACK);
		board.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
		board.setLayout(new GridLayout(8, 8, 2, 2));
		board.setBounds(50, 50, 800, 800);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Color color;
				Square cur;
				final int row = i;
				final int col = j;
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					color = darkBrown;
				} else {
					color = lightBrown;
				}
				cur = new Square(color, game.getBoard().getPiece(i, j), row, col);
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

	private void processMove(Square cur, int i, int j) {
		if (!pieceChosen) {
			System.out.println("Nothing chosen; Row: " + i + ", Col: " + j);
			try {
				selectPiece(i, j);
			} catch (Exception e) {

			}
		} else {
			int startRow = curPiece.getRow();
			int startCol = curPiece.getCol();
			// legalMoves = game.getLegalMoves(curPiece);
			// If click on already-selected piece, deselect piece
			if (startRow == i && startCol == j) {
				resetSquares();
				curPiece = null;
				pieceChosen = false;
			} else if (game.hasPiece(i, j) && curPiece.getColor() == game.getPiece(i, j).getColor()) {
				try {
					resetSquares();
					selectPiece(i, j);
				} catch (Exception e) {

				}
			} else {
				System.out.println("Piece chosen; Moving to Row:" + i + ", Col: " + j);
				try {
					game.movePiece(curPiece, i, j);
					squares[startRow][startCol].updatePiece(null);
					resetSquares();
					squares[i][j].updatePiece(curPiece);
					pieceChosen = false;
				} catch (Exception e) {

				}
			}

		}
		// System.out.println((8 - i) + ", " + (char)(j + 65));
	}

	private void selectPiece(int i, int j) throws Exception {
		curPiece = game.selectPiece(i, j);
		pieceChosen = true;
		legalMoves = game.getLegalMoves(curPiece);
		for (Move m : legalMoves) {
			squares[m.getRow()][m.getCol()].updateLook(Color.YELLOW);
		}

	}

	private void resetSquares() {
		for (Square[] row : squares) {
			for (Square sq : row) {
				sq.resetLook();
			}
		}
		for (Piece p : game.getCheckingPieces()) {
			squares[p.getRow()][p.getCol()].updateLook(Color.RED);
		}

	}
	
	public void start() {
		
	}
	
}