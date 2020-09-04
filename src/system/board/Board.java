package system.board;

import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.FREE_MEM;

import system.board.queen.Queen;
import system.board.state.State;

/**
 * 
 * @author Guglielmo Cassini
 * Classe rappresentante una scacchiera di un numero di caselle NxN dove N è pari
 * al numero delle regine con cui viene inizializzata la scacchiera.
 */
public class Board {

	//Il numero delle regine, corrisponde al numero di righe e colonne totali
	//della scacchiera
	private int numberOfQueens;
	private Map<Integer,State> oldStatesMap;
	private State currentState;
	private boolean[][] usedBox;
	private int lastColumnUsed;
	private int lastRowUsed;
	private BoardUsedBoxElaborator elab;
	
	/**
	 * Costruttore con cui viene creata una nuova scacchiera 
	 * specificando il numero N di caselle NxN
	 * @param numberOfQueens
	 */
	public Board(int numberOfQueens) {
		super();
		this.numberOfQueens = numberOfQueens;
		currentState = new State(numberOfQueens);
		oldStatesMap = new HashMap<Integer, State>();
		usedBox = new boolean[numberOfQueens][numberOfQueens];
		lastColumnUsed = 0;
		lastRowUsed = 0;
		elab = new BoardUsedBoxElaborator(usedBox, numberOfQueens);
	}

	/**
	 * Costruttore che genera una nuova istanza di una scacchiera a partire
	 * da una scacchiera preesistente.
	 * @param boardToCopyFrom
	 */
	public Board(Board boardToCopyFrom) {
		this.numberOfQueens = boardToCopyFrom.getNumberOfQueens();
		this.currentState = new State(boardToCopyFrom.getCurrentState());
		this.oldStatesMap = new HashMap<Integer, State>(boardToCopyFrom.getOldStatesMap());
		
		lastColumnUsed = boardToCopyFrom.getLastColumnUsed();
		lastRowUsed = boardToCopyFrom.getLastRowUsed();
		
		usedBox = new boolean[numberOfQueens][numberOfQueens];
		elab = new BoardUsedBoxElaborator(usedBox, numberOfQueens);
		elab.copyUsedBoxFromAnother(boardToCopyFrom.getUsedBox());
	}
	
	public int getNumberOfQueens() {
		return numberOfQueens;
	}

	public Map<Integer, State> getOldStatesMap() {
		return oldStatesMap;
	}

	public boolean[][] getUsedBox() {
		return usedBox;
	}

	public int getLastColumnUsed() {
		return lastColumnUsed;
	}

	public int getLastRowUsed() {
		return lastRowUsed;
	}

	public BoardUsedBoxElaborator getElab() {
		return elab;
	}
	
	
	//Il controllo della validità della nuova posizione e l'effettivo posizionamento
	//sono separati così un thread può avere un istante in cui sa come prendere le decisioni
	//successive senza che la board sia stata inalterata.
	//La used box è falsa quando quella cella è accessibile
	public boolean checkIfPositionAvailable(int column, int row){
		//System.out.println("Controllo se ("+row+","+column+") è disponibile");
		return !usedBox[row][column];
	}
	
	public void setQueenPosition(int queenId, int column, int row){	
		//System.out.println("Nella board, inserimento in corso");
		//Aggiungo lo stato a quelli vecchi creandone una copia
		int lastId = getMaxStateId();
		oldStatesMap.put(lastId+1, new State(currentState));
		
		//Aggiorno lo stato attuale
		elaborateUsedBox(column, row);
		currentState.setQueenNewPosition(queenId, column, row);
		//System.out.println("Nella board, inserimento in corso");
	}

	private int getMaxStateId() {
		int max = -1;
		
		if(oldStatesMap.size() == 0)
			return 0;		
		
		for(Map.Entry<Integer, State> entry : oldStatesMap.entrySet()) {
			if(max < entry.getKey())
				max = entry.getKey();
		}
		
		return max;
	}

	private void elaborateUsedBox(State state) {
		Map<Integer, Queen> queenMap = state.getQueenMap();
		for(Map.Entry<Integer, Queen> entry : queenMap.entrySet()){
			
			if(entry.getValue().getIfPositioned()){		
				elaborateUsedBox(entry.getValue().getColumn(), entry.getValue().getRow());
			}
		}		
	}

	private void elaborateUsedBox(int column, int row) {
		elab.elab(column, row);
	}
	
	//Devo recuperare il vecchio stato creandone una nuova copia, quello vecchio viene poi 
	//rimosso dalla mappa. Ricalcolo le usedBox a partire dal nuovo stato corrente
	public void recoveryThePrecedentOldState(){
		int lastStateId = getMaxStateId();
		State stateToRecovery = oldStatesMap.get(lastStateId);
		currentState = new State(stateToRecovery);
		//Quando faccio un recovery devo ricalcolare la used box, prima riazzerandola
		//e poi facendola elaborare
		freeUserBox();
		elaborateUsedBox(currentState);
		oldStatesMap.remove(lastStateId);
	}

	private void freeUserBox() {
		elab.freeAllUserBox();
	}

	public int getLastPositionedCol() {
		return currentState.getLastColUsed();
	}
	
	public void printQueenState(){
		
		for(Map.Entry<Integer, Queen> entry : currentState.getQueenMap().entrySet()){
			System.out.println("Regina # "+entry.getKey() + " Row="+entry.getValue().getRow() + " Col="+entry.getValue().getColumn());
		}
//		
	}
	
	public State getCurrentState() {
		return this.currentState;
	}
	
	
}
