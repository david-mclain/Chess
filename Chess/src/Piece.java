import java.util.HashSet;

/**
 * This class represents a Piece in our game of Chess. Pieces have a position,
 * color, name, image, and a set of legal moves they can execute.
 */
public abstract class Piece {

	protected int row;
	protected int col;
	protected Side color;
	protected PieceType name;
	protected boolean hasMoved;
	protected HashSet<Move> legalMoves;
	protected String image;

	/**
	 * Constructor for Piece. Assigned color and starting position.
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	public Piece(int row, int col, Side color) {
		this.row = row;
		this.col = col;
		this.color = color;
		this.legalMoves = new HashSet<Move>();
	}

	/**
	 * Getter for row
	 * 
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Getter for col
	 * 
	 * @return col
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Getter for color
	 * 
	 * @return color
	 */
	public Side getColor() {
		return color;
	}

	/**
	 * Getter for name
	 * 
	 * @return name
	 */
	public PieceType getName() {
		return name;
	}

	/**
	 * Getter for hasMoved
	 * 
	 * @return hasMoved
	 */
	public boolean getHasMoved() {
		return hasMoved;
	}

	/**
	 * Getter for image
	 * 
	 * @return image
	 */
	public String getIcon() {
		return image;
	}

	/**
	 * Getter for legalMoves
	 * 
	 * @return legalMoves
	 */
	public HashSet<Move> getLegalMoves() {
		return legalMoves;
	}

	/**
	 * Getter for the move with the specified row and column
	 * 
	 * @param rowToCheck
	 * @param colToCheck
	 * @return a move
	 */
	public Move getMove(int rowToCheck, int colToCheck) {
		for (Move move : legalMoves) {
			if (move.getRow() == rowToCheck && move.getCol() == colToCheck)
				return move;
		}
		return null;
	}

	/**
	 * Checks if the Piece is allowed to move to the specified square
	 * legalMoves.contains(moveToCheck) didn't work, it's probably comparing
	 * references or something
	 * 
	 * @param moveToCheck
	 * @return true if legal, false if not
	 */
	public boolean checkIfLegalMove(int rowToCheck, int colToCheck) {
		for (Move move : legalMoves) {
			if (move.getRow() == rowToCheck && move.getCol() == colToCheck)
				return true;
		}
		return false;
	}

	/**
	 * Changes the Piece's position based on the specified move
	 * 
	 * @param move
	 */
	public void move(Move move) {
		this.row = move.getRow();
		this.col = move.getCol();
		hasMoved = true;
	}

	/**
	 * Checks if the specified square is in bounds
	 * 
	 * @param row
	 * @param col
	 * @return true if in bounds, false if not
	 */
	protected boolean inBounds(int row, int col) {
		return row <= 7 && col <= 7 && row >= 0 && col >= 0;
	}

	/**
	 * Generates all legal moves the Piece can make
	 * 
	 * @param board
	 */
	public abstract void updateLegalMoves(Board board);

	public boolean causesCheck(Move move, Board board) {
		return false;
	}

	/**
	 * Getter for the Piece's unicode character
	 * 
	 * @return a unicode character
	 */
	public abstract String getString();
}