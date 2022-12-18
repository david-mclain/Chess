package Games;

import java.io.DataOutputStream;
import java.io.IOException;
/*
 * File: Game.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for maintaining game state when game
 * format is online
 */
public class OnlineGame extends Game {
	private DataOutputStream[] outputs;
	/**
	 * Instantiates OnlineGame
	 * @param outputs - outputs to send information to whenever game state is changed
	 */
	public OnlineGame(DataOutputStream[] outputs) {
		super();
		this.outputs = outputs;
	}
	/**
	 * Updates client with updated board
	 */
	@Override
	public void updateClient() {
		for (DataOutputStream o : outputs) {
			try {
				o.writeUTF("BOARD "+ FEN.toFEN(board));
			}
			catch (IOException e) {}
		}
	}
	/**
	 * Notifies client game has ended with cause
	 */
	@Override
	public void endGame(End gameOver) {
		try {
			outputs[0].writeUTF("END_GAME " + gameOver.toString());
			outputs[1].writeUTF("END_GAME " + gameOver.toString());
		}
		catch (IOException e) {}
	}
	/**
	 * Notifies client they are in check
	 */
	@Override
	public void notifyClientCheck() {
		int x = this.currentPlayer.getColor() == Side.BLACK ? 1 : 0;
		try {
			outputs[x].writeUTF("CHECKING_PIECES "+ getCheckingPieces().toString());
		} 
		catch (IOException e1) {}
	}
	/**
	 * Notifies client they can promote at specified position
	 */
	@Override
	public void clientPromote(int i, int j) {
		int x = this.currentPlayer.getColor() == Side.BLACK ? 1 : 0;
		try {
			outputs[x].writeUTF("PROMOTE "+ i + "" + j);
		} catch (IOException e1) {}
	}
	/**
	 * Sends all captured pieces to clients
	 */
	@Override
	public void sendCaptured() {
		for (DataOutputStream o : outputs) {
			try {
				o.writeUTF("CAPTURED "+ this.getCapturedPieces().toString());
			} catch (IOException e) {}
		}
	}
}
