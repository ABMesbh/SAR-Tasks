package specification;

public abstract class QueueBroker {  
    
    QueueBroker(BrokerImpl broker);
    String name();  
    MessageQueue accept(int port);  
    MessageQueue connect(String name, int port); 
}
