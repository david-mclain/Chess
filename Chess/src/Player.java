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
	
	public void setInput(DataInputStream input) {
		this.input = input;
	}
	public void setOutput(DataOutputStream output) {
		this.output = output;	
	}
	public DataInputStream getInput() {
		return input;
	}
	public DataOutputStream getOutput() {
		return output;
	}
	
}