package system.thread.forkingThread;

/**
 * 
 * @author Guglielmo Cassini
 * Classe singleton, utilizzata per contare quanti ForkingThread sono in esecuzione
 * in un determinato momento. Viene utilizzato per capire quando tutti i thread
 * hanno finito di lavorare.
 */
public class ForkingThreadLauncher{

	//Utilizzo un contatore che conta il numero di thread che vengono
	//creati e viene decrementato quando questi thread terminano.
	//Quando questo contatore varrà zero, il main stamperà le soluzioni
	private volatile int counter;
	
	private static ForkingThreadLauncher launcher;
	
	private ForkingThreadLauncher() {
		counter = 0;
	}
	
	static public ForkingThreadLauncher getForkingThreadLauncher() {
		if(launcher == null)
			launcher = new ForkingThreadLauncher();
		
		return launcher;
	}
	
	public synchronized void incrementCounter(){
		counter++;
	}
	
	public synchronized void decrementCounter(){
		counter--;
		notifyAll();
	}

	public synchronized int getCounter() {
		return counter;
	}
	
	public synchronized void awaitCounterEqualsZero(){
		while(counter != 0){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
