package Chess;

import java.util.List;

public class Queen extends ChessPiece {

	public Queen(int row, int col, boolean color) {
		super(row, col, color);
	}

	@Override
	public List<List<Integer>> getValidMoves() {
		return null;
	}

}
