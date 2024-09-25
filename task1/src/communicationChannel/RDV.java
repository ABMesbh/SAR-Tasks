package communicationChannel;

public class RDV {
    private Broker b_connect;
    private Broker b_accept;

    public Channel connect(Broker b) {
        this.b_connect = b;
        return new Channel(new CircularBuffer(1024), new CircularBuffer(1024)); // Example buffer sizes
    }

    public Channel accept(Broker b) {
        this.b_accept = b;
        return new Channel(new CircularBuffer(1024), new CircularBuffer(1024)); // Example buffer sizes
    }
}
