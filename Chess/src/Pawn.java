
import java.util.List;

public class Pawn extends ChessPiece {
	
	private boolean firstMove;
	
	public Pawn(int row, int col, boolean color) {
		super(row, col, color);
		firstMove = true;
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}
	
}
