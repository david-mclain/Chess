/*
 * File: FEN.java
 * Author: Luke Niemann
 * Contributors: Luke Niemann, Adrian Moore
 * Description: This class is used for converting to and from FEN (Forsyth–Edwards Notation)
 * the definition of this notation can be found here: 
 * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
 * 
 */

public class FEN {

	/**
	 * toBoard converts a FEN string into a board but ignores the arguments on the
	 * end
	 * 
	 * @param String: fen
	 * @return Piece[][] new Board object
	 */
	public static Piece[][] toBoard(String fen) {
		String[] fenArr = fen.split("/");
		// Separate placement data from the other args
		String[] args = fenArr[fenArr.length - 1].split(" ");
		fenArr[fenArr.length - 1] = args[0];

		// loop through each part of placement data to determine piece locations
		Piece[][] newBoard = new Piece[8][8];
		for (int i = 0; i < fenArr.length; i++) {
			// s represents a column
			char[] s = fenArr[i].toCharArray();
			int pos = 0;
			for (int j = 0; j < s.length; j++) {
				char c = s[j];
				// add white space for digits (-1 to account for increment at end)
				if (Character.isDigit(c)) {
					pos += Character.getNumericValue(c) - 1;
				} else {
					switch (c) {
					case 'p':
						newBoard[i][pos] = new Pawn(i, pos, Side.BLACK);
						break;
					case 'n':
						newBoard[i][pos] = new Knight(i, pos, Side.BLACK);
						break;
					case 'b':
						newBoard[i][pos] = new Bishop(i, pos, Side.BLACK);
						break;
					case 'r':
						newBoard[i][pos] = new Rook(i, pos, Side.BLACK);
						break;
					case 'q':
						newBoard[i][pos] = new Queen(i, pos, Side.BLACK);
						break;
					case 'k':
						newBoard[i][pos] = new King(i, pos, Side.BLACK);
						break;
					case 'P':
						newBoard[i][pos] = new Pawn(i, pos, Side.WHITE);
						break;
					case 'N':
						newBoard[i][pos] = new Knight(i, pos, Side.WHITE);
						break;
					case 'B':
						newBoard[i][pos] = new Bishop(i, pos, Side.WHITE);
						break;
					case 'R':
						newBoard[i][pos] = new Rook(i, pos, Side.WHITE);
						break;
					case 'Q':
						newBoard[i][pos] = new Queen(i, pos, Side.WHITE);
						break;
					case 'K':
						newBoard[i][pos] = new King(i, pos, Side.WHITE);
						break;
					}
				}
				pos += 1;
			}
		}
		return newBoard;

	}

	/**
	 * toFen converts a board into a FEN string. Does not contain arguments since
	 * Board does not
	 * 
	 * @param Piece[][]: board
	 * @return String: fen
	 */
	public static String toFEN(Board board) {
		String fenString = "";
		Piece[][] oldBoard = board.getBoard();
		for (int i = 0; i < 8; i++) {
			int spaceCount = 0;
			for (int j = 0; j < 8; j++) {
				// if empty add to space count else check names
				if (oldBoard[i][j] == null) {
					spaceCount += 1;
					// corner case if end of line and empty
					if (j == 7) {
						fenString += Integer.toString(spaceCount);
					}
				} else {
					PieceType p = oldBoard[i][j].getName();
					// if not empty add spaces to string
					if (spaceCount > 0) {
						fenString += Integer.toString(spaceCount);
					}
					spaceCount = 0;
					switch (p) {
					case PAWN:
						if (oldBoard[i][j].getColor() == Side.WHITE) {
							fenString += "P";
						} else {
							fenString += "p";
						}
						break;
					case KNIGHT:
						if (oldBoard[i][j].getColor() == Side.WHITE) {
							fenString += "N";
						} else {
							fenString += "n";
						}
						break;
					case BISHOP:
						if (oldBoard[i][j].getColor() == Side.WHITE) {
							fenString += "B";
						} else {
							fenString += "b";
						}
						break;
					case ROOK:
						if (oldBoard[i][j].getColor() == Side.WHITE) {
							fenString += "R";
						} else {
							fenString += "r";
						}
						break;
					case QUEEN:
						if (oldBoard[i][j].getColor() == Side.WHITE) {
							fenString += "Q";
						} else {
							fenString += "q";
						}
						break;
					case KING:
						if (oldBoard[i][j].getColor() == Side.WHITE) {
							fenString += "K";
						} else {
							fenString += "k";
						}
						break;
					}
				}
			}
			// corner case if end of line do not add slash
			if (i != 7) {
				fenString += "/";
			}
		}
		return fenString;
	}

	/**
	 * Converts the param board to a FEN String, including castling rights and the
	 * en passant square
	 * 
	 * @param board
	 * @return
	 */
	public static String toFENFull(Board board) {
		String fenString = toFEN(board);
		fenString += " ";
		fenString += (board.getShortCastle(Side.WHITE)) ? "K" : "-";
		fenString += (board.getLongCastle(Side.WHITE)) ? "Q" : "-";
		fenString += (board.getShortCastle(Side.BLACK)) ? "k" : "-";
		fenString += (board.getLongCastle(Side.BLACK)) ? "q" : "-";
		fenString += " ";
		fenString += (board.getEnPassantSquare() != null) ? board.getEnPassantSquare().toString() : "-";

		return fenString;
	}
}