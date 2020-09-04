package main;

import system.solutionContainer.SolutionContainer;
import system.thread.forkingThread.ForkingThread;
import system.thread.forkingThread.ForkingThreadLauncher;

public class ForkingMain {

	public static void main(String[] args) {
		final int numberOfQueens =  8;
		
		//Creo un determinato numero di forking thread
		for(int i = 0; i < numberOfQueens; i++){
			Thread t = new ForkingThread(i, numberOfQueens, i, 0);	
			t.start();
		}
		
		//Mi metto in attesa che il counter del launche diventi nullo
		//quando è nullo posso stampare tutte le soluzioni
		
		ForkingThreadLauncher launcher = ForkingThreadLauncher.getForkingThreadLauncher();
		//Problema: questa soluzione ha un attesa attiva
		//while(launcher.getCounter() != 0);
		launcher.awaitCounterEqualsZero();
		
		//Se è uscito dal ciclo siamo pronti a stampare le soluzioni
		SolutionContainer container = SolutionContainer.getSolutionContainer();
		container.printAllSolutionsOfForkings();
	}

}
