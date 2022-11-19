import java.util.List;
import javax.swing.ImageIcon;

public class Queen extends ChessPiece {

	public Queen(int row, int col, boolean color) {
		super(row, col, color);
		if (color) {
			image = new ImageIcon("src/queen_black.png");
		}
		else {
			image = new ImageIcon("src/queen_white.png");
		}
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}

}
