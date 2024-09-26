package communicationChannels;

public abstract class Channel {
    Channel(Broker broker);
    int read(byte[] bytes, int offset, int length);  
    int write(byte[] bytes, int offset, int length);  
    void disconnect();  
    boolean disconnected();
}
