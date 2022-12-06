
public class CheckBoard extends Board {

	public CheckBoard(Board board) {
		super(board);
	}

	/**
	 * Executes the specified move on the specified Piece. If the move is a capture,
	 * the captured Piece gets added to its corresponding array. If the move is a
	 * castle, the corresponding Rook gets moved. If the move is an en passant, the
	 * corresponding Pawn get captured.
	 * @param piece
	 * @param move
	 */
	public void executeMove(Piece piece, Move move) {
		handleSpecialMove(piece, move);
		board[piece.getRow()][piece.getCol()] = null;
		board[move.getRow()][move.getCol()] = piece;
		piece.move(move);
		updateInCheck();
	}

	
	
}
