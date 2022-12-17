import java.io.DataInputStream;
import java.io.DataOutputStream;
/*
 * File: Player.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: Player is used for containing game state of current player.
 * Contains inputs/outputs for player if game is online format
 */
public class Player {
	private Side color;
	private DataInputStream input;
	private DataOutputStream output;
 	/**
	 * Instantiates new player
	 * @param color - color of player
	 */
	public Player(Side color) {
		this.setColor(color);
	}
	/**
	 * Returns color of player
	 * @return color of player
	 */
	public Side getColor() {
		return color;
	}
	/**
	 * Sets color of player
	 * @param color - color to set player
	 */
	public void setColor(Side color) {
		this.color = color;
	}
	/**
	 * Sets current player's DataInputStream
	 * @param DataInputStream - DataInputStream to set to player
	 */
	public void setInput(DataInputStream input) {
		this.input = input;
	}
	/**
	 * Sets current player's output
	 * @param DataOutputStream - DataOutputStream to set to player
	 */
	public void setOutput(DataOutputStream output) {
		this.output = output;
	}
	/**
	 * Returns DataInputStream of player
	 * @return DataInputStream of player
	 */
	public DataInputStream getInput() {
		return input;
	}
	/**
	 * Returns DataOutputStream of player
	 * @return DataOutputStream of player
	 */
	public DataOutputStream getOutput() {
		return output;
	}
}
