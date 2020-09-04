package system.thread.forkingThread;

import java.util.ArrayList;
import java.util.List;

import system.board.Board;
import system.solutionContainer.SolutionContainer;
import system.thread.SolutionThread;

/**
 * 
 * @author Guglielmo Cassini
 * Classe che rappresenta un "thread biforcante", ovvero un thread che posiziona
 * una regina in una determinata casella specificata alla sua creazione.
 * Dopo il posizionamento questo thread controlla nella successiva riga quali
 * caselle sono disponibili. Trovato un certo numero di caselle disponibili crea
 * un numero N di copie di sè stesso per ogni casella disponibile sulla riga successiva.
 * Assegna quindi ad ognuno di questi thread una casella libera e questi thread lavorano
 * allo stesso modo di quello di partenza.
 */
public class ForkingThread extends SolutionThread {

	
	private int nextCol;
	private int nextRow;
	
	//Costruttore usato quando è il primo thred ad essere lanciato
	public ForkingThread(int threadId, int numberOfQueens, int initialQueenCol, int initialQueenRow) {
		super(threadId, numberOfQueens, initialQueenCol, initialQueenRow);
		this.nextCol = initialQueenCol;
		this.nextRow = initialQueenRow;
		//Il currentQueenId viene inizializzato dalla classe madre
		incrementThreadCounter();
	}
	
	//Costruttore invocato quando è eseguita una biforcazione
	public ForkingThread(ForkingThread thread, int nextCol, int nextRow){
		super(thread.getThreadId(), thread.getNumberOfQueens(), thread.getInitialQueenCol(), thread.getInitialQueenRow());		
		setNextMove(nextCol,nextRow);
		this.board = new Board(thread.getBoard());
		super.currentQueenId = thread.getCurrentQueenId();
		incrementThreadCounter();

	}
	
	private void incrementThreadCounter(){
		ForkingThreadLauncher launcher = ForkingThreadLauncher.getForkingThreadLauncher();
		launcher.incrementCounter();
	}
	
	private void decrementThreadCounter(){
		ForkingThreadLauncher launcher = ForkingThreadLauncher.getForkingThreadLauncher();
		launcher.decrementCounter();
	}
	
	
	public void setNextMove(int nextCol, int nextRow){
		this.nextCol = nextCol;
		this.nextRow = nextRow;
	}

	public int getNextCol() {
		return nextCol;
	}

	public int getNextRow() {
		return nextRow;
	}
	
	
	@Override
	public synchronized void start() {
		//Un forking thread è un thread che ha raggiunto un certo stato e da esso 
		//vuole partire eseguendo la nuova mossa successiva
		//Un forking thread ha la sua board ed esegue la mossa che gli è stata
		//imposta alla nascita.
		if(!board.checkIfPositionAvailable(nextCol, nextRow)) {
			//Se succede è un erorre perchè è stato utilizzato male la classe
			System.out.println("Cattivo utilizzo del forking thread");
			decrementThreadCounter();
			return;
		}
		
		board.setQueenPosition(super.currentQueenId, nextCol, nextRow);
		super.currentQueenId++;
		
		//Per prima cosa controllo se ho finito, ovvero se lo stato corrente della
		//mia board è uno stato valido. Se è uno stato valido mi fermo dopo aver 
		//aggiunto la soluzione al SolutionContainer
		
		if(board.getCurrentState().isValid()) { 
			System.out.println("Soluzione valida");
			SolutionContainer cont = SolutionContainer.getSolutionContainer();
			cont.addSolution(board.getCurrentState());
			decrementThreadCounter();
			return;
		}else if(nextRow + 1 == numberOfQueens) {
			System.out.println("Finito arrivato a riga 8");
			decrementThreadCounter();
			board.printQueenState();
			return;
		}else {
			//System.out.println("Non ho finito");
			//Se non ho finito; ora devo cercare tutte le altre celle disponibili sulla riga successiva
			//che è next row  + 1 e lanciare altrettanti thread su quelle colonne disponibili
			
			//Ricerca delle colonne disponibili nella riga successiva
			List<Integer> freeAfterMyLastMove = new ArrayList<Integer>();
			
			for(int i=0; i < numberOfQueens; i++) {
				if(board.checkIfPositionAvailable(i, nextRow+1)) {
					//Se quella colonna è disponibile, la metto nella lista
					freeAfterMyLastMove.add(i);
				}		
			}
	
			
			
			//Ora che ho la lista delle colonne disponibili posso lanciare un nuovo
			//forking thread per ogni nuova posizione disponibile
			//Creo una lista dei nuovi forking thread, che userò successivamente per 
			//mettermi nella loro attesa
			
			
			for(Integer number : freeAfterMyLastMove){
				ForkingThread fork = new ForkingThread(this, number, nextRow+1);
				fork.start();
			}
			
			//Nota bene: quando un thread non ha una successiva mossa disponibile, semplicemente
			//muore diventando fermo
			
			//Decremento il contatore di thread esistenti
			decrementThreadCounter();
			//System.out.println("Morto");
		}
	}
}
