import java.util.List;
/*
 * File: Game.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for maintaining game state when format is
 * local
 */
public class LocalGame extends Game {
	private LocalClient client;
	/**
	 * Instantiates LocalGame
	 * @param client - client to be communicating to
	 */
	public LocalGame(LocalClient client) {
		super();
		this.client = client;
		client.update(FEN.toFEN(board));
	}
	/**
	 * Instantiates LocalGame with input from file
	 * @param toLoad - game to load
	 * @param client - client to connect to
	 */
	public LocalGame(List<String> toLoad, LocalClient client) {
		super(toLoad);
		this.client = client;
		sendCaptured();
		client.update(FEN.toFEN(board));
	}
	/**
	 * Updates client with current game's board
	 */
	@Override
	public void updateClient() {
		client.update(FEN.toFEN(board));
	}
	/**
	 * Notifies client is in check
	 */
	@Override
	public void notifyClientCheck() {
		client.setCheckingPieces(getCheckingPieces().toString());
	}
	/**
	 * Notifies client to promote at position
	 */
	@Override
	public void clientPromote(int i, int j) {
		client.promote(i, j);
	}
	/**
	 * Notifies client to end game with cause
	 */
	@Override
	public void endGame(End gameOver) {
		client.endGame(gameOver.toString());
	}
	/**
	 * Sends captured pieces to client
	 */
	@Override
	public void sendCaptured() {
		client.setCapturedPieces(this.getCapturedPieces().toString());
	}
}
