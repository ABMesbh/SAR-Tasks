package echoServerTest;

import communicationChannel.Broker;
import communicationChannel.Channel;
import communicationChannel.CircularBuffer;
import communicationChannel.Task;

public class EchoServerTest {

    // Classe serveur d'écho, accepte une connexion client, elle lit les données envoyées
    // et les renvoie back to client.
    class EchoServer extends Task {

        public EchoServer(Broker broker) {
            super(broker, null);
        }

        public void run() {
            try {
                Channel channel = getBroker().accept(8080);

                byte[] tempBuffer = new byte[255];
                int bytesRead = channel.read(tempBuffer, 0, tempBuffer.length);

                CircularBuffer buffer = new CircularBuffer(255);
                for (int i = 0; i < bytesRead; i++) {
                    buffer.push(tempBuffer[i]);
                }

                for (int i = 0; i < bytesRead; i++) {
                    channel.write(new byte[]{buffer.pull()}, 0, 1);
                }

                channel.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Classe test client qui se connecte au serveur d'écho, envoie une séquence d'octets (1 à 255)
    // et vérifie que les octets renvoyés par le serveur sont bien les mêmes que ceux envoyés.
    class TestClient extends Task {

        public TestClient(Broker broker) {
            super(broker, null);
        }

        public void runTest() {
            try {
                Channel channel = getBroker().connect("localhost", 8080);
                byte[] sentData = new byte[255];

                for (int i = 0; i < 255; i++) {
                    sentData[i] = (byte) (i + 1);
                }

                channel.write(sentData, 0, sentData.length);

                byte[] receivedData = new byte[255];
                int bytesRead = channel.read(receivedData, 0, receivedData.length);

                assertArrayEquals(sentData, receivedData);

                channel.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Test principal où l'on crée un broker simulé et on démarre le serveur et le client.
    public void testEchoServer() {

        Broker broker = new Broker("TestBroker") ;

        EchoServer server = new EchoServer(broker);
        server.start();

        for (int i = 0; i < 3; i++) {
            TestClient client = new TestClient(broker);
            client.start();
        }
    }

//    // If assertArrayEquals is not working, a simple implementation to do the same work 
//    private void assertArrayEquals(byte[] expected, byte[] actual) {
//        if (expected.length != actual.length) {
//            throw new AssertionError("Array lengths do not match");
//        }
//        for (int i = 0; i < expected.length; i++) {
//            if (expected[i] != actual[i]) {
//                throw new AssertionError("Array contents do not match at index " + i);
//            }
//        }
//    }
}

