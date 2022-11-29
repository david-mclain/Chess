import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerClient extends Client {
	private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
	ServerClient(boolean competitive, String ip, int port) {
		super(competitive);
		socket = null;
		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		game = new OnlineGame();
		frame.add(getBoard());
	}
}
