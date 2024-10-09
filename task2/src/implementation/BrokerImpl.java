package implementation;

import java.util.HashMap;

public class BrokerImpl extends Broker {
    private final BrokerManager brokerManager;
    private final HashMap<Integer, RDV> accepts;
    private  String name;

    public BrokerImpl(String name) {
        super(name);
        this.name = name;
        accepts = new HashMap<>();
        brokerManager = BrokerManager.getInstance();
        brokerManager.add(this);
    }

    
    public ChannelImpl connect(String name, int port) {
        BrokerImpl remoteBroker = (BrokerImpl) brokerManager.get(name);
        // System.out.println("Connecting to " + broker.getName() + " on port " + port);
        if (remoteBroker == null) {
            throw new IllegalStateException("No broker found with name: " + name);
        }
        return remoteBroker._connect(this, port);
    }

    
    public ChannelImpl accept(int port) {
        RDV rdv = new RDV();
        synchronized (accepts) {
            if (accepts.containsKey(port)) {
                throw new IllegalStateException("Port " + port + " already accepting connections.");
            }
            
            accepts.put(port, rdv);
            accepts.notifyAll();
        }
            ChannelImpl channel = rdv.accept(this, port);
            // System.out.println("Accepting connections on port " + port);
            return channel;
        
    }

    private ChannelImpl _connect(BrokerImpl broker, int port) {
        
        synchronized (accepts) {
            // System.out.println("Connecting to " + broker.getName() + " on port " + port);    
            while (!accepts.containsKey(port)) {
                try {
                    accepts.wait();
                } catch (InterruptedException ignored) {}
            }
            RDV rdv = accepts.remove(port);
            return rdv.connect(broker, port);
        }
    }

    
    public String getName() {
        return this.name; // Return the broker name
    }
}
