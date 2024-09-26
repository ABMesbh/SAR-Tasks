package communicationChannel;

public abstract class Task implements Runnable {
    protected ChannelImpl channel;
    protected BrokerImpl broker;

    public Task(ChannelImpl channel, BrokerImpl broker) {
        this.channel = channel;
        this.broker = broker;
    }

    public static BrokerImpl getBroker(String name) {
        return BrokerManager.getInstance().get(name);
    }
}
