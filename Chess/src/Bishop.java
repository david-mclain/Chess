import java.util.List;
import javax.swing.ImageIcon;

public class Bishop extends ChessPiece {

	public Bishop(int row, int col, boolean color) {
		super(row, col, color);
		if (color) {
			image = new ImageIcon("src/bishop_black.png");
		}
		else {
			image = new ImageIcon("src/bishop_white.png");
		}
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}

}
