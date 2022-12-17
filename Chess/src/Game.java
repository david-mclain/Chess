import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
/*
 * File: Game.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for maintaining game state. Handles inputs
 * and outputs received from client and updates game accordingly
 */
public abstract class Game {
	protected Board board;
	protected Player white;
	protected Player black;
	protected Player currentPlayer;
	protected CheckChecker checkCheck;
	protected ArrayList<String> prevPositions;
	protected int halfMoveClock;
	protected End end;
	protected boolean hasPromotable;
	protected Piece promotable;
	/**
	 * Instantiates new game
	 */
	public Game() {
		board = new Board();
		white = new Player(Side.WHITE);
		black = new Player(Side.BLACK);
		currentPlayer = white;
		checkCheck = CheckChecker.getInstance(board);
		prevPositions = new ArrayList<>();
		prevPositions.add(FEN.toFENFull(board));
		halfMoveClock = 0;
	}
	/**
	 * Instantiates new game with list containing information of saved game
	 * @param toLoad - information of saved game
	 */
	public Game(List<String> toLoad) {
		try {
			board = new Board(toLoad);
		} catch (IOException e) {
			e.printStackTrace();
		}
		white = new Player(Side.WHITE);
		black = new Player(Side.BLACK);
		currentPlayer = toLoad.get(1).equals("white") ? white : black;
		checkCheck = CheckChecker.getInstance(board);
		prevPositions = new ArrayList<>(Arrays.asList(toLoad.get(5).split("_")));
		halfMoveClock = Integer.parseInt(toLoad.get(6));
	}
	/**
	 * Gets the white player
	 * @return the white player
	 */
	public Player getWhitePlayer() {
		return white;
	}
	/**
	 * Sets the white player
	 * @param player the player to assign to white
	 */
	public void setWhitePlayer(Player player) {
		this.white = player;
	}
	/**
	 * Gets the black player
	 * @return the black player
	 */
	public Player getBlackPlayer() {
		return black;
	}
	/**
	 * Sets the black player
	 * @param player the black player
	 */
	public void setBlackPlayer(Player player) {
		this.black = player;
	}
	/**
	 * Returns the player whose turn it is
	 * @return the current player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	/**
	 * Swaps the current player (call at end of turn)
	 */
	public void swapCurrentPlayer() {
		if (this.currentPlayer == getWhitePlayer()) {
			this.currentPlayer = getBlackPlayer();
		} else {
			this.currentPlayer = getWhitePlayer();
		}
	}
	/**
	 * Gets the game board
	 * @return the game board
	 */
	public Board getBoard() {
		return board;
	}
	/**
	 * Gets the previous board positions
	 * @return an array of previous positions
	 */
	public ArrayList<String> getPrevPositions() {
		return prevPositions;
	}
	/**
	 * Gets the number of half move since a pawn move or a capture
	 * @return halfMoveClock
	 */
	public int getHalfMoveClock() {
		return halfMoveClock;
	}
	/**
	 * For UI - when player clicks on a piece, will make sure the piece exists, that
	 * the piece belongs to the player, and that the piece has legal moves. If all
	 * of this is true, returns the piece so the UI can access it. Otherwise, throws
	 * an exception. Currently uses generic exception
	 * @param i the row of the clicked piece
	 * @param j the column of the clicked piece
	 * @return the piece at row i, column j
	 * @throws Exception
	 */
	public Piece selectPiece(int i, int j) throws Exception {
		if (!board.hasPiece(i, j)) {
			throw new IOException();
		}
		Piece curPiece = board.getPiece(i, j);
		if (curPiece.getColor() != currentPlayer.getColor() || curPiece.getLegalMoves().isEmpty()) {
			throw new Exception();
		}
		return curPiece;
	}
	/**
	 * Gets all legal moves of the selected piece
	 * @param selectedPiece the piece to check moves
	 * @return a set of that piece's legal moves
	 */
	public List<String> getLegalMoves(Piece selectedPiece) {
		List<String> ret = new ArrayList<>();
		if (selectedPiece != null) {
			for (Move m : selectedPiece.getLegalMoves()) {
				ret.add(m.toString());
			}
		}
		return ret;
	}
	/**
	 * Handles UI move commands Checks if a move is legal. If move is legal, moves
	 * piece. Then ends turn and switches current player. Throws an exception if
	 * there is no piece selected.
	 * @param piece the Piece to move
	 * @param i - the destination row as an int
	 * @param j - the destination column as an int
	 * @throws Exception
	 */
	public void movePiece(int row, int col, int i, int j) throws Exception {
		Piece piece = board.getPiece(row, col);
		if (piece == null)
			return;
		if (piece.checkIfLegalMove(i, j)) {
			Move move = piece.getMove(i, j);
			board.executeMove(piece, move);
			if (piece instanceof Pawn) {
				Pawn p = (Pawn) piece;
				if (p.isPromoteable()) {
					clientPromote(p.getRow(), p.getCol());
				}
			}
			checkCheck.update();
			prevPositions.add(FEN.toFENFull(board));
			halfMoveClock = (piece.getName() == PieceType.PAWN || move.getType() == MoveType.CAPTURE) ? 0
					: halfMoveClock + 1;
			updateClient();
			swapCurrentPlayer();
			notifyClientCheck();
			end = isGameOver();
			if (end != null) {
				endGame(end);
			}
		} else {
			throw new Exception();
		}
		sendCaptured();
	}

	/**
	 * Promotes piece at specified location to specified piece type
	 * @param i - row of piece to promote
	 * @param j - col of piece to promote
	 * @param promotePiece - type of piece to promote to
	 */
	public void promote(int i, int j, PieceType promotePiece) {
		Piece newPiece = null;
		Side color = getCurrentPlayer().getColor() == Side.BLACK ? Side.WHITE : Side.BLACK;
		if (promotePiece == PieceType.KNIGHT) {
			newPiece = new Knight(i, j, color);
		} else if (promotePiece == PieceType.ROOK) {
			newPiece = new Rook(i, j, color);
		} else if (promotePiece == PieceType.BISHOP) {
			newPiece = new Bishop(i, j, color);
		} else {
			newPiece = new Queen(i, j, color);
		}
		board.promotePawn(newPiece);
		checkCheck.update();
		updateClient();
		notifyClientCheck();
		end = isGameOver();
		if (end != null) {
			endGame(end);
		}
	}
	/**
	 * Returns a set of all pieces that have a king in check.
	 * @return the set of all checking pieces
	 */
	public List<String> getCheckingPieces() {
		List<String> ret = new ArrayList<>();
		Set<Piece> pieces = checkCheck.getCheckingPieces();
		for (Piece piece : pieces) {
			ret.add(piece.pos());
		}
		return ret;
	}

	/**
	 * Checks if the game is over
	 * @return true if the game is over, else false
	 */
	public End isGameOver() {
		End mate = checkCheck.checkMate(currentPlayer);
		if (mate == End.CHECKMATE)
			return End.CHECKMATE;
		else if (mate == End.STALEMATE)
			return End.STALEMATE;
		String curPosition = FEN.toFENFull(board);
		int count = 0;
		for (String position : prevPositions) {
			if (curPosition.equals(position))
				count++;
		}
		if (count >= 3)
			return End.THREEFOLD;
		if (halfMoveClock >= 100)
			return End.FIFTYMOVE;
		return null;
	}
	/**
	 * Returns if game has ended
	 * @return true if game has ended false otherwise
	 */
	public boolean end() {
		return end != null;
	}

	/**
	 * Returns cause of game ending
	 * @return cause of game ending
	 */
	public String getCause() {
		return end.toString();
	}

	/**
	 * Returns the piece in the given space
	 * @param i the row as an int
	 * @param j the column as an int
	 * @return the piece at row i, col j
	 */
	public Piece getPiece(int i, int j) {
		return board.getPiece(i, j);
	}
	/**
	 * Returns true if the board has a piece in the given space, false if it is
	 * empty
	 * 
	 * @param i the row as an int
	 * @param j the column as an int
	 * @return whether the given space is occupied
	 */
	public boolean hasPiece(int i, int j) {
		return board.hasPiece(i, j);
	}
	/**
	 * Returns all captured pieces
	 * @return all captured pieces
	 */
	public List<String> getCapturedPieces() {
		return board.getCapturedPieces();
	}

	/**
	 * Ends game
	 * @param gameOver - cause of game ending
	 */
	public abstract void endGame(End gameOver);
	/**
	 * Notifies client they are in check
	 */
	public abstract void notifyClientCheck();
	/**
	 * Sends all captured pieces to client
	 */
	public abstract void sendCaptured();
	/**
	 * Sends board to client so client can update pieces
	 */
	public abstract void updateClient();
	/**
	 * Sends information for client to promote when pawn reaches other side
	 * @param i - row of pawn to promote
	 * @param j - col of pawn to promote
	 */
	public abstract void clientPromote(int i, int j);

}
