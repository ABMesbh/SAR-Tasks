package specification;

abstract class Task extends Thread {  
    Task(Broker b, Runnable r);  
    Task(QueueBroker b, Runnable r);  
    Broker getBroker();  
    QueueBroker getQueueBroker();  
    static Task getTask(); 
}
