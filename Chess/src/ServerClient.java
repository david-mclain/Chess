import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.Timer;
/*
 * File: ServerClient.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for any UI handling and user input for an
 * online game.
 */
public class ServerClient extends Client {
	private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean gameOver;
    private Timer timer = new Timer(100, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				play();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	});
    /**
     * Instantiates server client with information to connect to server
     * @param competitive - if competitive is selected
     * @param ip - address to connect to
     * @param port - port to connect to
     */
	public ServerClient(boolean competitive, String ip, int port) {
		super(competitive);
		connect(ip, port);
		gameOver = false;
		timer.start();
	}
	/**
	 * Connects client to server
	 * @param ip - address to connect to
	 * @param port - port to connect to
	 */
	private void connect(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Begins play loop in new thread
	 * @throws IOException
	 */
	private void play() throws IOException {
		timer.stop();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String response = in.readUTF();
					frame.setTitle("Chess - Player " + (response.charAt(8) == 'B' ? "Black" : "White"));
					response = in.readUTF();
					System.out.println(response);
					update(response.substring(6));
					while (true) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (in.available() > 0) {
							response = in.readUTF();
							if (response.startsWith("START")) {
								start("white");
							}
							else if (response.startsWith("BOARD")) {
								update(response.substring(6));
							}
							else if (response.startsWith("FLIP")) {
								if (!gameOver) {
									flipPlayers();
								}
							}
							else if (response.startsWith("VALID")) {
								showMoves(response.substring(7, response.length() - 1));
							}
							else if (response.startsWith("END_GAME")) {
								gameOver = true;
								endGame(response.substring(9));
							}
							else if (response.startsWith("CHECKING_PIECES")) {
								setCheckingPieces(response.substring(16));
							}
							else if (response.startsWith("CAPTURED")) {
								setCapturedPieces(response.substring(9));
							}
							else if (response.startsWith("PROMOTE")) {
								promote(Character.getNumericValue(response.charAt(8)), Character.getNumericValue(response.charAt(9)));
							}
							else if (response.startsWith("ERROR")) {
								editMessage(response.substring(6));
							}
							else {
								break;
							}
						}
					}
					out.writeUTF("QUIT");
				}
				catch (SocketException e) {}
				catch (Exception e) {}
				finally {
					try {socket.close();}
					catch (IOException e) {}
					frame.dispose();
				}
			}
		});
		thread.start();
	}
	/**
	 * Edits message sent to user
	 * @param substring - message to display to user
	 */
	protected void editMessage(String substring) {
		System.out.print(substring);
	}
	/**
	 * Displays valid moves to user based on input from server
	 * @param substring - string containing valid moves
	 */
	private void showMoves(String substring) {
		legalMoves.clear();
		String[] s = substring.split(",");
		for (String x : s) {
			x = x.trim();
			legalMoves.add(x);
			int col = x.charAt(0) - 'A';
			int row = Math.abs(Character.getNumericValue(x.charAt(1)) - 8);
			squares[row][col].updateLook(Color.YELLOW);
			pieceChosen = true;
		}
	}
	/**
	 * Selects piece and communicates it to server
	 */
	public void selectPiece(int i, int j) throws Exception {
		curRow = i;
		curCol = j;
		curColor = curPlayer % 2 == 0 ? Side.WHITE : Side.BLACK;
		out.writeUTF("SELECT " + i + "" + j);
	}
	/**
	 * Attempts to move piece and notifies server
	 */
	@Override
	protected void move(int row, int col, int i, int j) throws Exception {
		out.writeUTF("MOVE " + row + "" + col + "" + i + "" + j);
	}
	/**
	 * Sends message to server to promote to piece user selected
	 */
	@Override
	public void promote(int i, int j, int type) {
		try {
			out.writeUTF("PROMOTE " + i + "" + j + "" + type);
		}
		catch(IOException e) {}
	}
	@Override
	protected void checkEnd() {}

}
