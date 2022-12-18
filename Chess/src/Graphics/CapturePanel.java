package Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import Games.*;
import Pieces.Piece;
/*
 * File: CapturePanel.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
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
	private ClassLoader loader = Piece.class.getClassLoader();
	/**
	 * Instantiates CapturePanel
	 * @param color - color of player panel is displaying captured pieces for
	 */
	public CapturePanel(Side color) {
		super();
		String end = color == Side.BLACK ? "black.png" : "white.png";
		URL pawnPath = loader.getResource("pawn_" + end);
		setLayout(new GridLayout(5, 2));
		pawn = new JLabel(new ImageIcon(pawnPath));
		add(pawn);
		pawnAmount = new JLabel("x0", SwingConstants.CENTER);
		pawnAmount.setFont(font);
		pawnAmount.setForeground(Color.WHITE);
		add(pawnAmount);

		URL rookPath = loader.getResource("rook_" + end);
		rook = new JLabel(new ImageIcon(rookPath));
		add(rook);
		rookAmount = new JLabel("x0", SwingConstants.CENTER);
		rookAmount.setFont(font);
		rookAmount.setForeground(Color.WHITE);
		add(rookAmount);

		URL knightPath = loader.getResource("knight_" + end);
		knight = new JLabel(new ImageIcon(knightPath));
		add(knight);
		knightAmount = new JLabel("x0", SwingConstants.CENTER);
		knightAmount.setFont(font);
		knightAmount.setForeground(Color.WHITE);
		add(knightAmount);

		URL bishopPath = loader.getResource("bishop_" + end);
		bishop = new JLabel(new ImageIcon(bishopPath));
		add(bishop);
		bishopAmount = new JLabel("x0", SwingConstants.CENTER);
		bishopAmount.setFont(font);
		bishopAmount.setForeground(Color.WHITE);
		add(bishopAmount);

		URL queenPath = loader.getResource("queen_" + end);
		queen = new JLabel(new ImageIcon(queenPath));
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
