import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * File: Board.java
 * Author: Adrian Moore
 * Contributors: Adrian Moore, David McLain, Martin Cox
 * Description: This class represents a Chess board in our game of Chess which is stored as a
 * 2D Piece array. The Board also keeps track of each player's captured pieces,
 * whether a player is in check, each player's castling rights, and the current
 * en passant target square.
 */
public class Board {
	protected Piece[][] board;
	private ArrayList<String> capturedPieces;
	private boolean blackInCheck;
	private boolean whiteInCheck;
	private boolean blackShortCastle;
	private boolean whiteShortCastle;
	private boolean blackLongCastle;
	private boolean whiteLongCastle;
	private Move enPassantSquare;
	private boolean isCopy;

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
		isCopy = false;
		capturedPieces = new ArrayList<String>();
		blackInCheck = false;
		whiteInCheck = false;
		blackShortCastle = true;
		blackLongCastle = true;
		whiteShortCastle = true;
		whiteLongCastle = true;
		enPassantSquare = null;
		updateLegalMoves();
		updateInCheck();
		updateLegalMoves();
	}

	/**
	 * Secondary constructor for board which creates a deep copy of the param
	 * oldBoard to simulate moves for calculating checks
	 * 
	 * @param oldBoard
	 */
	public Board(Board oldBoard) {
		this.board = new Piece[8][8];
		for (Piece[] row : oldBoard.getBoard()) {
			for (Piece piece : row) {
				if (piece != null) {
					int i = piece.getRow();
					int j = piece.getCol();
					if (piece instanceof Rook)
						this.board[i][j] = new Rook(piece);
					else if (piece instanceof Knight)
						this.board[i][j] = new Knight(piece);
					else if (piece instanceof Bishop)
						this.board[i][j] = new Bishop(piece);
					else if (piece instanceof Queen)
						this.board[i][j] = new Queen(piece);
					else if (piece instanceof King)
						this.board[i][j] = new King(piece);
					else if (piece instanceof Pawn)
						this.board[i][j] = new Pawn(piece);
				}
			}
		}
		capturedPieces = new ArrayList<String>();
		blackInCheck = oldBoard.getCheck(Side.BLACK);
		whiteInCheck = oldBoard.getCheck(Side.WHITE);
		blackShortCastle = oldBoard.getShortCastle(Side.BLACK);
		blackLongCastle = oldBoard.getLongCastle(Side.BLACK);
		whiteShortCastle = oldBoard.getShortCastle(Side.WHITE);
		whiteLongCastle = oldBoard.getLongCastle(Side.WHITE);
		enPassantSquare = oldBoard.getEnPassantSquare();
		isCopy = true;
		updateLegalMoves();
		updateInCheck();
		updateLegalMoves();
	}

	/**
	 * Instantiates board with information of saved game
	 * 
	 * @param toLoad - information of saved game to load
	 * @throws IOException
	 */
	public Board(List<String> toLoad) throws IOException {
		String[] boardStr = toLoad.get(0).split(" ");
		String board = boardStr[0];
		char[] castling = boardStr[1].toCharArray();
		char[] enPassant = boardStr[2].toCharArray();
		this.board = FEN.toBoard(board);
		whiteShortCastle = (castling[0] == 'K') ? true : false;
		whiteLongCastle = (castling[1] == 'Q') ? true : false;
		blackShortCastle = (castling[2] == 'k') ? true : false;
		blackLongCastle = (castling[3] == 'q') ? true : false;
		System.out.println(enPassant);
		enPassantSquare = (enPassant[0] == '-') ? null
				: new Move(8 - (enPassant[1] - '0'), (int) enPassant[0] - 65, MoveType.ENPASSANT);
		capturedPieces = new ArrayList<String>();
		for (int i = 7; i < toLoad.size(); i++)
			capturedPieces.add(toLoad.get(i));

		isCopy = false;
		updateLegalMoves();
		updateInCheck();
		updateLegalMoves();
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
	 * @return captured pieces
	 */
	public ArrayList<String> getCapturedPieces() {
		return capturedPieces;
	}

	/**
	 * Getter for whether a player is in check
	 * 
	 * @param color
	 * @return true if the player of the param color is in check, false if not
	 */
	public boolean getCheck(Side color) {
		return color == Side.BLACK ? blackInCheck : whiteInCheck;
	}

	/**
	 * Getter for whether a player has the right to castle king-side
	 * 
	 * @param color
	 * @return true if the player can short-castle, false if not
	 */
	public boolean getShortCastle(Side color) {
		return color == Side.BLACK ? blackShortCastle : whiteShortCastle;
	}

	/**
	 * Getter for whether a player has the right to castle queen-side
	 * 
	 * @param color
	 * @return true if the player can long-castle, false if not
	 */
	public boolean getLongCastle(Side color) {
		return color == Side.BLACK ? blackLongCastle : whiteLongCastle;
	}

	/**
	 * Getter for the current en passant square, which null if the previous move was
	 * not a double square Pawn move
	 * 
	 * @return the en passant square
	 */
	public Move getEnPassantSquare() {
		return enPassantSquare;
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
	 * HashMap. The keys are Pieces and the values are sets of moves.
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
	 * Places the specified Piece (which is replacing a promoted Pawn) onto the
	 * board
	 * 
	 * @param piece - piece to promote to
	 */
	public void promotePawn(Piece piece) {
		board[piece.getRow()][piece.getCol()] = piece;
		updateLegalMoves();
		updateInCheck();
		updateLegalMoves();
	}

	/**
	 * Executes the specified move on the specified Piece.
	 * 
	 * @param piece - piece to move
	 * @param move  - position to move to
	 */
	public void executeMove(Piece piece, Move move) {
		updateEnPassantSquare(piece, move);
		updateCastlingRights(piece, move);
		handleSpecialMove(piece, move);
		board[piece.getRow()][piece.getCol()] = null;
		board[move.getRow()][move.getCol()] = piece;
		piece.move(move);
		updateLegalMoves();
		updateInCheck();
		updateLegalMoves();
	}

	/**
	 * Performs auxiliary actions for each of the special move types. If the move is
	 * a capture, the captured Piece gets added to its corresponding array. If the
	 * move is a castle, the corresponding Rook gets moved. If the move is an en
	 * passant, the corresponding Pawn get captured.
	 * 
	 * @param piece - piece to move
	 * @param move  - position to move to
	 */
	protected void handleSpecialMove(Piece piece, Move move) {
		int moveRow = move.getRow();
		int moveCol = move.getCol();
		MoveType type = move.getType();
		int pieceRow = piece.getRow();
		int pieceCol = piece.getCol();
		Side color = piece.getColor();

		if (type == MoveType.CAPTURE) {
			capturedPieces.add(board[moveRow][moveCol].toString());
		} else if (type == MoveType.SHORTCASTLE) {
			Piece rook = getPiece(pieceRow, pieceCol + 3);
			board[pieceRow][pieceCol + 3] = null;
			board[pieceRow][pieceCol + 1] = rook;
			rook.move(new Move(pieceRow, pieceCol + 1));
		} else if (type == MoveType.LONGCASTLE) {
			Piece rook = getPiece(pieceRow, pieceCol - 4);
			board[pieceRow][pieceCol - 4] = null;
			board[pieceRow][pieceCol - 1] = rook;
			rook.move(new Move(pieceRow, pieceCol - 1));
		} else if (type == MoveType.ENPASSANT) {
			if (color == Side.WHITE) {
				capturedPieces.add(board[moveRow + 1][moveCol].toString());
				board[move.getRow() + 1][move.getCol()] = null;
			}
			if (color == Side.BLACK) {
				capturedPieces.add(board[moveRow - 1][moveCol].toString());
				board[moveRow - 1][moveCol] = null;
			}
		}
	}

	/**
	 * Updates the en passant square if the current move is a Pawn double square
	 * move. The en passant square only available for one move before it is reset.
	 * 
	 * @param piece - piece to update
	 * @param move  - move being executed
	 */
	private void updateEnPassantSquare(Piece piece, Move move) {
		if (move.getType() == MoveType.DOUBLEMOVE) {
			if (piece.getColor() == Side.WHITE)
				enPassantSquare = new Move(move.getRow() + 1, move.getCol(), MoveType.ENPASSANT);
			if (piece.getColor() == Side.BLACK)
				enPassantSquare = new Move(move.getRow() - 1, move.getCol(), MoveType.ENPASSANT);
		} else {
			enPassantSquare = null;
		}
	}

	/**
	 * Updates castling rights. A player loses the right to castle if they move
	 * their King, the right to castle king-side if they move their king-side Rook
	 * or their king-side Rook is captured, and the right to castle queen-side if
	 * they move their queen-side Rook or their queen-side Rook is captured
	 * 
	 * @param piece - piece to update rights for
	 * @param move  - move to execute
	 */
	private void updateCastlingRights(Piece piece, Move move) {
		if (piece.getName() == PieceType.KING) {
			if (piece.getColor() == Side.BLACK) {
				blackShortCastle = false;
				blackLongCastle = false;
			}
			if (piece.getColor() == Side.WHITE) {
				whiteShortCastle = false;
				whiteLongCastle = false;
			}
		} else if (piece.getName() == PieceType.ROOK) {
			int pieceRow = piece.getRow();
			int pieceCol = piece.getCol();
			if (piece.getColor() == Side.BLACK) {
				if (pieceRow == 0 && pieceCol == 0)
					blackLongCastle = false;
				if (pieceRow == 0 && pieceCol == 7)
					blackShortCastle = false;
			}
			if (piece.getColor() == Side.WHITE) {
				if (pieceRow == 7 && pieceCol == 0)
					whiteLongCastle = false;
				if (pieceRow == 7 && pieceCol == 7)
					whiteShortCastle = false;
			}
		}
		if (move.getType() == MoveType.CAPTURE) {
			int moveRow = move.getRow();
			int moveCol = move.getCol();
			Piece targetPiece = board[moveRow][moveCol];
			if (targetPiece.getName() != PieceType.ROOK)
				return;
			if (targetPiece.getColor() == Side.BLACK) {
				if (moveRow == 0 && moveCol == 0)
					blackLongCastle = false;
				if (moveRow == 0 && moveCol == 7)
					blackShortCastle = false;
			}
			if (targetPiece.getColor() == Side.WHITE) {
				if (moveRow == 7 && moveCol == 0)
					whiteLongCastle = false;
				if (moveRow == 7 && moveCol == 7)
					whiteShortCastle = false;
			}
		}
	}

	/**
	 * Updates the legal moves of each Piece in the Board
	 */
	private void updateLegalMoves() {
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece == null)
					continue;
				piece.updateLegalMoves(this);
				if (!isCopy) {
					if (piece.getName() == PieceType.KING) {
						King king = (King) piece;
						king.filterLegalMoves(this);
					} else {
						piece.filterLegalMoves(this);
					}
				}
			}
		}
	}

	/**
	 * Updates whether each player is in check
	 */
	protected void updateInCheck() {
		blackInCheck = false;
		whiteInCheck = false;
		for (Piece[] boardRow : board) {
			for (Piece piece : boardRow) {
				if (piece == null)
					continue;
				Set<Move> legalMoves = piece.getLegalMoves();
				for (Move move : legalMoves) {
					int moveRow = move.getRow();
					int moveCol = move.getCol();
					if (!hasPiece(moveRow, moveCol))
						continue;
					Piece targetPiece = getPiece(moveRow, moveCol);
					if (targetPiece instanceof King && piece.getColor() != targetPiece.getColor()) {
						if (targetPiece.getColor() == Side.WHITE)
							whiteInCheck = true;
						else
							blackInCheck = true;
					}
				}
			}
		}
	}

	/**
	 * Returns string representation of current board
	 */
	public String toString() {
		String boardString = "    A   B   C   D   E   F   G   H   \n";
		boardString += "  +-------------------------------+  \n";
		for (int i = 0; i < 8; i++) {
			String cur = (i + 1) + " |";
			for (int j = 0; j < 8; j++) {
				Piece p = getPiece(i, j);
				cur = cur + ' ' + (p == null ? ' ' : p.getString()) + (j < 7 ? " |" : '\0');
			}
			cur = cur + "| " + (i + 1) + "\n";
			boardString += cur;
			if (i < 7) {
				boardString += "  |-------------------------------|  \n";
			}
		}
		boardString += "  +-------------------------------+  ";
		return boardString;
	}
}
