package Chess;

public class ChessPiece {
	protected int x;
	protected int y;
	protected boolean color;

	
	public ChessPiece(int x, int y, boolean color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public boolean getColor() {
		return color;
	}
	
	public int getCol() {
		return x;
	}
	
	public int getRow() {
		return y;
	}
	

}
