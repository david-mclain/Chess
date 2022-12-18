package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import javax.swing.Timer;
import Games.*;
import Pieces.*;
/*
 * File: Server.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for creating a server to handle an online
 * chess game set on a specific port. Allows two connections and as long as both
 * clients are connected everything will respond accordingly
 */
public class Server {
	private static DataOutputStream[] outputs = new DataOutputStream[2];
	private static int cur = 0;
	public static void main(String[] args) throws Exception {
		System.out.println(InetAddress.getLocalHost());
		try (var listener = new ServerSocket(10000)) {
			listener.setSoTimeout(10000);
			System.out.println("The Chess server is running...");
			var pool = Executors.newFixedThreadPool(200);
			Game game = new OnlineGame(outputs);
			while (cur < 1) {
				pool.execute(new ChessManager(listener.accept(), Side.WHITE, game));
				pool.execute(new ChessManager(listener.accept(), Side.BLACK, game));
			}
		}
	}
	/*
	 * Description: ChessManager deals with creating a new thread when a client
	 * connects. After client connects ChessManager will deal with all input and
	 * output to clients to ensure clients do not interrupt threads.
	 */
	@SuppressWarnings("unused")
	private static class ChessManager implements Runnable {
		private Player you;
		private Player opponent = null;
		private Game game;
		private Socket socket;
		private Timer timer;
		private boolean gameStarted;

		public ChessManager(Socket socket, Side color, Game game) throws IOException {
			this.socket = socket;
			this.you = new Player(color);
			this.game = game;
			if (game.getWhitePlayer() == null)
				game.setWhitePlayer(you);
			else
				game.setBlackPlayer(you);
		}
		/**
		 * Method for thread to run
		 */
		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				you.setInput(new DataInputStream(socket.getInputStream()));
				you.setOutput(new DataOutputStream(socket.getOutputStream()));
				outputs[cur++] = you.getOutput();
				you.getOutput().writeUTF("WELCOME " + you.getColor().toString().charAt(0));
				you.getOutput().writeUTF("BOARD " + FEN.toFEN(game.getBoard()));
				processInputs();
			} catch (Exception e) {
				System.out.println("Error:" + socket);
				e.printStackTrace();
			} finally {
				try { socket.close(); }
				catch (IOException e) {}
				System.out.println("Closed: " + socket);
			}
		}
		/**
		 * Processes input from user
		 */
		private void processInputs() {
			for (;;) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (you == game.getBlackPlayer())
					opponent = game.getWhitePlayer();
				else
					opponent = game.getBlackPlayer();
				try {
					if (you.getInput().available() > 0) {
						String response = you.getInput().readUTF();
						processCommand(response);
					}
				}
				catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
		/**
		 * Processes all inputs from users using a synchronized method which doesn't allow
		 * users whose turn it isn't to alter game state
		 * @param response - Input from client telling server what they are trying to process
		 */
		private synchronized void processCommand(String response) {
			if (cur > 1 && !gameStarted) {
				try {
					outputs[0].writeUTF("START");
					outputs[1].writeUTF("START");
					gameStarted = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (game.getCurrentPlayer().getColor() != you.getColor() && !response.startsWith("PROMOTE")) {
				try {
					you.getOutput().writeUTF("ERROR Not Your Turn");
				} catch (IOException e) {}
				return;
			}
			if (response.startsWith("SELECT")) {
				try {
					Piece piece = game.selectPiece(Character.getNumericValue(response.charAt(7)), Character.getNumericValue(response.charAt(8)));
					you.getOutput().writeUTF("VALID " + game.getLegalMoves(piece).toString());
				}
				catch (IOException e) {
					try {
						you.getOutput().writeUTF("ERROR Select Valid Piece");
					}
					catch (IOException e1) {}
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (response.startsWith("MOVE")) {
				System.out.println("moving");
				int i = Character.getNumericValue(response.charAt(5));
				int j = Character.getNumericValue(response.charAt(6));
				int l = Character.getNumericValue(response.charAt(7));
				int m = Character.getNumericValue(response.charAt(8));
				try {
					game.movePiece(i, j, l, m);
					outputs[0].writeUTF("BOARD " + FEN.toFEN(game.getBoard()));
					outputs[1].writeUTF("BOARD " + FEN.toFEN(game.getBoard()));
					outputs[0].writeUTF("FLIP");
					outputs[1].writeUTF("FLIP");
				} catch (Exception e) {
					System.out.println("error moving piece");
					e.printStackTrace();
				}
			}
			else if (response.startsWith("PROMOTE")) {
				int type = Character.getNumericValue(response.charAt(10));
				PieceType promotePiece = null;
				if (type == 1) {
					promotePiece = PieceType.ROOK;
				}
				else if (type == 2) {
					promotePiece = PieceType.KNIGHT;
				}
				else if (type == 3) {
					promotePiece = PieceType.BISHOP;
				}
				else {
					promotePiece = PieceType.QUEEN;
				}
				game.promote(Character.getNumericValue(response.charAt(8)), Character.getNumericValue(response.charAt(9)), promotePiece);
			}
			else if (response.startsWith("END_GAME")) {
				String cause = response.substring(9);
				End end = null;
				if (cause.equals("CHECKMATE")) {
					end = End.CHECKMATE;
				}
				else if (cause.equals("TIMER")) {
					end = End.TIMER;
				}
				else if (cause.equals("STALEMATE")) {
					end = End.STALEMATE;
				}
				else if (cause.equals("THREEFOLD")) {
					end = End.THREEFOLD;
				}
				else if (cause.equals("FIFTYMOVE")) {
					end = End.FIFTYMOVE;
				}
				game.endGame(end);
			}
		}
	}
}
