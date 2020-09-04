package main;

import java.util.ArrayList;
import java.util.List;

import system.solutionContainer.SolutionContainer;
import system.thread.SolutionThread;

public class Main {

	public static void main(String[] args) {
		
		//Thread list
		List<Thread> threadList =  new ArrayList<Thread>();
		
		final int numberOfQueens =  8;
		
		//int initialQueenCol = 0;
		//int initialQueenRow = 0;
		int threadId = 0;
		
		
		//Faccio un ciclo dove lancio le 64 inizializzazioni 
		for(int initialQueenCol = 0; initialQueenCol < numberOfQueens; initialQueenCol++) {
			for(int initialQueenRow = 0; initialQueenRow < numberOfQueens; initialQueenRow++) {
				Thread t = new SolutionThread(threadId, numberOfQueens, initialQueenCol, initialQueenRow);
				t.start();
				threadList.add(t);
				threadId++;
			}
		}
		
		//Mi metto in attesa che ogni thread abbia finito
		for(Thread t : threadList) {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Tutti i thread hanno finito, ecco i risultati");
		SolutionContainer cont =  SolutionContainer.getSolutionContainer();
		cont.printAllSolutions();
		
		
		
	}

}
