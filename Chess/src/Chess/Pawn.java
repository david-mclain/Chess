package Chess;

import java.util.List;

public class Pawn extends ChessPiece {
	
	private boolean firstMove;
	
	public Pawn(int x, int y, boolean color) {
		super(x, y, color);
		firstMove = true;
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}
	
}
