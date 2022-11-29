/**
 * This class represents a Rook Piece in our game of Chess. Rooks move
 * orthogonally and can move any number of squares.
 */
public class Rook extends Piece {

	/**
	 * Constructor for Rook. Assigns color and starting position.
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	public Rook(int row, int col, Side color) {
		super(row, col, color);
		name = PieceType.ROOK;
		if (color == Side.BLACK) {
			image = "src/rook_black.png";
		} else {
			image = "src/rook_white.png";
		}
	}

	/**
	 * Generates all legal moves the Rook can make. For each of the four orthogonal
	 * directions, we search outwards from the Rook's current position, stopping
	 * once we are out of bounds. If we hit an unoccupied square we add it to our
	 * legal moves. If we hit a square with a Piece on it, we add it to our legal
	 * moves if the Piece is the opposite color of the Rook, then we stop searching
	 * since we can't move through other Pieces.
	 * 
	 * @param board
	 */
	public void updateLegalMoves(Board board) {
		legalMoves .clear();
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row + i, col))
				break;
			if (!board.hasPiece(row + i, col)) {
				legalMoves.add(new Move(row + i, col));
			} else {
				if (board.getPiece(row + i, col).getColor() != color)
					legalMoves.add(new Move(row + i, col));
				break;
			}
		}
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row - i, col))
				break;
			if (!board.hasPiece(row - i, col)) {
				legalMoves.add(new Move(row - i, col));
			} else {
				if (board.getPiece(row - i, col).getColor() != color)
					legalMoves.add(new Move(row - i, col));
				break;
			}
		}
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row, col + i))
				break;
			if (!board.hasPiece(row, col + i)) {
				legalMoves.add(new Move(row, col + i));
			} else {
				if (board.getPiece(row, col + i).getColor() != color)
					legalMoves.add(new Move(row, col + i));
				break;
			}
		}
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row, col - i))
				break;
			if (!board.hasPiece(row, col - i)) {
				legalMoves.add(new Move(row, col - i));
			} else {
				if (board.getPiece(row, col - i).getColor() != color)
					legalMoves.add(new Move(row, col - i));
				break;
			}
		}
	}

	/**
	 * Getter for the Rook's unicode character
	 * 
	 * @return a unicode character
	 */
	public String getString() {
		return color == Side.BLACK ? "\u2656" : "\u265C";
	}
	
	public String toString() {
		return "rook_" + (color == Side.BLACK ? "black" :  "white");
	}
}