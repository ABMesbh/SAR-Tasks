package implementation;

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
    public abstract String getName();
}
