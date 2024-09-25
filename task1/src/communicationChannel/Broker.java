package communicationChannel;

import java.util.HashMap;
import java.util.Map;

public class Broker {
    private final String name;
    private final Map<Integer, Channel> channels = new HashMap<>();
    private final BrokerManager manager;
    private final RDV rdv;

    public Broker(String name) {
        this.name = name;
        this.manager = BrokerManager.getInstance();
        this.rdv = new RDV();
        manager.registerBroker(name, this);
    }

    public Channel accept(int port) {
        synchronized (manager) {
            Channel existing = manager.getPendingChannel(name, port);
            if (existing != null) {
                return existing;
            }

            // Register itself to wait for a client connection
            manager.registerAccept(name, port);
            while (!manager.isConnected(name, port)) {
                try {
                    manager.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return manager.getConnectedChannel(name, port);
        }
    }

    public Channel connect(String remoteBrokerName, int port) {
        synchronized (manager) {
            Broker remoteBroker = manager.getBroker(remoteBrokerName);
            if (remoteBroker == null) {
                return null;
            }

            Channel clientChannel = rdv.connect(remoteBroker);
            Channel serverChannel = rdv.accept(this);

            manager.addPendingChannel(remoteBrokerName, port, serverChannel);

            synchronized (manager) {
                manager.notifyAll(); // Wake up the broker accepting on this port
            }
            return clientChannel;
        }
    }

    public String getName() {
        return name;
    }
}
