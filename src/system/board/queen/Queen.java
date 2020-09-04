package system.board.queen;
/**
 * 
 * @author Guglielmo Cassini
 * Classe rappresentante una singola regina.
 *
 */
public class Queen {
	
	private int column;
	private int row;
	
	/**
	 * Costruttore che crea una regina non posizionata
	 */
	public Queen() {
		super();
		column = -1;
		row = -1;
	}
	
	/**
	 * Costruttore che crea una regina posizionata alla specificata riga e colonna.
	 * @param column
	 * @param row
	 */
	public Queen(int column, int row) {
		super();
		this.column = column;
		this.row = row;
	}
	
	/**
	 * Ritorna se la regina è posizionata nella board o no
	 * @return true se la regina è posizionate, false altrimentis
	 */
	public boolean getIfPositioned(){
		return column != -1 && row != -1;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	
}
