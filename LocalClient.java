import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class LocalClient extends Client {
	
	LocalClient(boolean competitive) {
		this(null, competitive);
	}

	public LocalClient(List<String> toLoad, boolean competitive) {
		super(competitive);
		if (toLoad == null) {
			game = new LocalGame();
		}
		else {
			game = new LocalGame(toLoad);
		}
		setMenu();
		frame.add(getBoard());
	}
	
	private void setMenu() {
		JMenuBar menu = new JMenuBar();
		JMenu load = new JMenu("Save");
		JMenuItem loadGame = new JMenuItem("Save Game");
		loadGame.addActionListener(e -> saveGame());
		load.add(loadGame);
		menu.add(load);
		frame.setJMenuBar(menu);
	}
	
	private void saveGame() {
		List<String> toSave = new ArrayList<>();
		Board board = game.getBoard();
		for (int i = 0; i < 64; i++) {
			toSave.add((board.getPiece(i / 8, i % 8) != null ? (board.getPiece(i / 8, i % 8).toString()) : "null") + ";" + (i / 8) + ";" + (i % 8) + "\n");
		}
		toSave.add(game.getCurrentPlayer().getColor() == Side.WHITE ? "WHITE\n" : "BLACK\n");
		toSave.add("" + competitive);
		
		JFileChooser file = new JFileChooser();
		file.setDialogTitle("Choose where to save");
		int x = file.showSaveDialog(frame);
		if (x == JFileChooser.APPROVE_OPTION) {
			File save = file.getSelectedFile();
			try {
				FileWriter write = new FileWriter(save.getAbsolutePath().toString());
				for (int i = 0; i < toSave.size(); i++) {
					write.write(toSave.get(i));
				}
				write.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
