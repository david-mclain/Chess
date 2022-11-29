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
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MainMenu {
	private static JFrame frame;
	private static JPanel panel;
	private static JButton local;
	private static JButton online;
	private static JButton load;
	private static JRadioButton normal;
	private static JRadioButton competitive;
	private static ButtonGroup buttons;
	private static Font font = new Font("Monospaced", Font.PLAIN, 15);
	
	public static void main(String[] args) {
		frame = new JFrame("Main Menu");
		panel = new JPanel();
		local = new JButton("Local Game");
		online = new JButton("Online Game");
		load = new JButton("Load Game");
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
		
		normal.setBounds(225, 75, 130, 25);
		normal.setFont(font);
		normal.setBackground(Color.WHITE);
		competitive.setBounds(225, 150, 130, 25);
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
		frame.add(panel);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setSize(400, 300);
		frame.setVisible(true);
	}
	
	private static void createClient(String format) {
		Client client = null;
		if (format.equals("load")) {
			List<String> toLoad = new ArrayList<>();
			JFileChooser file = new JFileChooser();
			file.setDialogTitle("Choose file to load game from");
			int x = file.showSaveDialog(frame);
			if (x == JFileChooser.APPROVE_OPTION) {
				File read = file.getSelectedFile();
				try {
					Scanner in = new Scanner(read);
					while (in.hasNextLine()) {
						toLoad.add(in.nextLine());
					}
					in.close();
					client = new LocalClient(toLoad, competitive.isSelected());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		else if (format.equals("local")) {
			client = new LocalClient(competitive.isSelected());
		}
		else {
			client = new ServerClient(competitive.isSelected());
		}
		frame.dispose();
		client.start();
	}
	
}
