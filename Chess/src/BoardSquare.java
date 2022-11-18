import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoardSquare extends JPanel {
	
	private Piece piece;
	private Color defaultColor;
	private JLabel label;
	private int row;
	private int col;
	
	public BoardSquare(Color defaultColor, Piece piece, int row, int col) {
		this.defaultColor = defaultColor;
		this.piece = piece;
		this.row = row;
		this.col = col;
		label = new JLabel();
		setLayout(new GridBagLayout());
		updateLook(defaultColor);
		add(label);
	}
	
	public void updatePiece(Piece piece) {
		this.piece = piece;
		updateLook(defaultColor);
	}
	
	private void updateLook(Color color) {
		setBackground(defaultColor);
		if (piece != null) {
			label.setIcon(piece.getIcon());
		}
		else {
			label.setIcon(null);
		}
	}
	
}
