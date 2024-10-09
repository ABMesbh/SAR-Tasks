package implementation;

public class QueueBrokerImpl  {
    private BrokerImpl broker;

    public QueueBrokerImpl(BrokerImpl b) {
        super();
        this.broker = b;
    }

    public String name() {
        return broker.getName();
    }

    public MessageQueueImpl accept(int port) {
        ChannelImpl channel = broker.accept(port);
        return new MessageQueueImpl(channel);
    }

    public MessageQueueImpl connect(String name, int port) {
        ChannelImpl channel = broker.connect(name, port);
        return new MessageQueueImpl(channel);
    }
}

