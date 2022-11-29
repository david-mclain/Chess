import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Square extends JPanel {
	
	private Piece piece;
	private Color defaultColor;
	private JLabel label;
	
	public Square(Color defaultColor, Piece piece, int row, int col) {
		this.defaultColor = defaultColor;
		this.piece = piece;
		label = new JLabel();
		setLayout(new GridBagLayout());
		updatePiece(piece);
		add(label);
	}
	
	public void updatePiece(Piece piece) {
		this.piece = piece;
		updateLook(defaultColor);
	}
	
	public void resetLook() {
		setBackground(defaultColor);
	}
	
	public void updateLook(Color color) {
		setBackground(color);
		if (piece != null) {
			label.setIcon(new ImageIcon(piece.getIcon()));
		}
		else {
			label.setIcon(null);
		}
	}
	
}
