package system.board.state;
import java.util.HashMap;
import java.util.Map;

import system.board.queen.Queen;

/**
 * 
 * @author Guglielmo Cassini
 * 
 * Classe rappresentante il posizionamento delle 8 regine all'interno della scacciera.
 *
 */
public class State {

	private Map<Integer, Queen> queenMap;
	//Mi serve la lastColUsed, che dice l'ultima colonna in cui è stata inserita
	//una regina.
	private int lastColUsed;
	private int numberOfQueens;
	
	
	public State(int numberOfQueens) {
		lastColUsed = 0;
		this.numberOfQueens = numberOfQueens;
		queenMap = new HashMap<Integer, Queen>();
		//Quando uno stato viene creato creo al suo interno un determinato numero
		//di regine non posizionate
		for(int i=0; i < numberOfQueens; i++){
			Queen tmp = new Queen();
			queenMap.put(i, tmp);
		}
	}
	
	//Quando voglio passare ad uno stato successivo, devo copiare lo stato attuale
	//in quello nuovo e modificare il nuovo stato. Mi serve quindi la possibilità di 
	//creare un nuovo stato copiandone uno vecchio --> Secondo costruttore
	public State(State stateToCopyFrom) {
		lastColUsed = stateToCopyFrom.getLastColUsed();
		this.queenMap = new HashMap<Integer, Queen>();
		this.numberOfQueens = stateToCopyFrom.getNumberOfQueen();
		//Istruzioni per copiare la mappa di uno stato in quello nuovo
		Map<Integer, Queen> mapOfThestateToCopyFrom = stateToCopyFrom.getQueenMap();
		for(Map.Entry<Integer, Queen> entry : mapOfThestateToCopyFrom.entrySet()) {
			Queen tmp = entry.getValue();
			this.queenMap.put(entry.getKey(), new Queen(tmp.getColumn(), tmp.getRow()));
		}
	}

	
	public Map<Integer, Queen> getQueenMap(){
		return queenMap;
	}
	
	public int getLastColUsed() {
		return lastColUsed;
	}
	
	//Azione che viene richiamata solo dopo che la board ha controllato la sua
	//fattibilità
	public void setQueenNewPosition(int queenId, int column, int row){
		Queen queen = queenMap.get(queenId);
		queen.setColumn(column);
		queen.setRow(row);
		lastColUsed = column;
	}
	
	public int getNumberOfQueen(){
		return numberOfQueens;
	}
	
	//Uno stato è valido quando tutte le regine presenti sono posizionate
	public boolean isValid(){
		
		for(Map.Entry<Integer, Queen> entry : queenMap.entrySet()) {
			if(!entry.getValue().getIfPositioned())
				return false;
		}
		
		return true;
	}
	
}
