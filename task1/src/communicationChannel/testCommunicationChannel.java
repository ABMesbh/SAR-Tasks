package communicationChannel;

public class testCommunicationChannel {

    public class ServerTask extends Task {

        public ServerTask(ChannelImpl channel, BrokerImpl broker) {
            super(channel, broker);
        }

        @Override
        public void run() {
            byte[] buffer = new byte[32];
            int bytesRead = channel.read(buffer, 0, buffer.length);
            System.out.println("Server received: " + new String(buffer, 0, bytesRead));
            channel.disconnect();
        }
    }

    public class ClientTask extends Task {

        public ClientTask(ChannelImpl channel, BrokerImpl broker) {
            super(channel, broker);
        }

        @Override
        public void run() {
            byte[] message = "Hello, Server!".getBytes();
            channel.write(message, 0, message.length);
            channel.disconnect();
        }
    }

	public static void main(String[] args) {
        testCommunicationChannel instance = new testCommunicationChannel();

        BrokerImpl serverBroker = new BrokerImpl("Server");
        BrokerImpl clientBroker = new BrokerImpl("Client");

        new Thread(() -> {
            ChannelImpl serverChannel = serverBroker.accept(8080);
            ServerTask serverTask = instance.new ServerTask(serverChannel, serverBroker);
            serverTask.run();
        }).start();

        
        new Thread(() -> {
            ChannelImpl clientChannel = clientBroker.connect("Server", 8080);
            ClientTask clientTask = instance.new ClientTask(clientChannel, clientBroker);
            clientTask.run();
        }).start();
    }
}