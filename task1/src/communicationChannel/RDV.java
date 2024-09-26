package communicationChannel;

public class RDV {
    private ChannelImpl ac;  // accepting channel
    private ChannelImpl cc;  // connecting channel
    private BrokerImpl ab;   // accepting broker
    private BrokerImpl cb;   // connecting broker

    // Synchronized wait method to avoid busy waiting
    private synchronized void waitForConnection() {
        while (ac == null || cc == null) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
    }

    public synchronized ChannelImpl connect(BrokerImpl cb, int port) {
        this.cb = cb;
        cc = new ChannelImpl(cb, port);

        if (ac != null) {
            ac.connect(cc, cb.getName());
            notifyAll();
        } else {
            waitForConnection();
        }
        return cc;
    }

    public synchronized ChannelImpl accept(BrokerImpl ab, int port) {
        this.ab = ab;
        ac = new ChannelImpl(ab, port);

        if (cc != null) {
            ac.connect(cc, ab.getName());
            notifyAll();
        } else {
            waitForConnection();
        }
        return ac;
    }
}
