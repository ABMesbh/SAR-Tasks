package echoServerTest;

import communicationChannel.BrokerImpl;
import communicationChannel.ChannelImpl;
import communicationChannel.Task;

import java.util.Arrays;

public class testCommunicationChannel {

    public class ServerTask extends Task {

        public ServerTask( BrokerImpl broker) {
            super(broker);
        }

        @Override
        public void run() {
			while (true) {
				ChannelImpl channel = broker.accept(1999);
				byte[] buffer = new byte[255];
				int bytesRead;

				bytesRead = channel.read(buffer, 0, buffer.length);
				if (bytesRead > 0) {
					channel.write(buffer, 0, bytesRead);
				}
			}
        }
    }

    public class ClientTask extends Task {

        public ClientTask(BrokerImpl broker) {
            super(broker);
        }

        @Override
        public void run() {
			while (true) {
				ChannelImpl channel = broker.connect("Server", 1999);
				// 255 bytes is the size of the circular buffer
				byte[] message = new byte[255];
				for (int i = 1; i <= 255; i++) {
					message[i - 1] = (byte) i;
				}
				
				channel.write(message, 0, message.length);
				byte[] buffer = new byte[255];
				int bytesRead = channel.read(buffer, 0, buffer.length);
				if (bytesRead == -1) {
					System.out.println(broker.getName() + " received nothing");
				} else {
					if (Arrays.equals(message, buffer)) {
						System.out.println(broker.getName() +" received the same message it sent");
					} else {
						System.out.println(broker.getName() +" received a different message");
					}
				}
					
				channel.disconnect();
			}
       }
    }

	public static void main(String[] args) {
        testCommunicationChannel instance = new testCommunicationChannel();

        new Thread(() -> {
			BrokerImpl serverBroker = new BrokerImpl("Server");
            ServerTask serverTask = instance.new ServerTask(serverBroker);            
			serverTask.run();
        }).start();

		for (int i = 0; i < 10; i++) {
			final int clientId = i;
			new Thread(() -> {
				BrokerImpl clientBroker = new BrokerImpl("Client_" + clientId);
				ClientTask clientTask = instance.new ClientTask( clientBroker);
				clientTask.run();
			}).start();
		}
    }
}