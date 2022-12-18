package Pieces;

import Games.*;
/*
 * File: Pawn.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class represents a Pawn Piece in our game of Chess.
 * Pawns can move one square forward and capture one square diagonally
 * forward. On the first move a Pawn can move two square forward, but it
 * is then vulnerable to being captured en passant for one turn. Pawns can
 * be promoted to another piece when they reach the opposite side of the Board.
 */
public class Pawn extends Piece {
	/**
	 * Constructor for Pawn. Assigns color and starting position.
	 * @param row
	 * @param col
	 * @param color
	 */
	public Pawn(int row, int col, Side color) {
		super(row, col, color);
		name = PieceType.PAWN;
		if (color == Side.BLACK)
			icon = "pawn_black.png";
		else
			icon = "pawn_white.png";
	}
	/**
	 * Secondary constructor for Pawn which creates a copy of the param piece
	 * @param piece
	 */
	public Pawn(Piece piece) {
		this(piece.getRow(), piece.getCol(), piece.getColor());
		this.legalMoves.addAll(piece.legalMoves);
	}
	/**
	 * Generates all legal moves the Pawn can make. We have different checks for
	 * each color because Pawns can only move forward. We add a square to our legal
	 * moves if it is in bounds, unoccupied (for forward moves) or is occupied by a
	 * piece of the opposite color (for diagonal moves). En passant needs to be added
	 * @param board
	 */
	public void updateLegalMoves(Board board) {
		legalMoves.clear();
		Move enPassant = board.getEnPassantSquare();
		int i = (color == Side.BLACK) ? 1 : -1;

		// single square forward
		if (inBounds(row + i, col) && !board.hasPiece(row + i, col))
			legalMoves.add(new Move(row + i, col));

		// double square forward on first move
		if (inBounds(row + (2 * i), col) && !board.hasPiece(row + i, col) && !board.hasPiece(row + (2 * i), col)
				&& ((color == Side.WHITE && row == 6) || (color == Side.BLACK && row == 1)))
			legalMoves.add(new Move(row + (2 * i), col, MoveType.DOUBLEMOVE));

		// diagonal and en passant captures
		for (int j : new int[] { 1, -1 }) {
			if (!inBounds(row + i, col + j))
				continue;
			if (board.hasPiece(row + i, col + j) && board.getPiece(row + i, col + j).getColor() != color)
				legalMoves.add(new Move(row + i, col + j, MoveType.CAPTURE));
			else if (enPassant != null && enPassant.getRow() == row + i && enPassant.getCol() == col + j
					&& board.hasPiece(row, col + j) && board.getPiece(row, col + j).getColor() != color
					&& board.getPiece(row, col + j).getName() == PieceType.PAWN)
				legalMoves.add(enPassant);
		}
	}
	/**
	 * Returns whether the Pawn can be promoted to another non-King piece
	 * @return if the Pawn has reached the opposite side of the board
	 */
	public boolean isPromoteable() {
		return ((getColor() == Side.WHITE && row == 0) || (getColor() == Side.BLACK && row == 7));
	}
	/**
	 * Getter for the Pawn's unicode character
	 * @return a unicode character
	 */
	public String getString() {
		return color == Side.BLACK ? "\u2659" : "\u265F";
	}
	/**
	 * Returns as the Pawn as a String with the Pawn's color
	 * @return the Pawn as a String
	 */
	public String toString() {
		return "pawn_" + (color == Side.BLACK ? "black" : "white");
	}
}
