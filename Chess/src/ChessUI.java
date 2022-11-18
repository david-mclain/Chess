import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8554571131076780052L;
	
	Board board;

	ChessUI() {
		super("Chess");
		board = new Board();
		setLayout(null);
		add(getBoard());
		setVisible(true);
		setSize(900, 925);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JPanel getBoard() {
		JPanel board = new JPanel();
		board.setBackground(Color.BLACK);
		board.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
		board.setLayout(new GridLayout(8, 8, 2, 2));
		board.setBounds(50, 50, 800, 800);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JPanel panel = new JPanel();
				Color color;
				BoardSquare cur;
				final int row = i;
				final int col = j;
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					color = new Color(112, 79, 50);
				}
				else {
					color = new Color(191, 153, 114);
				}
				cur = new BoardSquare(color, this.board.getPiece(i, j), row, col);
				cur.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						processMove(cur, row, col);
					}
				});
				board.add(cur);
			}
		}
		return board;
	}
	
	private void processMove(BoardSquare cur, int i, int j) {
		
	}
	
	public static void main(String[] args) {
		ChessUI cur = new ChessUI();
	}
	
	public void update() {
		
	}
	
}
