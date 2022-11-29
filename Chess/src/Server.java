import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class Server {
	
	static ArrayList<DataOutputStream> sq;


	public static void main(String[] args) throws Exception {
			System.out.println(InetAddress.getLocalHost());
			sq = new ArrayList<>();
			
	        try (var listener = new ServerSocket(59896)) 
	        {
	            System.out.println("The Chess server is running...");
	            var pool = Executors.newFixedThreadPool(2);
	            while (true) 
	            {
	                pool.execute(new chessManager(listener.accept()));
	            }
	        }
	    }
	
    private static class chessManager implements Runnable 
    {
        private Socket socket;

        chessManager(Socket socket) { this.socket = socket; }

		@Override
		public void run() {
			System.out.println("Connected: " + socket);
            try 
            {
            	DataInputStream in = new DataInputStream(socket.getInputStream());
            	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                sq.add(out);
                if (sq.size() > 1) {
                	out.writeUTF("side black");
                } else {
                	out.writeUTF("side white");
                }
                while (true)
                {
                	if (in.available() > 0) {
                		//placeholder until commands are finalized
                		System.out.println(in.readUTF());
                	}
    				try {
    					Thread.sleep(10);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
                }
            } catch (Exception e) 
            {
                System.out.println("Error:" + socket);
                e.printStackTrace();
            } finally {
                try { socket.close(); } 
                catch (IOException e) {}
                System.out.println("Closed: " + socket);
            }
		}
    }
}