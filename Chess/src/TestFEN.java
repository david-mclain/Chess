/*
 * File: TestFEN.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: Testing script for FEN.java. Tests toFen and toBoard.
 */
public class TestFEN{
	public static void main(String[] args) {
		Board b = new Board();

		//test board to fen
		System.out.println("Should be: rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		System.out.println("Actual:    "+ FEN.toFEN(b));

		System.out.println("\n-------------------\n");

		//test FEN to board
		System.out.println("Should be: \nROOK KNIGHT BISHOP QUEEN KING BISHOP KNIGHT ROOK \r\n"
				+ "PAWN PAWN PAWN PAWN PAWN PAWN PAWN PAWN \r\n"
				+ ". . . . . . . . \r\n"
				+ ". . . . . . . . \r\n"
				+ ". . . . . . . . \r\n"
				+ ". . . . . . . . \r\n"
				+ "PAWN PAWN PAWN PAWN PAWN PAWN PAWN PAWN \r\n"
				+ "ROOK KNIGHT BISHOP QUEEN KING BISHOP KNIGHT ROOK");
		Piece[][] newB =
				FEN.toBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		System.out.println("Actual:");
		printBoard(newB);
	}

	//this helper method is verified to print accurately
	public static void printBoard(Piece[][] newB) {
		for (int i = 0; i < 8; i++) {
			String row = "";
			for (int j = 0; j < 8; j++) {
				if (newB[i][j] == null) {
					row+=". ";
				} else {
					row+=newB[i][j].getName().toString();
					row+=" ";
				}
			}
			System.out.println(row);
			row = "";
		}
	}
}
