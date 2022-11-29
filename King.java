/**
 * This class represents a King Piece in our game of Chess. Kings move one
 * square in any direction, orthogonal or diagonal.
 */
public class King extends Piece {

	/**
	 * Constructor for King. Assigns color and starting position.
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	public King(int row, int col, Side color) {
		super(row, col, color);
		name = PieceType.KING;
		if (color == Side.BLACK) {
			image = "src/king_black.png";
		} else {
			image = "src/king_white.png";
		}
	}

	/**
	 * Generates all legal moves the King can make. We iterate through offsets of 1,
	 * 0, and -1 to generate each of the eight possible one square moves. We add a
	 * square to our legal moves if it is in bounds, and is unoccupied or is
	 * occupied by a piece of the opposite color. Castling needs to be added.
	 * 
	 * @param board
	 */
	public void updateLegalMoves(Board board) {
		legalMoves.clear();
		for (int i : new int[] { 1, 0, -1 }) {
			for (int j : new int[] { 1, 0, -1 }) {
				// we skip offset 0, 0 since the King can't move to the square it's already on
				if (!inBounds(row + i, col + j) || (i == 0 && j == 0))
					continue;
				if (!board.hasPiece(row + i, col + j))
					legalMoves.add(new Move(row + i, col + j));
				else if (board.getPiece(row + i, col + j).getColor() != color)
					legalMoves.add(new Move(row + i, col + j, MoveType.CAPTURE));
			}
		}
		// castling
		if (!hasMoved) {
			if (!board.hasPiece(row, col + 1) && !board.hasPiece(row, col + 2) && board.hasPiece(row, col + 3)
					&& board.getPiece(row, col + 3).getName() == PieceType.ROOK
					&& !board.getPiece(row, col + 3).getHasMoved()) {
				legalMoves.add(new Move(row, col + 2, MoveType.SHORTCASTLE));
			}
			if (!board.hasPiece(row, col - 1) && !board.hasPiece(row, col - 2) && !board.hasPiece(row, col - 3)
					&& board.hasPiece(row, col - 4) && board.getPiece(row, col - 4).getName() == PieceType.ROOK
					&& !board.getPiece(row, col - 4).getHasMoved()) {
				legalMoves.add(new Move(row, col - 2, MoveType.LONGCASTLE));
			}
		}
	}

	/**
	 * Getter for the King's unicode character
	 * 
	 * @return a unicode character
	 */
	public String getString() {
		return color == Side.BLACK ? "\u2654" : "\u265A";
	}
	
	public String toString() {
		return "king_" + (color == Side.BLACK ? "black" :  "white");
	}
}