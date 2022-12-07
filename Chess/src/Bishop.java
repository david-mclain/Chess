/**
 * @author Adrian Moore
 * 
 *         Contributors: Martin Cox
 * 
 *         This class represents a Bishop Piece in our game of Chess. Bishops
 *         move diagonally and can move any number of squares.
 */
public class Bishop extends Piece {

	/**
	 * Constructor for Bishop. Assigns color and starting position.
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	public Bishop(int row, int col, Side color) {
		super(row, col, color);
		name = PieceType.BISHOP;
		if (color == Side.BLACK)
			icon = "src/bishop_black.png";
		else
			icon = "src/bishop_white.png";
	}

	/**
	 * Secondary constructor for Bishop which creates a copy of the param Piece
	 * 
	 * @param piece
	 */
	public Bishop(Piece piece) {
		this(piece.getRow(), piece.getCol(), piece.getColor());
		this.legalMoves.addAll(piece.legalMoves);
	}

	/**
	 * Generates all legal moves the Bishop can make. For each of the four diagonal
	 * directions, we search outwards from the Bishop's current position, stopping
	 * once we are out of bounds. If we hit an unoccupied square we add it to our
	 * legal moves. If we hit a square with a Piece on it, we add it to our legal
	 * moves if the Piece is the opposite color of the Bishop, then we stop
	 * searching since we can't move through other Pieces.
	 * 
	 * @param board
	 */
	public void updateLegalMoves(Board board) {
		legalMoves.clear();
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
	 * Getter for the Bishop's unicode character
	 * 
	 * @return a unicode character
	 */
	public String getString() {
		return color == Side.BLACK ? "\u2657" : "\u265D";
	}

	/**
	 * Returns as the Bishop as a String with the Bishop's color
	 * 
	 * @return the Bishop as a String
	 */
	public String toString() {
		return "bishop_" + (color == Side.BLACK ? "black" : "white");
	}
}