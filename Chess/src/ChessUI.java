import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
		Font font = new Font("Monospaced", Font.PLAIN, 40);
		for (int i = 0; i < 8; i++) {
			JLabel num = new JLabel();
			num.setSize(40, 40);
			num.setFont(font);
			num.setText("" + (8 - i));
			num.setBounds(15, 80 + i * 100, 40, 40);
			num.setForeground(Color.BLACK);
			add(num);
			JLabel num2 = new JLabel();
			num2.setSize(40, 40);
			num2.setFont(font);
			num2.setText("" + (8 - i));
			num2.setBounds(865, 80 + i * 100, 40, 40);
			num2.setForeground(Color.BLACK);
			add(num2);
			JLabel let = new JLabel();
			let.setSize(40, 40);
			let.setFont(font);
			let.setText("" + (char)(i + 65));
			let.setBounds(90 + i * 100, 10, 40, 40);
			let.setForeground(Color.BLACK);
			add(let);
			JLabel let2 = new JLabel();
			let2.setSize(40, 40);
			let2.setFont(font);
			let2.setText("" + (char)(i + 65));
			let2.setBounds(90 + i * 100, 850, 40, 40);
			let2.setForeground(Color.BLACK);
			add(let2);
		}
		setVisible(true);
		setSize(925, 950);
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
