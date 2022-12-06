import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/*
 * File: CapturePanel.java
 * Author: Martin Cox
 * Contributors: Martin Cox, David McLain
 * Description: This class is used for displaying captured pieces
 */
@SuppressWarnings("serial")
public class CapturePanel extends JPanel {
	
	private List<String> capturedPieces;
	private JLabel pawn;
	private JLabel pawnAmount;
	private JLabel rook;
	private JLabel rookAmount;
	private JLabel knight;
	private JLabel knightAmount;
	private JLabel bishop;
	private JLabel bishopAmount;
	private JLabel queen;
	private JLabel queenAmount;
	private Font font = new Font("Monospaced", Font.ITALIC, 40);
	/**
	 * Instantiates CapturePanel
	 * @param color - color of player panel is displaying captured pieces for
	 */
	public CapturePanel(Side color) {
		super();
		String end = color == Side.BLACK ? "black.png" : "white.png";
		setLayout(new GridLayout(5, 2));
		pawn = new JLabel(new ImageIcon("src/pawn_" + end));
		add(pawn);
		pawnAmount = new JLabel("x0", SwingConstants.CENTER);
		pawnAmount.setFont(font);
		pawnAmount.setForeground(Color.WHITE);
		add(pawnAmount);
		
		rook = new JLabel(new ImageIcon("src/rook_" + end));
		add(rook);
		rookAmount = new JLabel("x0", SwingConstants.CENTER);
		rookAmount.setFont(font);
		rookAmount.setForeground(Color.WHITE);
		add(rookAmount);
		
		knight = new JLabel(new ImageIcon("src/knight_" + end));
		add(knight);
		knightAmount = new JLabel("x0", SwingConstants.CENTER);
		knightAmount.setFont(font);
		knightAmount.setForeground(Color.WHITE);
		add(knightAmount);
		
		bishop = new JLabel(new ImageIcon("src/bishop_" + end));
		add(bishop);
		bishopAmount = new JLabel("x0", SwingConstants.CENTER);
		bishopAmount.setFont(font);
		bishopAmount.setForeground(Color.WHITE);
		add(bishopAmount);
		
		queen = new JLabel(new ImageIcon("src/queen_" + end));
		add(queen);
		queenAmount = new JLabel("x0", SwingConstants.CENTER);
		queenAmount.setFont(font);
		queenAmount.setForeground(Color.WHITE);
		add(queenAmount);
	}
	/**
	 * Sets captured pieces for displaying
	 * @param capturedPieces - captured pieces
	 */
	public void setCapturedPieces(List<String> capturedPieces) {
		this.capturedPieces = capturedPieces;
		update();
	}
	/**
	 * Refreshes amount of captured pieces to display according to new amount
	 */
	public void update() {
		int pawns = 0, rooks = 0, knights = 0, bishops = 0, queens = 0;
		for (String p : capturedPieces) {
			if (p.startsWith("pawn"))
				pawns++;
			else if (p.startsWith("rook"))
				rooks++;
			else if (p.startsWith("knight"))
				knights++;
			else if (p.startsWith("bishop"))
				bishops++;
			else if (p.startsWith("queen"))
				queens++;
		}
		pawnAmount.setText("x" + pawns);
		rookAmount.setText("x" + rooks);
		knightAmount.setText("x" + knights);
		bishopAmount.setText("x" + bishops);
		queenAmount.setText("x" + queens);
	}
}
