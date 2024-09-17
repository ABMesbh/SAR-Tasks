package communicationChannel;

public abstract class Broker {
    private String name;

    public Broker(String name) {
        this.name = name;
    }

    public abstract Channel accept(int port) ;
    public abstract Channel connect(String name, int port) ;

    public String getName() {
        return this.name;
    }
}