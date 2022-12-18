package Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import Games.*;
/*
 * File: GameOverMessage.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for creating a panel to show in popup
 * when game ends which includes the cause of game ending and who won
 */
@SuppressWarnings("serial")
public class GameOverMessage extends JPanel {
	/**
	 * Instantiates panel with information on game end
	 * @param curPlayer - player who won if cause is a win
	 * @param cause - cause of game ending
	 */
	public GameOverMessage(int curPlayer, End cause) {
		super();
		setBackground(Color.BLACK);
		JLabel label1 = new JLabel("Game over!", SwingConstants.CENTER);
		String message = "";
		if (cause == End.CHECKMATE) {
			if (curPlayer  % 2 == 0)
				message = "Black Wins by Checkmate";
			else
				message = "White Wins by Checkmate";
		} else if (cause == End.TIMER) {
			if (curPlayer % 2 == 0)
				message = "Black Wins by Timer";
			else
				message = "White Wins by Timer";
		} else if (cause == End.STALEMATE) {
			message = "Draw by Stalemate";
		} else if (cause == End.THREEFOLD) {
			message = "Draw by Threefold Repetition";
		} else if (cause == End.FIFTYMOVE) {
			message = "Draw by Fifty-Move Rule";
		}
		JLabel label2 = new JLabel(message, SwingConstants.CENTER);
		label1.setFont(new Font("Monospaced", Font.ITALIC, 40));
		label1.setForeground(Color.WHITE);
		label2.setFont(new Font("Monospaced", Font.ITALIC, 40));
		label2.setForeground(Color.WHITE);
		setLayout(new GridLayout(2, 1, 2, 2));
		add(label1);
		add(label2);
	}
}
