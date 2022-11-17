
public class Board {
	private ChessPiece[][] board;
	
	public Board() {
		board = new ChessPiece[8][8];
		board[0][0] = new Rook(0, 0, true);
		board[0][1] = new Knight(0, 1, true);
		board[0][2] = new Bishop(0, 2, true);
		board[0][3] = new Queen(0, 3, true);
		board[0][4] = new King(0, 4, true);
		board[0][5] = new Bishop(0, 2, true);
		board[0][6] = new Knight(0, 1, true);
		board[0][7] = new Rook(0, 0, true);
		board[7][0] = new Rook(0, 0, false);
		board[7][1] = new Knight(0, 1, false);
		board[7][2] = new Bishop(0, 2, false);
		board[7][3] = new Queen(0, 3, false);
		board[7][4] = new King(0, 4, false);
		board[7][5] = new Bishop(0, 2, false);
		board[7][6] = new Knight(0, 1, false);
		board[7][7] = new Rook(0, 0, false);
		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn(1, i, true);
			board[6][i] = new Pawn(6, i, false);
		}
	}
	
	public ChessPiece[][] getBoard() {
		return board;
	}
	
	public boolean hasPiece(int row, int col) {
		return board[row][col] != null;
	}
	
	public ChessPiece getPiece(int row, int col) {
		return board[row][col] == null ? null : board[row][col];
	}
}
