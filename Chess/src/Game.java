import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class containing the board, not used yet, maybe should contain Players or
 * listeners for Players
 */
public abstract class Game {

	protected Board board;
	private Player white;
	private Player black;
	private Player currentPlayer;
	private boolean gameOver;
	

	public Game() {
		board = new Board();
		white = new Player(Side.WHITE);
		black = new Player(Side.BLACK);
		gameOver = false;
		currentPlayer = white;
	}
	
	public Game(List<String> toLoad) {
		try {
			board = new Board(toLoad);
		} catch (IOException e) {
			e.printStackTrace();
		}
		white = new Player(Side.WHITE);
		black = new Player(Side.BLACK);
		gameOver = false;
		currentPlayer = white;
	}

	public Player getWhitePlayer() {
		return white;
	}

	public void setWhitePlayer(Player player) {
		this.white = player;
	}
	
	public Player getBlackPlayer() {
		return black;
	}
	
	public void setBlackPlayer(Player player) {
		this.black = player;
	}


	public Player getCurrentPlayer() {
		return currentPlayer;
	}


	public void swapCurrentPlayer() {
		if (this.currentPlayer == getWhitePlayer()) {
			this.currentPlayer = getBlackPlayer();
		} else {
			this.currentPlayer = getWhitePlayer();
		}
	}

	public Board getBoard() {
		return board;
	}

	public Piece selectPiece(int i, int j) throws Exception {
		if (!board.hasPiece(i, j)) {
			throw new Exception();		
		}
		Piece curPiece = board.getPiece(i, j);
		curPiece.updateLegalMoves(board);
		if (curPiece.getColor() != currentPlayer.getColor() || curPiece.getLegalMoves().isEmpty()) {
			throw new Exception();
		}
		return curPiece;
	}

	
	public Set<Move> getLegalMoves(Piece selectedPiece) {
		if (selectedPiece != null) {
			selectedPiece.updateLegalMoves(board);
			return selectedPiece.getLegalMoves();
		} else {
			return new HashSet<Move>();
		}
	}

	public void movePiece(Piece selectedPiece, int i, int j) throws Exception {
		if (selectedPiece != null) {
			Move move = new Move(i, j);
			if (selectedPiece.checkIfLegalMove(i, j)) {
				board.executeMove(selectedPiece, new Move(i, j));
				Set<Piece> checkingPieces = getCheckingPieces();
				if (!checkingPieces.isEmpty()) {
					
				}
				swapCurrentPlayer();
			}
			else {
				throw new Exception();
			}
		
		}
	}

	public Set<Piece> getCheckingPieces() {
		HashSet<Piece> checkingPieces = new HashSet<Piece>();
		Map<Piece, HashSet<Move>> allLegalMoves = board.getLegalMoves(currentPlayer.getColor());
		for (Entry<Piece, HashSet<Move>> entry : allLegalMoves.entrySet()) {
			Set<Move> legalMoves = entry.getValue();
			for (Move m : legalMoves) {
				int row = m.getRow();
				int col = m.getCol();
				if (board.hasPiece(row, col)) {
					Piece targetPiece = board.getPiece(row, col);
					if (targetPiece instanceof King) {
						checkingPieces.add(entry.getKey());
					}
				}
			}
		}	
		return checkingPieces;
	}
	
	
	public boolean isGameOver() {
		return gameOver;
	}

	public Piece getPiece(int i, int j) {
		return board.getPiece(i, j);
	}

	public boolean hasPiece(int i, int j) {
		return board.hasPiece(i, j);
	}
	
	public List<String> saveBoard() {
		List<String> toSave = new ArrayList<>();
		for (int i = 0; i < 64; i++) {
			
		}
		return toSave;
	}
	
}