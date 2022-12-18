package Graphics;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import Games.*;
import Pieces.*;
/*
 * File: LocalClient.java
 * Contributors: David McLain, Adrian Moore, Martin Cox, Luke Niemann
 * Description: This class is used for any UI handling and user input for a
 * local game
 */
public class LocalClient extends Client {
	private LocalGame game;
	/**
	 * Instantiates new LocalClient
	 * @param competitive - if game is competitive
	 */
	public LocalClient(boolean competitive) {
		super(competitive);
		game = new LocalGame(this);
		setMenu();
		start("white");
	}
	/**
	 * Instantiates new LocalClient with game to load
	 * @param toLoad      - game to load
	 * @param competitive - if game is competitive
	 */
	public LocalClient(List<String> toLoad, boolean competitive) {
		super(competitive, toLoad.get(1).equals("white") ? 0 : 1, Integer.parseInt(toLoad.get(3)), Integer.parseInt(toLoad.get(4)));
		game = new LocalGame(toLoad, this);
		setMenu();
		start(toLoad.get(1));
	}
	/**
	 * Sets menu of client
	 */
	private void setMenu() {
		JMenuBar menu = new JMenuBar();
		JMenu save = new JMenu("Save");
		JMenuItem saveGame = new JMenuItem("Save Game");
		saveGame.addActionListener(e -> saveGame());
		save.add(saveGame);
		menu.add(save);
		frame.setJMenuBar(menu);
	}
	/**
	 * Saves current game to file that client specifies
	 */
	private void saveGame() {
		List<String> toSave = new ArrayList<>();
		toSave.add(FEN.toFENFull(game.getBoard()) + "\n");
		toSave.add(game.getCurrentPlayer().getColor() == Side.WHITE ? "white\n" : "black\n");
		toSave.add(competitive + "\n");
		toSave.add(whiteTime + "\n");
		toSave.add(blackTime + "\n");
		String allPrevPos = "";
		ArrayList<String> prevPos = game.getPrevPositions();
		for (int i = 0; i < prevPos.size(); i++) {
			allPrevPos += prevPos.get(i);
			if (i < prevPos.size() - 1)
				allPrevPos += "_";
		}
		toSave.add(allPrevPos + "\n");
		toSave.add(game.getHalfMoveClock() + "\n");
		for (String piece : game.getCapturedPieces())
			toSave.add(piece + "\n");
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
	/**
	 * Selects piece in game
	 */
	@Override
	public void selectPiece(int i, int j) throws Exception {
		Piece curPiece = game.selectPiece(i, j);
		curRow = i;
		curCol = j;
		curColor = curPlayer % 2 == 0 ? Side.WHITE : Side.BLACK;
		pieceChosen = true;
		legalMoves = game.getLegalMoves(curPiece);
		for (String m : legalMoves) {
			int col = m.charAt(0) - 'A';
			int row = Math.abs(Character.getNumericValue(m.charAt(1)) - 8);
			squares[row][col].updateLook(Color.YELLOW);
		}
	}
	/**
	 * Moves current piece
	 */
	@Override
	protected void move(int curRow, int curCol, int i, int j) throws Exception {
		game.movePiece(curRow, curCol, i, j);
		flipPlayers();
	}
	/**
	 * Promotes piece to new specified piece
	 */
	@Override
	public void promote(int i, int j, int type) {
		if (type == 1) {
			promotePiece = PieceType.ROOK;
		} else if (type == 2) {
			promotePiece = PieceType.KNIGHT;
		} else if (type == 3) {
			promotePiece = PieceType.BISHOP;
		} else {
			promotePiece = PieceType.QUEEN;
		}
		game.promote(i, j, promotePiece);
	}
	/**
	 * Checks if game has ended
	 */
	@Override
	protected void checkEnd() {
		if (game.end()) {
			String cause = game.getCause();
			endGame(cause);
		}
	}
}
