import java.util.List;
import javax.swing.ImageIcon;

public class Rook extends ChessPiece {

	public Rook(int row, int col, boolean color) {
		super(row, col, color);
		if (color) {
			image = new ImageIcon("src/rook_black.png");
		}
		else {
			image = new ImageIcon("src/rook_white.png");
		}
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}
	
}
