import java.util.List;
import javax.swing.ImageIcon;

public class King extends ChessPiece {

	public King(int row, int col, boolean color) {
		super(row, col, color);
		if (color) {
			image = new ImageIcon("src/king_black.png");
		}
		else {
			image = new ImageIcon("src/king_white.png");
		}
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}

}
