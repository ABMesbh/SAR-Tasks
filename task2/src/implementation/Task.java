package implementation;

public class Task extends Thread {
    private BrokerImpl broker;
    private QueueBrokerImpl QBroker;
    private Runnable runnable;

    Task(BrokerImpl b, Runnable r) {
        this.broker = b;
        this.runnable = r;
    }

    public Task(QueueBrokerImpl b, Runnable r) {
        this.QBroker = b;
        this.runnable = r;
    }

    public Broker getBroker() {
        return broker;
    }

    public QueueBrokerImpl getQueueBroker() {
        return QBroker;
    }

    public static Task getTask() {
        return (Task) Thread.currentThread();
    }
    public void run() {
        runnable.run();
    }
    
}
