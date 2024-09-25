package communicationChannel;

public abstract class Task implements Runnable {
    protected Channel channel; // Channel for communication
    protected static Broker broker; // Static reference to the broker

    public Task(Channel channel, Broker broker) {
        this.channel = channel;
        this.broker = broker;
    }

    // Static method to get the broker reference
    public static Broker getBroker() {
        return broker;
    }

    @Override
    public abstract void run();
}