package specification;

public abstract class MessageQueue {  
    void send(byte[] bytes, int offset, int length);  
    byte[] receive();  
    void close();  
    boolean closed(); 
}
