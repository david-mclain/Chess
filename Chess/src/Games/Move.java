package Games;
/*
 * File: Move.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class represents a Move in our game of Chess.
 * The Move contains the row and column of the target square of the
 * moving piece, and a MoveType to identify special moves like capturing,
 * castling, and en passant.
 */
public class Move {
	private int row;
	private int col;
	private MoveType type;
	/**
	 * Constructor for Move. Default MoveType is NORMAL.
	 * @param row
	 * @param col
	 */
	public Move(int row, int col) {
		this.row = row;
		this.col = col;
		this.type = MoveType.NORMAL;
	}
	/**
	 * Secondary constructor for Move which accepts a MoveType.
	 * @param row
	 * @param col
	 * @param type
	 */
	public Move(int row, int col, MoveType type) {
		this.row = row;
		this.col = col;
		this.type = type;
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
	 * Getter for type
	 * @return type
	 */
	public MoveType getType() {
		return type;
	}
	/**
	 * Checks if the Move is in bounds
	 * @return true if in bounds, false if not
	 */
	public boolean inBounds() {
		return (row >= 0 && col < 8 && row >= 0 && col < 8);
	}
	/**
	 * Converts the Move to algebraic chess notation for printing
	 * @return the Move as a String
	 */
	public String toString() {
		return (char) (col + 65) + "" + (8 - row);
	}
}
