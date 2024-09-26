package communicationChannel;

public abstract class Broker {
    Broker(String name);  
    Channel accept(int port);
    Channel connect(String name, int port);
    protected abstract String getName();
}
