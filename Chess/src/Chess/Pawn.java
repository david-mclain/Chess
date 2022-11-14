package Chess;

import java.util.List;

public class Pawn extends ChessPiece {
	
	public Pawn(int x, int y, boolean color) {
		super(x, y, color);
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}
	
}
