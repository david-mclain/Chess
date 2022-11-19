import java.util.List;
import javax.swing.ImageIcon;

public abstract class ChessPiece {
	protected int row;
	protected int col;
	protected boolean color;
	protected ImageIcon image;
	
	public ChessPiece(int row, int col, boolean color) {
		this.row = row;
		this.col = col;
		this.color = color;
	}
	
	public boolean getColor() {
		return color;
	}
	
	public int getCol() {
		return row;
	}
	
	public int getRow() {
		return col;
	}
	
	public abstract List<List<Integer>> getValidMoves();

	public ImageIcon getIcon() {
		return image;
	}
	
}
