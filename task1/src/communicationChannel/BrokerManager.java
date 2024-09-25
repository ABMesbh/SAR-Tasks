package communicationChannel;

import java.util.HashMap;
import java.util.Map;

public class BrokerManager {
    private static BrokerManager instance;

    private final Map<String, Broker> brokers = new HashMap<>();
    private final Map<String, Map<Integer, Channel>> pendingChannels = new HashMap<>();

    private BrokerManager() {}

    public static synchronized BrokerManager getInstance() {
        if (instance == null) {
            instance = new BrokerManager();
        }
        return instance;
    }

    public void registerBroker(String name, Broker broker) {
        brokers.put(name, broker);
    }

    public Broker getBroker(String name) {
        return brokers.get(name);
    }

    public void addPendingChannel(String brokerName, int port, Channel channel) {
        pendingChannels.computeIfAbsent(brokerName, k -> new HashMap<>()).put(port, channel);
    }

    public Channel getPendingChannel(String brokerName, int port) {
        return pendingChannels.getOrDefault(brokerName, new HashMap<>()).get(port);
    }

    public boolean isConnected(String brokerName, int port) {
        return getPendingChannel(brokerName, port) != null;
    }

    public void registerAccept(String brokerName, int port) {
        addPendingChannel(brokerName, port, null);
    }

    public Channel getConnectedChannel(String brokerName, int port) {
        return getPendingChannel(brokerName, port);
    }
}

