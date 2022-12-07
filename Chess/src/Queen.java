/**
 * 
 * @author Adrian Moore
 * 
 *         Contributors: Martin Cox
 * 
 *         This class represents a Queen Piece in our game of Chess. Queens can
 *         move orthogonally and diagonally and can move any number of squares.
 */
public class Queen extends Piece {

	/**
	 * Constructor for Queen. Assigns color and starting position.
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	public Queen(int row, int col, Side color) {
		super(row, col, color);
		name = PieceType.QUEEN;
		if (color == Side.BLACK)
			icon = "src/queen_black.png";
		else
			icon = "src/queen_white.png";
	}

	/**
	 * Secondary constructor for Queen which creates a copy of the param piece
	 * 
	 * @param piece
	 */
	public Queen(Piece piece) {
		this(piece.getRow(), piece.getCol(), piece.getColor());
		this.legalMoves.addAll(piece.legalMoves);
	}

	/**
	 * Generates all legal moves the Queen can make. For each of the four orthogonal
	 * and diagonal directions, we search outwards from the Queen's current
	 * position, stopping once we are out of bounds. If we hit an unoccupied square
	 * we add it to our legal moves. If we hit a square with a Piece on it, we add
	 * it to our legal moves if the Piece is the opposite color of the Queen, then
	 * we stop searching since we can't move through other Pieces.
	 * 
	 * @param board
	 */
	public void updateLegalMoves(Board board) {
		legalMoves.clear();

		// first, we check the orthogonal directions (like a Rook)
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row + i, col))
				break;
			if (!board.hasPiece(row + i, col)) {
				legalMoves.add(new Move(row + i, col));
			} else {
				if (board.getPiece(row + i, col).getColor() != color)
					legalMoves.add(new Move(row + i, col, MoveType.CAPTURE));
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
					legalMoves.add(new Move(row - i, col, MoveType.CAPTURE));
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
					legalMoves.add(new Move(row, col + i, MoveType.CAPTURE));
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
					legalMoves.add(new Move(row, col - i, MoveType.CAPTURE));
				break;
			}
		}

		// the, we check the diagonal directions (like a Bishop)
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row + i, col + i))
				break;
			if (!board.hasPiece(row + i, col + i)) {
				legalMoves.add(new Move(row + i, col + i));
			} else {
				if (board.getPiece(row + i, col + i).getColor() != color)
					legalMoves.add(new Move(row + i, col + i, MoveType.CAPTURE));
				break;
			}
		}
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row + i, col - i))
				break;
			if (!board.hasPiece(row + i, col - i)) {
				legalMoves.add(new Move(row + i, col - i));
			} else {
				if (board.getPiece(row + i, col - i).getColor() != color)
					legalMoves.add(new Move(row + i, col - i, MoveType.CAPTURE));
				break;
			}
		}
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row - i, col + i))
				break;
			if (!board.hasPiece(row - i, col + i)) {
				legalMoves.add(new Move(row - i, col + i));
			} else {
				if (board.getPiece(row - i, col + i).getColor() != color)
					legalMoves.add(new Move(row - i, col + i, MoveType.CAPTURE));
				break;
			}
		}
		for (int i = 1; i < 8; i++) {
			if (!inBounds(row - i, col - i))
				break;
			if (!board.hasPiece(row - i, col - i)) {
				legalMoves.add(new Move(row - i, col - i));
			} else {
				if (board.getPiece(row - i, col - i).getColor() != color)
					legalMoves.add(new Move(row - i, col - i, MoveType.CAPTURE));
				break;
			}
		}
	}

	/**
	 * Getter for the Queen's unicode character
	 * 
	 * @return a unicode character
	 */
	public String getString() {
		return color == Side.BLACK ? "\u2655" : "\u265B";
	}

	/**
	 * Returns as the Queen as a String with the Queen's color
	 * 
	 * @return the Queen as a String
	 */
	public String toString() {
		return "queen_" + (color == Side.BLACK ? "black" : "white");
	}
}