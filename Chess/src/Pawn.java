import java.util.List;
import javax.swing.ImageIcon;

public class Pawn extends ChessPiece {
	
	private boolean firstMove;
	
	public Pawn(int row, int col, boolean color) {
		super(row, col, color);
		firstMove = true;
		if (color) {
			image = new ImageIcon("src/pawn_black.png");
		}
		else {
			image = new ImageIcon("src/panw_white.png");
		}
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}
	
}
