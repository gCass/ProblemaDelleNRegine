package system.solutionContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.board.queen.Queen;
import system.board.state.State;

public class SolutionContainer {

	private Map<Integer, State> solutions;
	private List<State> solutionsList; //Nei forking thread non posso usare un id
										//univoco perchè è di difficile gestione
	
	private static SolutionContainer container;
	
	private SolutionContainer() {
		solutions = new HashMap<Integer, State>();
		solutionsList = new ArrayList<State>();
	}
	
	synchronized public static SolutionContainer getSolutionContainer(){
		if(container == null)
			container = new SolutionContainer();
		
		return container;
	} 
	
	public void addSolution(State solutionState) {
		solutionsList.add(solutionState);
	}
	
	synchronized public  void addSolution(int key, State solutionState){
		solutions.put(key, solutionState);		
	}
	
	//Se la lista non è vuota restituisce quella, altrimenti restituisce la 
	// lista ottenuta dalla mappa.
	//Ho la distinzione per via dei forking thread e dei solutions thread
	synchronized public List<State> getAllSolutionsList(){
		if(solutionsList.size() !=0 )
			return solutionsList;
		
		return new ArrayList<State>(solutions.values());
	}
	
	synchronized public void printSolution(int key){
		
		//Prima controllo che quello stato esiste ed è valido
		State state = solutions.get(key);
		
		if(state == null) {
			System.out.println("Stato #"+key+ " non esistente");
			return;
		}
		
		if(!state.isValid()) {
			System.out.println("Stato #"+key+ " non è valido");
			return;
		}
		
		int[][] matrix = new int[state.getNumberOfQueen()][state.getNumberOfQueen()];
		Map<Integer, Queen> map =  state.getQueenMap();
		
		for(Map.Entry<Integer, Queen> entry : map.entrySet()) {
			Queen q = entry.getValue();
			matrix[q.getRow()][q.getColumn()] = 1; 			
		}
		
		System.out.println("ThreadId:"+key);
		printMatrix(matrix);
		
	}

	private void printMatrix(int[][] matrix) {
		for(int[] riga : matrix) {
			for(int elemento : riga) {
				System.out.print(elemento);
			}
			System.out.println();//Fine riga
		}		
	}
	
	synchronized public void printAllSolutions(){
		List<Integer> ids = new ArrayList<Integer>(solutions.keySet()); 
		
		for(Integer id : ids){
			System.out.println();
			System.out.println();
			System.out.println();
			
			printSolution(id);
			
			System.out.println();
			System.out.println();
			System.out.println();
			
			
		}
	}

	//Utilizzato dai forkings, mi arrangio utilizzando la mappa così da non ripetere
	//altre istruzioni
	synchronized public void printAllSolutionsOfForkings(){
		
		int i = 0;
		solutions.clear();
		for(State state : solutionsList){
			solutions.put(i, state);
			i++;		
		}
		
		printAllSolutions();		
	}	
	
	
}
