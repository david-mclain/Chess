import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Player {
	
	private Side color;
	private DataInputStream input;
	private DataOutputStream output;
	
	public Player(Side color) {
		this.setColor(color);
	}

	public Side getColor() {
		return color;
	}

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
