package system.board;

import java.util.Map;

import system.board.state.State;

/**
 * 
 * @author Guglielmo Cassini.
 * Pure fabrication che elabora e gestisce una matrice rappresentante le caselle di un oggetto
 * Board per calcolare quale di queste caselle ogni volta vengono occupate con l'inserimento
 * di una nuova regina.
 */
public class BoardUsedBoxElaborator {

	boolean[][] usedBox;
	int numberOfQueens;
	
	/**
	 * Costruttore che riceve come input una matrice usedBox, istanziata da una 
	 * Board, ed il numero di regine di una board.
	 * @param usedBox
	 * @param numberOfQueens
	 */
	public BoardUsedBoxElaborator(boolean[][] usedBox, int numberOfQueens) {
		super();
		this.usedBox = usedBox;
		this.numberOfQueens = numberOfQueens;
	}
	
	
	/**
	 * Metodo che a partire da una riga e colonna va a settare come occupate
	 * le posizioni relativa della regina e quelle messe sotto attacco dalla medesima
	 * regina in quelle posizioni.
	 * @param column
	 * @param row
	 */
	public void elab(int column, int row) {
		
		usedBox[row][column] = true;
		
		columnToUp(column, row);
		columnToDown(column, row);
		rowLeft(column, row);
		rowRight(column, row);
		
		
		obliqueLeftUp(column, row);
		obliqueLeftDown(column, row);
		obliqueRightUp(column, row);
		obliqueRightDown(column, row);
		
		//printUsedBox();
	}

	private void rowRight(int column, int row) {
		for(int i=0; column+i+1 < numberOfQueens; i++) {
			usedBox[row][column+i+1] = true;
		}		
	}


	private void rowLeft(int column, int row) {
		for(int i=0; column-i-1 > -1; i++) {
			usedBox[row][column-i-1] = true;
		}	
	}


	public void columnToDown(int column, int row) {
		for(int i=0; row+i+1 < numberOfQueens; i++) {
			usedBox[row+i+1][column] = true;
		}
	}	

	public void columnToUp(int column, int row) {
		for(int i=0; row-i-1 > -1; i++) {
			usedBox[row-i-1][column] = true;
		}		
	}
	
	public void obliqueRightDown(int column, int row) {
		for(int i=0; row+i+1 < numberOfQueens && column+i+1 < numberOfQueens  ; i++) {
			usedBox[row+i+1][column+i+1] = true;
		}
	}
	
	public void obliqueRightUp(int column, int row) {
		for(int i=0; row-i-1 > -1 && column+i+1 < numberOfQueens  ; i++) {
			usedBox[row-i-1][column+i+1] = true;
		}
	}
	
	public void obliqueLeftUp(int column, int row) {
		for(int i=0; row-i-1 > -1 && column-i-1 > -1 ; i++) {
			usedBox[row-i-1][column-i-1] = true;
		}
	}
	
	public void obliqueLeftDown(int column, int row) {
		for(int i=0; row+i+1 < numberOfQueens && column-i-1 > -1 ; i++) {
			usedBox[row+i+1][column-i-1] = true;
		}
	}
	
	/**
	 * Metodo che libera tutte le caselle di un determinata board
	 */
	public void freeAllUserBox(){
		for(int i=0; i < numberOfQueens; i++)
			for(int j=0; j< numberOfQueens; j++)
				usedBox[i][j] = false;
		
	}
	
	
//	public void printUsedBox(){
//		for(int i=0; i < numberOfQueens; i++) {
//			for(int j=0; j < numberOfQueens; j++) {
//				int v = usedBox[i][j] ? 1 : 0;
//				System.out.print( v + " - ");
//			
//			}
//			
//			System.out.println();
//		}
//	}
	
	/**
	 * Metodo che copia il contenuto di una matrice rappresentante le celle di una
	 * Board in una nuova matrice. Utilizzato durante le operazioni di creatura
	 * di una Board a partire da un'altra.
	 * 
	 * @param another
	 */
	public void copyUsedBoxFromAnother(boolean another[][]){

		for(int i=0; i < numberOfQueens; i++) 
			for(int j=0; j < numberOfQueens; j++) 
				usedBox[i][j] = another[i][j];
	}
	
}