package Pieces;

import java.util.Iterator;
import Games.*;

/**
 * File: King.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class represents a King Piece in our game of Chess.
 * Kings move one square in any direction, orthogonal or diagonal.
 */
public class King extends Piece {
	/**
	 * Constructor for King. Assigns color and starting position.
	 * @param row
	 * @param col
	 * @param color
	 */
	public King(int row, int col, Side color) {
		super(row, col, color);
		name = PieceType.KING;
		if (color == Side.BLACK)
			icon = "king_black.png";
		else
			icon = "king_white.png";
	}
	/**
	 * Secondary constructor for King which creates a copy of the param piece
	 * @param piece
	 */
	public King(Piece piece) {
		this(piece.getRow(), piece.getCol(), piece.getColor());
		this.legalMoves.addAll(piece.legalMoves);
	}
	/**
	 * Generates all legal moves the King can make. We iterate through offsets of 1,
	 * 0, and -1 to generate each of the eight possible one square moves. We add a
	 * square to our legal moves if it is in bounds, and is unoccupied or is
	 * occupied by a piece of the opposite color. Then we add castling which the
	 * King can only perform if it is not in check, has castling rights, and there
	 * are no pieces between it and the Rook it wants to castle with.
	 * @param board
	 */
	public void updateLegalMoves(Board board) {
		legalMoves.clear();

		// standard moves in each of the eight possible directions
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
		if (board.getCheck(color))
			return;
		if (board.getShortCastle(color)) {
			if (!board.hasPiece(row, col + 1) && !board.hasPiece(row, col + 2) && board.hasPiece(row, col + 3)
					&& board.getPiece(row, col + 3).getName() == PieceType.ROOK) {
				legalMoves.add(new Move(row, col + 2, MoveType.SHORTCASTLE));
			}
		}
		if (board.getLongCastle(color)) {
			if (!board.hasPiece(row, col - 1) && !board.hasPiece(row, col - 2) && !board.hasPiece(row, col - 3)
					&& board.hasPiece(row, col - 4) && board.getPiece(row, col - 4).getName() == PieceType.ROOK) {
				legalMoves.add(new Move(row, col - 2, MoveType.LONGCASTLE));
			}
		}
	}

	/**
	 * The King needs a special implementation of filterLegalMoves to prevent the
	 * King from castling through an attacked square
	 * @param board
	 */
	public void filterLegalMoves(Board board) {
		CheckChecker cc = CheckChecker.getInstance(board);
		Iterator<Move> moveIter = legalMoves.iterator();
		boolean removeShortCastle = false;
		boolean removeLongCastle = false;
		while (moveIter.hasNext()) {
			Move move = moveIter.next();
			if (!cc.isSafeMove(this, move)) {
				if (move.getCol() == col + 1)
					removeShortCastle = true;
				if (move.getCol() == col - 1)
					removeLongCastle = true;
				moveIter.remove();
			}
		}
		if (removeShortCastle) {
			moveIter = legalMoves.iterator();
			while (moveIter.hasNext()) {
				Move move = moveIter.next();
				if (move.getRow() == row && move.getCol() == col + 2) {
					moveIter.remove();
					break;
				}
			}
		}
		if (removeLongCastle) {
			moveIter = legalMoves.iterator();
			while (moveIter.hasNext()) {
				Move move = moveIter.next();
				if (move.getRow() == row && move.getCol() == col - 2) {
					moveIter.remove();
					break;
				}
			}
		}
	}

	/**
	 * Getter for the King's unicode character
	 * @return a unicode character
	 */
	public String getString() {
		return color == Side.BLACK ? "\u2654" : "\u265A";
	}

	/**
	 * Returns as the King as a String with the King's color
	 * @return the King as a String
	 */
	public String toString() {
		return "king_" + (color == Side.BLACK ? "black" : "white");
	}
}
