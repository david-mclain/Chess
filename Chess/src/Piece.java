import java.util.HashSet;
import java.util.Iterator;

/*
 * File: Piece.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class represents a Piece in our game of Chess.
 * Pieces have a position, color, name, image, and a set of legal
 * moves they can execute.
 */
public abstract class Piece {
	protected int row;
	protected int col;
	protected Side color;
	protected PieceType name;
	protected String icon;
	protected HashSet<Move> legalMoves;
	/**
	 * Constructor for Piece. Assigned color and starting position.
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
	 * @return row
	 */
	public int getRow() {
		return row;
	}
	/**
	 * Getter for col
	 * @return col
	 */
	public int getCol() {
		return col;
	}
	/**
	 * Getter for color
	 * @return color
	 */
	public Side getColor() {
		return color;
	}
	/**
	 * Getter for name, which represents the type of the Piece
	 * @return name
	 */
	public PieceType getName() {
		return name;
	}
	/**
	 * Getter for image
	 * @return image
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * Getter for legalMoves
	 * @return legalMoves
	 */
	public HashSet<Move> getLegalMoves() {
		return legalMoves;
	}
	/**
	 * Getter for the move with the specified row and column
	 * @param moveRow
	 * @param moveCol
	 * @return a move
	 */
	public Move getMove(int moveRow, int moveCol) {
		for (Move move : legalMoves) {
			if (move.getRow() == moveRow && move.getCol() == moveCol)
				return move;
		}
		return null;
	}
	/**
	 * Checks if the Piece is allowed to move to the specified square
	 * @param moveToCheck
	 * @return true if legal, false if not
	 */
	public boolean checkIfLegalMove(int moveRow, int moveCol) {
		for (Move move : legalMoves) {
			if (move.getRow() == moveRow && move.getCol() == moveCol)
				return true;
		}
		return false;
	}
	/**
	 * Removes moves from the Piece's legal moves that leave the player in check
	 * @param board
	 */
	public void filterLegalMoves(Board board) {
		CheckChecker cc = CheckChecker.getInstance(board);
		Iterator<Move> moveIter = legalMoves.iterator();
		while (moveIter.hasNext()) {
			Move move = moveIter.next();
			if (!cc.isSafeMove(this, move))
				moveIter.remove();
		}
	}
	/**
	 * Changes the Piece's position based on the specified move
	 * @param move
	 */
	public void move(Move move) {
		this.row = move.getRow();
		this.col = move.getCol();
	}
	/**
	 * Changes the Piece's position based on the specified row and column
	 * @param move
	 */
	public void move(int moveRow, int moveCol) {
		this.row = moveRow;
		this.col = moveCol;
	}
	/**
	 * Checks if the specified square is in bounds
	 * @param row
	 * @param col
	 * @return true if in bounds, false if not
	 */
	protected boolean inBounds(int row, int col) {
		return row <= 7 && col <= 7 && row >= 0 && col >= 0;
	}
	/**
	 * Converts the Piece's position to algebraic chess notation for printing
	 * @return the Piece's position as a String
	 */
	protected String pos() {
		return (char) (col + 65) + "" + (8 - row);
	}
	/**
	 * Generates all legal moves the Piece can make
	 * @param board
	 */
	public abstract void updateLegalMoves(Board board);
	/**
	 * Getter for the Piece's unicode character
	 * @return a unicode character
	 */
	public abstract String getString();
}
