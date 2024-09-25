package communicationChannel;

public class testEcho {
	 public static void main(String[] args) {
	        Broker server = new Broker("Server");
	        


	        // Server Task: Accept connections and handle each client in a separate thread
	        new Task(server, () -> {
	            while (true) { // Keep accepting clients
	                Channel channel = server.accept(1234);
	                if (channel != null) {
	                    new Thread(() -> {
	                        byte[] receivedBytes = new byte[1024];
	                        int bytesRead = 0;
							try {
								bytesRead = channel.read(receivedBytes, 0, receivedBytes.length);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                        System.out.println("Server received: " + new String(receivedBytes, 0, bytesRead));
	                        try {
								channel.write(receivedBytes, 0, receivedBytes.length);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                        channel.disconnect();
	                    }).start();
	                }
	            }
	        }).start();	 
		     // Create multiple clients to test the server handling
	        //for (int i = 0; i < 3; i++) {
	            int clientId = 0;
	            new Thread(() -> {
	                Broker client = new Broker("Client" + clientId);
	                Channel channel = client.connect("Server", 1234);
	                if (channel != null) {
	                    byte[] message = ("Hello Server from Client " + clientId).getBytes();
	                    try {
							channel.write(message, 0, message.length);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                    byte[] receivedBytes = new byte[1024];
                        int bytesRead = 0;
						try {
							bytesRead = channel.read(receivedBytes, 0, receivedBytes.length);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        //System.out.println(client.getName() + " received: " + new String(receivedBytes, 0, bytesRead));
						System.out.println("client " + bytesRead);
	                    channel.disconnect();
	                }
	            }).start();
	       // }
	    }

}
