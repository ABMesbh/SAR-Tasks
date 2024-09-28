package communicationChannel;

public class testCommunicationChannel {

    public class ServerTask extends Task {

        public ServerTask(ChannelImpl channel, BrokerImpl broker) {
            super(channel, broker);
        }

        @Override
        public void run() {
			
				byte[] buffer = new byte[255];
				int bytesRead;
				bytesRead = channel.read(buffer, 0, buffer.length);
				if (bytesRead > 0) {
					channel.write(buffer, 0, bytesRead);
				}
           		 
        }
    }

    public class ClientTask extends Task {

        public ClientTask(ChannelImpl channel, BrokerImpl broker) {
            super(channel, broker);
        }

        @Override
        public void run() {
			
				byte[] message = new byte[255];
				for (int i = 1; i <= 255; i++) {
					message[i - 1] = (byte) i;
				}
				
				channel.write(message, 0, message.length);
				byte[] buffer = new byte[255];
				int bytesRead = channel.read(buffer, 0, buffer.length);
				String sent =  new String(message, 0, bytesRead);
				String received =  new String(buffer, 0, bytesRead);
				
				if (bytesRead == -1) {
					System.out.println(broker.getName() + " received nothing");
				} else {
					if (sent.equals(received)) {
						System.out.println(broker.getName() +" received the same message it sent");
					} else {
						System.out.println(broker.getName() +" received a different message");
					}
				}
					
				channel.disconnect();
			
       }
    }

	public static void main(String[] args) {
        testCommunicationChannel instance = new testCommunicationChannel();

        new Thread(() -> {
			BrokerImpl serverBroker = new BrokerImpl("Server");
			while (true) {
            ChannelImpl serverChannel = serverBroker.accept(1999);
            ServerTask serverTask = instance.new ServerTask(serverChannel, serverBroker);            
			serverTask.run();
			}
        }).start();

		for (int i = 0; i < 10; i++) {
			final int clientId = i;
			new Thread(() -> {
				BrokerImpl clientBroker = new BrokerImpl("Client_" + clientId);
				while (true) {
					ChannelImpl clientChannel = clientBroker.connect("Server", 1999);
					ClientTask clientTask = instance.new ClientTask(clientChannel, clientBroker);
					
					clientTask.run();
				}
			}).start();
		}
    }
}