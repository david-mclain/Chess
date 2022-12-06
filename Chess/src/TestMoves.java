import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
/*
 * File: TestMoves.java
 * Author: David McLain
 * Contributors: David McLain, Adrian Moore
 * Description: This class is used for testing board movements and possibilities
 * without having to create UI
 */
public class TestMoves {
	private static Board board;
	private static Scanner in;

	public static void main(String[] args) {		
		in = new Scanner(System.in);

		PrintStream out = null;
		try {
			out = new PrintStream(System.out, true, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		board = new Board();

		while (true) {
			printBoard(out);
			printAllMoves(board, out);
			out.println("Piece to find valid moves for: (Format example G7)");
			String input = in.nextLine();
			int row = Character.getNumericValue(input.charAt(1)) - 1;
			int col = input.charAt(0);
			col = col > 96 ? col - 97 : col - 65;
			out.println("row: " + (row + 1) + ", col: " + Character.toString(col + 65));
			Piece piece = board.getPiece(row, col);
			if (piece != null) {
				printPieceMoves(piece, out);
			}
			Move userMove = null;
			boolean validMove = false;
			while (!validMove) {
				out.println("Please pick one of the above moves: (Format example G7)");
				String targetInput = in.nextLine();
				int targetRow = Character.getNumericValue(targetInput.charAt(1)) - 1;
				int targetCol = targetInput.charAt(0);
				targetCol = targetCol > 96 ? targetCol - 97 : targetCol - 65;
				out.println("row: " + (row + 1) + ", col: " + Character.toString(col + 65));
				userMove = new Move(targetRow, targetCol);
				validMove = piece.checkIfLegalMove(targetRow, targetCol);
			}
			board.executeMove(piece, userMove);
			if (input.equals("quit")) {
				break;
			}
		}
	}

	/**
	 * Prints a representation of the board using UTF characters
	 * @param out - stream for printing
	 */
	private static void printBoard(PrintStream out) {
		out.println("    A   B   C   D   E   F   G   H   ");
		out.println("  +-------------------------------+  ");
		for (int i = 0; i < 8; i++) {
			String cur = (i + 1) + " |";
			for (int j = 0; j < 8; j++) {
				Piece p = board.getPiece(i, j);
				cur = cur + ' ' + (p == null ? ' ' : p.getString()) + (j < 7 ? " |" : '\0');
			}
			cur = cur + "| " + (i + 1);
			out.println(cur);
			if (i < 7) {
				out.println("  |-------------------------------|  ");
			}
		}
		out.println("  +-------------------------------+  ");
		out.println("    A   B   C   D   E   F   G   H   ");
	}

	/**
	 * Prints all legal moves in the current board
	 * @param board - current board
	 * @param out - stream for printing
	 */
	private static void printAllMoves(Board board, PrintStream out) {
		for (Piece[] row : board.getBoard()) {
			for (Piece piece : row) {
				if (piece != null) {
					out.print(piece.getColor() + " " + piece.getName() + ": ");
					printPieceMoves(piece, out);
				}
			}
		}
		out.println();
	}

	/**
	 * Prints all legal moves of the specified piece
	 * @param piece - piece to print moves for
	 * @param out - stream for printing
	 */
	private static void printPieceMoves(Piece piece, PrintStream out) {
		for (Move move : piece.getLegalMoves())
			out.print(Character.toString(move.getCol() + 65) + (move.getRow() + 1) + " ");
		out.println();
	}
}
