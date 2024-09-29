package communicationChannel;

public abstract class Broker {
    protected String name;
    Broker(String name) {
        this.name = name;
    }  
    Channel accept(int port) {
        return accept(port);
    }
    Channel connect(String name, int port) {
        return connect(name, port);
    }
    protected abstract String getName();
}
