import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
/*
 * File: Square.java
 * Author: David McLain
 * Description: This class is used for creating a panel for user to interact with
 * in game which will display current piece, if move is valid panel will highlight yellow,
 * if panel contains piece responsible for check panel will highlight red
 */
@SuppressWarnings("serial")
public class Square extends JPanel {
	
	private String piece;
	private Color defaultColor;
	private JLabel label;
	/**
	 * Instantiates square with information on location
	 * @param defaultColor - default color to display panel
	 */
	public Square(Color defaultColor) {
		this.defaultColor = defaultColor;
		this.piece = null;
		label = new JLabel();
		setLayout(new GridBagLayout());
		updateIcon(piece);
		add(label);
	}
	/**
	 * Updates squares icon to new piece
	 * @param piece - path to piece to display
	 */
	public void updateIcon(String piece) {
		this.piece = piece;
		updateLook(defaultColor);
	}
	/**
	 * Resets background color to default
	 */
	public void resetLook() {
		setBackground(defaultColor);
	}
	/**
	 * Highlights square different color
	 * @param color - color to highlight square
	 */
	public void updateLook(Color color) {
		setBackground(color);
		if (piece != null) {
			label.setIcon(new ImageIcon(piece));
		}
		else {
			label.setIcon(null);
		}
	}
	
}