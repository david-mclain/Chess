package Games;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import Pieces.*;
/*
 * File: CheckChecker.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for evaluating which moves are legal
 * for a player to make. Player's can't make a move that would put their
 * own King in check, and if they are currently in check they can only
 * make a move which takes them out of check.
 */
public class CheckChecker {
	private static CheckChecker instance;
	private Board board;
	private Set<Piece> checkingPieces;
	private boolean whiteInCheck;
	private boolean blackInCheck;
	/**
	 * Constructor for CheckChecker
	 * @param board
	 */
	private CheckChecker(Board board) {
		this.board = board;
		checkingPieces = new HashSet<Piece>();
		whiteInCheck = false;
		blackInCheck = false;
	}
	public static CheckChecker getInstance() {
		if (instance == null) {
			instance = new CheckChecker(new Board());
		}
		return instance;
	}
	/**
	 * Returns the Check Checker's instance, creating a new one if it does not
	 * already exist
	 * @param board the board to use
	 * @return the Check Checker's instance
	 */
	public static CheckChecker getInstance(Board board) {
		if (instance == null) {
			instance = new CheckChecker(board);
		}
		return instance;
	}
	/**
	 * Forces an update of the checker. Checks each side's legal moves, then gets
	 * any pieces that have another piece in check.
	 */
	public void update() {
		whiteInCheck = false;
		blackInCheck = false;
		checkingPieces = new HashSet<Piece>();
		Map<Piece, HashSet<Move>> whiteLegalMoves = board.getLegalMoves(Side.WHITE);
		Map<Piece, HashSet<Move>> blackLegalMoves = board.getLegalMoves(Side.BLACK);
		Set<Piece> whiteChecks = findCheckingPieces(whiteLegalMoves);
		Set<Piece> blackChecks = findCheckingPieces(blackLegalMoves);
		if (!whiteChecks.isEmpty()) {
			blackInCheck = true;
			checkingPieces.addAll(whiteChecks);
		}
		if (!blackChecks.isEmpty()) {
			whiteInCheck = true;
			checkingPieces.addAll(blackChecks);
		}
	}
	/**
	 * Gets a set of checking pieces for the current board
	 * @param moves a hashmap of pieces and their legal moves
	 * @return any pieces that are checking a king
	 */
	private Set<Piece> findCheckingPieces(Map<Piece, HashSet<Move>> moves) {
		return findCheckingPieces(this.board, moves);
	}
	/**
	 * Gets a set of checking pieces for the given board For each piece, checks each
	 * move. If it has a legal move that ends on a king's space (by definition an
	 * opposing king), adds that piece to the set.
	 * @param board the board to check
	 * @param moves a hashmap of pieces and their legal moves
	 * @return any pieces that are checking a king
	 */
	private Set<Piece> findCheckingPieces(Board board, Map<Piece, HashSet<Move>> moves) {
		HashSet<Piece> checking = new HashSet<Piece>();
		for (Entry<Piece, HashSet<Move>> entry : moves.entrySet()) {
			Set<Move> legalMoves = entry.getValue();
			for (Move m : legalMoves) {
				int row = m.getRow();
				int col = m.getCol();
				if (board.hasPiece(row, col)) {
					Piece targetPiece = board.getPiece(row, col);
					if (targetPiece instanceof King) {
						checking.add(entry.getKey());
					}
				}
			}
		}
		return checking;
	}

	/**
	 * Returns the current set of checking pieces
	 * @return the current set of checking pieces
	 */
	public Set<Piece> getCheckingPieces() {
		return checkingPieces;
	}

	/**
	 * Checks if a given move will leave your own king in check Creates a copy of
	 * the board, moves the piece on that board, and checks if the friendly king is
	 * now in check. Returns true if the move is safe, false otherwise.
	 * @param p the piece to check
	 * @param m the move to check
	 * @return a boolean denoting if the move is safe
	 */
	public boolean isSafeMove(Piece p, Move m) {
		Board boardCopy = new Board(board);
		Piece pieceCopy = boardCopy.getPiece(p.getRow(), p.getCol());
		Side side = p.getColor();
		boardCopy.executeMove(pieceCopy, m);
		return !boardCopy.getCheck(side);
	}
	/**
	 * Checks if the given player is in check
	 * @param p the player to check
	 * @return the player's check status as boolean
	 */
	public boolean isInCheck(Side side) {
		return side == Side.BLACK ? blackInCheck : whiteInCheck;
	}
	/**
	 * Returns true if the white player is in check
	 * @return boolean denoting white's check status
	 */
	public boolean isWhiteInCheck() {
		return whiteInCheck;
	}
	/**
	 * Returns true if the black player is in check
	 * @return boolean denoting black's check status
	 */
	public boolean isBlackInCheck() {
		return blackInCheck;
	}
	/**
	 * Checks if a player is in checkmate. If a player is in check with no legal
	 * moves, they are in checkmate. If a player is not in check with no legal
	 * moves, it is a stalemate.
	 * @param p
	 * @return null if game is not over, if it is returns way game ended
	 */
	public End checkMate(Player p) {
		board.updateInCheck();
		HashMap<Piece, HashSet<Move>> moves = board.getLegalMoves(p.getColor());
		for (Map.Entry<Piece, HashSet<Move>> entry : moves.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				return null;
			}
		}
		return isInCheck(p.getColor()) ? End.CHECKMATE : End.STALEMATE;
	}
}
