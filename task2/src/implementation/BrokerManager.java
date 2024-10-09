package implementation;

import java.util.HashMap;

public class BrokerManager {
    private static BrokerManager self;
    private HashMap<String, BrokerImpl> brokers;

    private BrokerManager() {
        brokers = new HashMap<>();
    }

    // Singleton instance getter
    public static BrokerManager getInstance() {
        if (self == null) {
            self = new BrokerManager();
        }
        return self;
    }

    public synchronized void add(BrokerImpl broker) {
        String name = broker.getName();
        if (brokers.containsKey(name)) {
            throw new IllegalStateException("Broker " + name + " already exists");
        }
        brokers.put(name, broker);
    }

    public synchronized void remove(BrokerImpl broker) {
        brokers.remove(broker.getName());
    }

    public synchronized BrokerImpl get(String name) {
        return brokers.get(name);
    }
}
