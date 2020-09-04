package system.thread;

import system.board.Board;
import system.solutionContainer.SolutionContainer;

public class SolutionThread extends Thread{
	
	protected int numberOfQueens;
	protected Board board;
	
	protected int threadId;
	
	private int initialQueenCol; 
	private int initialQueenRow;
	protected int currentQueenId;
	
	private int lastColWherePositioned;
	
	public SolutionThread(int threadId,int numberOfQueens, int initialQueenCol, int initialQueenRow) {
		super();
		this.threadId = threadId;
		this.numberOfQueens = numberOfQueens;
		this.board = new Board(numberOfQueens);
		this.initialQueenCol = initialQueenCol;
		this.initialQueenRow = initialQueenRow;
	}



	@Override
	public void run() {
		//Un thread inizia posizionando la regina iniziale nella board
		currentQueenId = 0;
		if(!board.checkIfPositionAvailable(initialQueenCol, initialQueenRow)){
			System.out.println("Qualcosa è andato storto, il thread con inizio di ");
			
		}			
		
		//Imposta la prima regina
		board.setQueenPosition(currentQueenId, initialQueenCol, initialQueenRow);
		
		//Ora scandisce ogni riga a partire da quella della regina iniziale, quando torno al fondo 
		//della scacchiera torno a zero. Mi fermo quando torno alla stessa riga dell'inizio
		//Per ogni riga cerca una colonna in cui posizionare la nuova regina
		currentQueenId++;
		
		//Ciclo for sulle righe.
		//Devo ciclare finchè non raggiungo il fondo e poi risalgo la scacchiera come se fosse
		//un rounded buffer (fine ed inizio in sequenza) e poi arrivo  alla riga 
		//precedente a quella iniziale
		
		for(int i=((initialQueenRow+1)%numberOfQueens); i != initialQueenRow && i < numberOfQueens ; i++) {
			//Ciclo for sulle colonne
			for(int j=0; j < numberOfQueens; j++){
				if(board.checkIfPositionAvailable(j, i)){
					//Trovata una cella che è ok, ci metto la regina ed interrompo il ciclo
					//delle colonne, per passare alla prossima riga
					board.setQueenPosition(currentQueenId, j, i);
					break;
				} else if(j == numberOfQueens - 1) {					
					//Sono arrivato all'ultima colonna di questa riga ed ho fallito, 
					//devo fare un recovery.
					//Prima però controllo su che riga sono, perchè se sono nella riga successiva
					//alla condizione iniziale, non devo fare il recovery ma dire che ho direttamente
					//fallito (perchè la condizione iniziale deve essere rispettata).
					
					//Il modulo serve a fare il "gioco del rounded buffer" nel calcolo della riga
					//(se la condizione iniziale era sull'ultima riga della scacchiera, la prima 
					// riga da controllare è la prima riga della scacchiera).
					if(i == ((initialQueenRow+1)%numberOfQueens)) {
						SolutionContainer cont =  SolutionContainer.getSolutionContainer();
						//Aggiungo all'elenco delle soluzioni uno stato che ha come
						//valori di alcune regine -1,-1 per dire che è non valido.
						cont.addSolution(threadId, new system.board.state.State(board.getCurrentState()));
						return;
					}else {						
						int[] ret = recovery(i,j);
						i = ret[0];
						j = ret[1];
					}
					
				}
			}
				
			//Controllo se ho raggiunto l'ultima riga e se quella non era la riga di partenza,
			//altrimenti ciclo all'infinito, torno a zero.
			//Per capire se devo fare l'inversione conto il numero di regine a cui sono arrivato,
			//se sono all'ottava regina, il cui id vale 7, vuol dire che le ho fatte tutte.
			if(i == numberOfQueens-1 && currentQueenId != numberOfQueens-1) {
				//Devo mettere -1 perchè tanto con l'incremento di fine 
				//ciclo va a zero
				i=-1;
			}
				
			currentQueenId++;
		}
			
		//Se il codice arriva qui vuol dire che ha funzionato ed è finito.
		//Salvo la soluzione nel container delle soluzioni.
		SolutionContainer cont =  SolutionContainer.getSolutionContainer();
		cont.addSolution(threadId, board.getCurrentState());
	}


	/**
	 * Metodo che torna ad uno stato precedente valido della board.
	 * @return un array, il primo valore contiene il corretto valore dell'indice i, il secondo dell'indice j,
	 * dopo aver eseguito il recovery.
	 */
	private int[] recovery(int i, int j) {
		//Se ho raggiunto l'ultima colonna e non è disponibile la cella, vuol dire che ho fallito, 
		//non è possibile aggiungere una regina su quella riga. 
		//Quindi fallisco e torno allo stato precedente se mi è possibile.
	
		//Recupero l'ultima colonna in cui ho inserito per l'ultima volta una regina,
		//mi serve saperla prima di fare un recovery allo stato antecedente a quell'inserimento
		lastColWherePositioned = board.getLastPositionedCol();
		
		//Faccio un recovery
		board.recoveryThePrecedentOldState();
		//Torno anche indietro di un queen id per modificare la regina giusta nello stato
		currentQueenId--;					
		//Torno indietro di una riga (quella che avevo messo a posto all'iterazione
		//percedente) e torno alla colonna in cui avevo messo quella regina e vado avanti
		//di una colonna.
		i--;
		
		//Una volta tornato indietro di una riga devo stare attento a dove sto
		//tornando, perchè percorrendo il rounded a ritroso devo controllare se sto
		//tornado alla riga -1. Se la riga a cui torno è la -1, vuol dire che devo tornare
		//alla fine del rounded buffer. Faccio questa operazione solo se la riga 0
		//però non è la condizione iniziale, poichè la condizione iniziale non 
		//devo cambiarla.
		if(i == -1 && initialQueenRow != 0) {
			i = numberOfQueens - 1;
		}
		
		//Se l'ultima colonna utilizzata è la settima, allora devo fare un ulteriore recovery
		//in modo ricorsivo.
		if(lastColWherePositioned == numberOfQueens - 1 ) {
			return recovery(i, j);
		}
		
		//Gli metto l'ultimo valore, poi c'è il ++ del fine del ciclo che fa passare
		//alla successiva colonna corretta
		j = lastColWherePositioned;
		
		int ret[] = new int[2];
		ret[0] = i;
		ret[1] = j;	
		
		return ret;		
	}



	public int getNumberOfQueens() {
		return numberOfQueens;
	}



	public Board getBoard() {
		return board;
	}



	public int getThreadId() {
		return threadId;
	}



	public int getInitialQueenCol() {
		return initialQueenCol;
	}



	public int getInitialQueenRow() {
		return initialQueenRow;
	}



	public int getCurrentQueenId() {
		return currentQueenId;
	}



	public int getLastColWherePositioned() {
		return lastColWherePositioned;
	}
	
}
