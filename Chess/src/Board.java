import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This class represents a Chess board in our game of Chess which is stored as a
 * 2D Piece array.
 */
public class Board {
	private Piece[][] board;
	private HashSet<Piece> capturedBlackPieces;
	private HashSet<Piece> capturedWhitePieces;
	private boolean blackInCheck;
	private boolean whiteInCheck;

	/**
	 * Constructor for Board. Places all the pieces on their starting positions.
	 */
	public Board() {
		board = new Piece[8][8];
		board[0][0] = new Rook(0, 0, Side.BLACK);
		board[0][1] = new Knight(0, 1, Side.BLACK);
		board[0][2] = new Bishop(0, 2, Side.BLACK);
		board[0][3] = new Queen(0, 3, Side.BLACK);
		board[0][4] = new King(0, 4, Side.BLACK);
		board[0][5] = new Bishop(0, 5, Side.BLACK);
		board[0][6] = new Knight(0, 6, Side.BLACK);
		board[0][7] = new Rook(0, 7, Side.BLACK);
		board[7][0] = new Rook(7, 0, Side.WHITE);
		board[7][1] = new Knight(7, 1, Side.WHITE);
		board[7][2] = new Bishop(7, 2, Side.WHITE);
		board[7][3] = new Queen(7, 3, Side.WHITE);
		board[7][4] = new King(7, 4, Side.WHITE);
		board[7][5] = new Bishop(7, 5, Side.WHITE);
		board[7][6] = new Knight(7, 6, Side.WHITE);
		board[7][7] = new Rook(7, 7, Side.WHITE);
		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn(1, i, Side.BLACK);
			board[6][i] = new Pawn(6, i, Side.WHITE);
		}
		capturedBlackPieces = new HashSet<>();
		capturedWhitePieces = new HashSet<>();
		updateInCheck();
		updateLegalMoves();
	}

	public Board(List<String> board) throws IOException {
		this.board = new Piece[8][8];
		for (int i = 0; i < 64; i++) {
			String s = board.get(i);
			String[] cur = s.split(";");
			String[] temp = cur[0].split("_");
			if (temp[0].equals("rook"))
				this.board[Integer.parseInt(cur[1])][Integer.parseInt(cur[2])] = new Rook(Integer.parseInt(cur[1]), Integer.parseInt(cur[2]), temp[1].equals("white") ? Side.WHITE : Side.BLACK);
			else if (temp[0].equals("knight"))
				this.board[Integer.parseInt(cur[1])][Integer.parseInt(cur[2])] = new Knight(Integer.parseInt(cur[1]), Integer.parseInt(cur[2]), temp[1].equals("white") ? Side.WHITE : Side.BLACK);
			else if (temp[0].equals("bishop"))
				this.board[Integer.parseInt(cur[1])][Integer.parseInt(cur[2])] = new Bishop(Integer.parseInt(cur[1]), Integer.parseInt(cur[2]), temp[1].equals("white") ? Side.WHITE : Side.BLACK);
			else if (temp[0].equals("queen"))
				this.board[Integer.parseInt(cur[1])][Integer.parseInt(cur[2])] = new Queen(Integer.parseInt(cur[1]), Integer.parseInt(cur[2]), temp[1].equals("white") ? Side.WHITE : Side.BLACK);
			else if (temp[0].equals("king"))
				this.board[Integer.parseInt(cur[1])][Integer.parseInt(cur[2])] = new King(Integer.parseInt(cur[1]), Integer.parseInt(cur[2]), temp[1].equals("white") ? Side.WHITE : Side.BLACK);
			else if (temp[0].equals("pawn"))
				this.board[Integer.parseInt(cur[1])][Integer.parseInt(cur[2])] = new Pawn(Integer.parseInt(cur[1]), Integer.parseInt(cur[2]), temp[1].equals("white") ? Side.WHITE : Side.BLACK);
			else if (temp[0].equals("null"))
				this.board[Integer.parseInt(cur[1])][Integer.parseInt(cur[2])] = null;
			else
				throw new IOException();
		}
	}
	
	/**
	 * Getter for board
	 * 
	 * @return board
	 */
	public Piece[][] getBoard() {
		return board;
	}

	/**
	 * Getter for captured pieces
	 * 
	 * @param color
	 * @return captured black or white pieces
	 */
	public HashSet<Piece> getCapturedPieces(Side color) {
		return color == Side.BLACK ? capturedBlackPieces : capturedWhitePieces;
	}

	/**
	 * Getter for whether a player is in check
	 * 
	 * @param color
	 * @return if black or white is in check
	 */
	public boolean getCheck(Side color) {
		return color == Side.BLACK ? blackInCheck : whiteInCheck;
	}

	/**
	 * Returns whether the specified square has a Piece on it
	 * 
	 * @param row
	 * @param col
	 * @return true if the square has a piece, false if not
	 */
	public boolean hasPiece(int row, int col) {
		return board[row][col] != null;
	}

	/**
	 * Returns the Piece at the specified square
	 * 
	 * @param row
	 * @param col
	 * @return Piece
	 */
	public Piece getPiece(int row, int col) {
		return board[row][col] == null ? null : board[row][col];
	}

	/**
	 * Returns all the legal moves of all the Pieces of a specified color as a
	 * HashMap. The keys are Pieces and the values are arrays of moves.
	 * 
	 * CURRENLY UNUSED, POSSIBLY SHOULD REMOVE
	 * 
	 * @param color
	 * @return a HashMap of the legal moves
	 */
	public HashMap<Piece, HashSet<Move>> getLegalMoves(Side color) {
 	HashMap<Piece, HashSet<Move>> allLegalMoves = new HashMap<>();
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece != null && piece.color == color)
					allLegalMoves.put(piece, piece.getLegalMoves());
			}
		}
		return allLegalMoves;
	}

	/**
	 * Executes the specified move on the specified Piece. If the move is a capture,
	 * the captured Piece gets added to its corresponding array. If the move is a
	 * castle, the corresponding Rook gets moved. If the move is an en passant, the
	 * corresponding Pawn get captured.
	 * 
	 * @param piece
	 * @param move
	 */
	public void executeMove(Piece piece, Move move) {
		handleSpecialMove(piece, move);
		board[piece.getRow()][piece.getCol()] = null;
		board[move.getRow()][move.getCol()] = piece;
		piece.move(move);
		promotePawn(piece);
		removeEnPassantFlags();
		updateInCheck();
		updateLegalMoves();
	}

	/**
	 * Performs auxiliary actions for each of the special move types
	 * 
	 * @param piece
	 * @param move
	 */
	private void handleSpecialMove(Piece piece, Move move) {
		if (move.getType() == MoveType.CAPTURE) {
			if (piece.getColor() == Side.WHITE)
				capturedBlackPieces.add(board[move.getRow()][move.getCol()]);
			if (piece.getColor() == Side.BLACK)
				capturedWhitePieces.add(board[move.getRow()][move.getCol()]);
		} else if (move.getType() == MoveType.SHORTCASTLE) {
			board[piece.getRow()][piece.getCol() + 1] = getPiece(piece.getRow(), piece.getCol() + 3);
			board[piece.getRow()][piece.getCol() + 3] = null;
		} else if (move.getType() == MoveType.LONGCASTLE) {
			board[piece.getRow()][piece.getCol() - 1] = getPiece(piece.getRow(), piece.getCol() - 4);
			board[piece.getRow()][piece.getCol() - 4] = null;
		} else if (move.getType() == MoveType.ENPASSANT) {
			if (piece.getColor() == Side.WHITE) {
				capturedBlackPieces.add(board[move.getRow() + 1][move.getCol()]);
				board[move.getRow() + 1][move.getCol()] = null;
			}
			if (piece.getColor() == Side.BLACK) {
				capturedWhitePieces.add(board[move.getRow() - 1][move.getCol()]);
				board[move.getRow() - 1][move.getCol()] = null;
			}
		}
	}

	/**
	 * TODO
	 * 
	 * SWAP PAWN FOR USER SPECIFIED PIECE
	 */
	private void promotePawn(Piece piece) {
		if (piece.getName() != PieceType.PAWN)
			return;
	}

	/**
	 * Removes the ability of a Pawn to be captured by en passant after one turn.
	 * Since we can't invoke a Pawn method when iterating through Pieces, we extract
	 * all the Pawns, then reset en passant on each Pawn.
	 */
	private void removeEnPassantFlags() {
		HashSet<Pawn> pawns = new HashSet<>();
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece != null && piece.getName() == PieceType.PAWN)
					pawns.add((Pawn) piece);
			}
		}
		for (Pawn pawn : pawns)
			pawn.resetEnPassantable();
	}

	/**
	 * WORK IN PROGRESS
	 * 
	 * Updates whether each player is in check
	 */
	private void updateInCheck() {
		blackInCheck = false;
		whiteInCheck = false;
	}

	/**
	 * Updates the legal moves of each Piece
	 */
	private void updateLegalMoves() {
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece != null)
					piece.updateLegalMoves(this);
			}
		}
	}
}