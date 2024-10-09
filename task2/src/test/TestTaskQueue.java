package test;

import implementation.BrokerImpl;
import implementation.MessageQueueImpl;
import implementation.QueueBrokerImpl;
import implementation.Task;

public class TestTaskQueue {

    public static void main(String[] args) {
        BrokerImpl bs = new BrokerImpl("SenderBroker");
        BrokerImpl br = new BrokerImpl("ReceiverBroker");

        QueueBrokerImpl qbs = new QueueBrokerImpl(bs);
        QueueBrokerImpl qbr = new QueueBrokerImpl(br);

        
        Task senderTask = new Task(qbs, () -> {
            try {
                
                QueueBrokerImpl qb = Task.getTask().getQueueBroker();
                MessageQueueImpl queue = qb.connect("ReceiverBroker", 9999);
                byte[] message = new byte[1025];
				for (int i = 1; i <= 1024; i++) {
					message[i - 1] = (byte) i;
				}
                queue.send(message, 0, message.length);
                System.out.println("Sender sent a message of length: " + message.length);
                // queue.close();
            } catch (Exception e) {
                System.out.println("Error in sender task: " + e.getMessage());
            }
        });

        // Create a message receiver task
        Task receiverTask = new Task(qbr, () -> {
            try {
                QueueBrokerImpl qb = Task.getTask().getQueueBroker();
                MessageQueueImpl queue = qb.accept(9999);
                byte[] received = queue.receive();
                System.out.println("Receiver received: " + new String(received).trim());
                System.out.println("Receiver received: " + received.length);
                // queue.close();
            } catch (Exception e) {
                System.out.println("Error in receiver task: " + e.getMessage());
            }
        });

        // Start both tasks
        receiverTask.start();
        senderTask.start();
        
        // Wait for both tasks to complete
        try {
            senderTask.join();
            receiverTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Test finished.");
    }
}
