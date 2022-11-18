

import javax.swing.JButton;
import javax.swing.JFrame;

public class MainMenu {
	private static JFrame frame;
	
	public static void main(String[] args) {
		frame = new JFrame("Main Menu");
		JButton local = new JButton("Local");
		JButton online = new JButton("Online");
		local.setBounds(100, 100, 100, 25);
		local.addActionListener(e -> createLocal());
		online.setBounds(300, 100, 100, 25);
		online.addActionListener(e -> createOnline());
		frame.add(local);
		frame.add(online);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
	
	private static void createLocal() {
		frame.dispose();
		ChessUI ui = new ChessUI();
	}
	
	private static void createOnline() {
		frame.dispose();
		ChessUI ui = new ChessUI();
	}
}
