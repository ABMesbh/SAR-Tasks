package communicationChannel;

import java.util.HashMap;

public class BrokerImpl extends Broker {
    private final BrokerManager brokerManager;
    private final HashMap<Integer, RDV> accepts;
    private String name;

    public BrokerImpl(String name) {
        super(name);
        accepts = new HashMap<>();
        brokerManager = BrokerManager.getInstance();
        brokerManager.add(this);
    }

    @Override
    public ChannelImpl connect(String name, int port) {
        BrokerImpl remoteBroker = (BrokerImpl) brokerManager.get(name);
        if (remoteBroker == null) {
            throw new IllegalStateException("No broker found with name: " + name);
        }
        return remoteBroker._connect(this, port);
    }

    @Override
    public ChannelImpl accept(int port) {
        synchronized (accepts) {
            if (accepts.containsKey(port)) {
                throw new IllegalStateException("Port " + port + " already accepting connections.");
            }
            RDV rdv = new RDV();
            accepts.put(port, rdv);
            ChannelImpl channel = rdv.accept(this, port);
            return channel;
        }
    }

    private ChannelImpl _connect(BrokerImpl broker, int port) {
        synchronized (accepts) {
            while (!accepts.containsKey(port)) {
                try {
                    accepts.wait();
                } catch (InterruptedException ignored) {}
            }
            RDV rdv = accepts.remove(port);
            return rdv.connect(broker, port);
        }
    }

    @Override
    protected String getName() {
        return this.name; // Return the broker name
    }
}
