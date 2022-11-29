/**
 * This class represents a Knight Piece in our game of Chess. Knights move in an
 * L shape: two square in one direction and one square in another (both
 * orthogonally).
 */
public class Knight extends Piece {

	/**
	 * Constructor for Knight. Assigns color and starting position.
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	public Knight(int row, int col, Side color) {
		super(row, col, color);
		name = PieceType.KNIGHT;
		if (color == Side.BLACK) {
			image = "src/knight_black.png";
		} else {
			image = "src/knight_white.png";
		}
	}

	/**
	 * Generates all legal moves the Knight can make. We iterate through offsets of
	 * 1, -1 and 2, -2 to generate each of the eight possible moves. We add a square
	 * to our legal moves if it is in bounds, and is unoccupied or is occupied by a
	 * piece of the opposite color.
	 * 
	 * @param board
	 */
	public void updateLegalMoves(Board board) {
		legalMoves.clear();
		for (int i : new int[] { 1, -1 }) {
			for (int j : new int[] { 2, -2 }) {
				if (inBounds(row + i, col + j)) {
					if (!board.hasPiece(row + i, col + j))
						legalMoves.add(new Move(row + i, col + j));
					else if (board.getPiece(row + i, col + j).getColor() != color)
						legalMoves.add(new Move(row + i, col + j, MoveType.CAPTURE));
				}
				if (inBounds(row + j, col + i)) {
					if (!board.hasPiece(row + j, col + i))
						legalMoves.add(new Move(row + j, col + i));
					else if (board.getPiece(row + j, col + i).getColor() != color)
						legalMoves.add(new Move(row + j, col + i, MoveType.CAPTURE));
				}
			}
		}
	}

	/**
	 * Getter for the Knight's unicode character
	 * 
	 * @return a unicode character
	 */
	public String getString() {
		return color == Side.BLACK ? "\u2658" : "\u265E";
	}
	
	public String toString() {
		return "knight_" + (color == Side.BLACK ? "black" :  "white");
	}
}
