/**
 * This class represents a Pawn Piece in our game of Chess. Pawns can move one
 * square forward and capture one square diagonally forward. On the first move a
 * Pawn can move two square forward, but it is then vulnerable to being captured
 * en passant for one turn. Pawns can be promoted to another piece when they
 * reach the opposite side of the Board.
 */
public class Pawn extends Piece {

	private boolean enPassantable;

	/**
	 * Constructor for Pawn. Assigns color and starting position.
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	public Pawn(int row, int col, Side color) {
		super(row, col, color);
		name = PieceType.PAWN;
		enPassantable = false;
		if (color == Side.BLACK) {
			image = "src/pawn_black.png";
		} else {
			image = "src/pawn_white.png";
		}
	}

	/**
	 * Generates all legal moves the Pawn can make. We have different checks for
	 * each color because Pawns can only move forward. We add a square to our legal
	 * moves if it is in bounds, unoccupied (for forward moves) or is occupied by a
	 * piece of the opposite color (for diagonal moves). En passant needs to be
	 * added
	 * 
	 * @param board
	 */
	public void updateLegalMoves(Board board) {
		legalMoves.clear();
		int i = (color == Side.BLACK) ? 1 : -1; 
		// single square forward
		if (inBounds(row + i, col) && !board.hasPiece(row + i, col))
			legalMoves.add(new Move(row + i, col));
		// diagonal captures
		if (inBounds(row + i, col + 1) && board.hasPiece(row + i, col + 1)
				&& board.getPiece(row + i, col + 1).getColor() != color)
			legalMoves.add(new Move(row + i, col + 1, MoveType.CAPTURE));
		if (inBounds(row + i, col - 1) && board.hasPiece(row + i, col - 1)
				&& board.getPiece(row + i, col - 1).getColor() != color)
			legalMoves.add(new Move(row + i, col - 1, MoveType.CAPTURE));
		// double square forward on first move
		if (inBounds(row + (2 * i), col) && !board.hasPiece(row + i, col) && !board.hasPiece(row + (2 * i), col) && !hasMoved) {
			legalMoves.add(new Move(row + (2 * i), col));
			enPassantable = true;
		}
		// en passant
		if (inBounds(row + i, col + 1) && !board.hasPiece(row + i, col + 1) && inBounds(row, col + 1)
				&& board.hasPiece(row, col + 1) && board.getPiece(row, col + 1).getColor() != color
				&& board.getPiece(row, col + 1).getName() == PieceType.PAWN) {
			Pawn pawn = (Pawn) board.getPiece(row, col + 1);
			if (pawn.isEnPassantable())
				legalMoves.add(new Move(row + i, col + 1, MoveType.ENPASSANT));
		}
		if (inBounds(row + i, col - 1) && !board.hasPiece(row + i, col - 1) && inBounds(row, col - 1)
				&& board.hasPiece(row, col - 1) && board.getPiece(row, col - 1).getColor() != color
				&& board.getPiece(row, col - 1).getName() == PieceType.PAWN) {
			Pawn pawn = (Pawn) board.getPiece(row, col - 1);
			if (pawn.isEnPassantable())
				legalMoves.add(new Move(row + i, col - 1, MoveType.ENPASSANT));
		}

		// need promotion
	}

	/**
	 * Getter for enPassantable
	 * 
	 * @return enPassantable
	 */
	public boolean isEnPassantable() {
		return enPassantable;
	}

	/**
	 * Sets enPassantable to false
	 */
	public void resetEnPassantable() {
		enPassantable = false;
	}

	/**
	 * Getter for the Pawn's unicode character
	 * 
	 * @return a unicode character
	 */
	public String getString() {
		return color == Side.BLACK ? "\u2659" : "\u265F";
	}
	
	public String toString() {
		return "pawn_" + (color == Side.BLACK ? "black" :  "white");
	}
}