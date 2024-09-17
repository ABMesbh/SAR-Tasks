package communicationChannel;

public abstract class Task extends Thread {  
	Broker broker;
	Runnable runnable;
	
	Task(Broker b, Runnable r) {
        this.broker = b;
        this.runnable = r;
    }
	
	public void run() {
		runnable.run();
	}
	
	 static Broker getBroker();	
}
