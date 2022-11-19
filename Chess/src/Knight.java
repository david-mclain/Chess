import java.util.List;
import javax.swing.ImageIcon;

public class Knight extends ChessPiece {

	public Knight(int row, int col, boolean color) {
		super(row, col, color);
		if (color) {
			image = new ImageIcon("src/knight_black.png");
		}
		else {
			image = new ImageIcon("src/knight_white.png");
		}
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}

}
