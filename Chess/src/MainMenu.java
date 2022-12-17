import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
/*
 * File: MainMenu.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for creating a main menu to create a chess client.
 * If user decides to create server, it will allow them to then connect to server from
 * within the same main menu after. User has options of choosing competitive mode or not
 * from within the menu which will give players both a 3 minute timer for their gameplay.
 */

public class MainMenu {
	private static JFrame frame;
	private static JPanel panel;
	private static JButton local;
	private static JButton online;
	private static JButton load;
	private static JButton create;
	private static JRadioButton normal;
	private static JRadioButton competitive;
	private static ButtonGroup buttons;
	private static Font font = new Font("Monospaced", Font.PLAIN, 15);
	@SuppressWarnings("unused")
	private static Client client;
	public static void main(String[] args) {
		createMenu();
	}
	/**
	 * Creates UI for main menu
	 */
	private static void createMenu() {
		frame = new JFrame("Main Menu");
		panel = new JPanel();
		local = new JButton("Local Game");
		online = new JButton("Online Game");
		load = new JButton("Load Game");
		create = new JButton("Start Server");
		normal = new JRadioButton("Normal");
		competitive = new JRadioButton("Competitive");
		buttons = new ButtonGroup();

		panel.setLayout(null);
		panel.setBounds(0, 0, 400, 300);
		panel.setBackground(Color.WHITE);

		local.setBounds(25, 25, 150, 50);
		local.setFont(font);
		local.addActionListener(e -> createClient("local"));
		online.setBounds(25, 100, 150, 50);
		online.setFont(font);
		online.addActionListener(e -> createClient("network"));
		load.setBounds(25, 175, 150, 50);
		load.setFont(font);
		load.addActionListener(e -> createClient("load"));
		create.setBounds(25, 250, 150, 50);
		create.setFont(font);
		create.addActionListener(e -> createServer());

		normal.setBounds(225, 110, 130, 25);
		normal.setFont(font);
		normal.setBackground(Color.WHITE);
		competitive.setBounds(225, 185, 130, 25);
		competitive.setFont(font);
		competitive.setBackground(Color.WHITE);
		normal.setSelected(true);
		buttons.add(normal);
		buttons.add(competitive);

		panel.add(normal);
		panel.add(competitive);
		panel.add(local);
		panel.add(online);
		panel.add(load);
		panel.add(create);
		frame.add(panel);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setSize(400, 370);
		frame.setVisible(true);
	}
	/**
	 * Creates server if user selects to start a server
	 */
	private static void createServer() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					Server.main(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	/**
	 * Creates client based on type of game user wants to play along with
	 * competitive mode if selected
	 * @param format - Type of game to create (e.g "local" or "online")
	 */
	private static void createClient(String format) {
		if (format.equals("load")) {
			List<String> toLoad = new ArrayList<>();
			JFileChooser file = new JFileChooser();
			file.setDialogTitle("Choose file to load game from");
			int x = file.showOpenDialog(frame);
			if (x == JFileChooser.APPROVE_OPTION) {
				File read = file.getSelectedFile();
				try {
					Scanner in = new Scanner(read);
					while (in.hasNextLine()) {
						toLoad.add(in.nextLine().trim());
					}
					in.close();
					new LocalClient(toLoad, toLoad.get(2).equals("true"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			else {
				JOptionPane.showMessageDialog(frame, "Error Opening File.");
				return;
			}
			frame.dispose();
		}
		else if (format.equals("local")) {
			new LocalClient(competitive.isSelected());
			frame.dispose();
		}
		else {
			frame.dispose();
			String serverIP = getServerIP();
			while (!validIP(serverIP)) {
				serverIP = getServerIP();
			}
			new ServerClient(competitive.isSelected(), serverIP, 10000);
		}
	}
	/**
	 * Recieves string and determines if it is a valid IP Address
	 * @param serverIP - IP address entered
	 * @return true if valid, false otherwise
	 */
	private static boolean validIP(String serverIP) {
		String[] groups = serverIP.split("\\.");
		if (groups.length == 4) {
			for (String group : groups) {
				try {
					int num = Integer.parseInt(group);
					if (num < 0 || num > 255) {
						System.out.println("invalid IP");
						return false;
					}
				} catch (NumberFormatException e) {
					System.out.println("invalid IP");
					return false;
				}
			}
			return true;
		}
		System.out.println("invalid IP");
		return false;
	}
	/**
	 * Gets IP address with dialog option
	 * @return IP address entered by user
	 */
	private static String getServerIP() {
		return (String) JOptionPane.showInputDialog(
				frame,
				"Enter IP Address:\n",
				"Connect...",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				"127.0.0.1");
	}
}
