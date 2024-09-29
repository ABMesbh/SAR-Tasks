package communicationChannel;

public abstract class Task implements Runnable {
    protected BrokerImpl broker;

    public Task(BrokerImpl broker) {
        this.broker = broker;
    }

    public static BrokerImpl getBroker(String name) {
        return BrokerManager.getInstance().get(name);
    }
}
