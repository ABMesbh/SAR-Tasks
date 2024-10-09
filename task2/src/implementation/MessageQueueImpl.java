package implementation;

import java.io.EOFException;
import java.util.Arrays;

// import specification.MessageQueue;

public class MessageQueueImpl {
    private final ChannelImpl channel;
    private boolean closed;

    public MessageQueueImpl(ChannelImpl channel) {
        this.channel = channel;
        this.closed = false;
    }

    public synchronized void send(byte[] bytes, int offset, int length) {
        // Send length as 4 bytes
        byte[] lengthBytes = {
            (byte) (length >> 24),
            (byte) (length >> 16),
            (byte) (length >> 8),
            (byte) length
        };
        writeToChannel(lengthBytes);

        // Send message in chunks
        int sentBytes = 0;
        while (sentBytes < length) {
            int toSend = Math.min(256, length - sentBytes);
            channel.write(bytes, offset + sentBytes, toSend);
            sentBytes += toSend;
        }
    }

    public synchronized byte[] receive() throws EOFException {
        // Read length
        byte[] lengthBytes = readFromChannel(4);
        int length = ((lengthBytes[0] & 0xFF) << 24) 
                   | ((lengthBytes[1] & 0xFF) << 16) 
                   | ((lengthBytes[2] & 0xFF) << 8) 
                   | (lengthBytes[3] & 0xFF);

        // Read message in chunks
        byte[] message = new byte[length]; 
        int totalBytes = 0;

        while (totalBytes < length-1) {
            byte[] chunk = new byte[Math.min(256, length - totalBytes)];
            int received = channel.read(chunk, 0, chunk.length);  

            if (received <= 0) {
                break;  // No more data to read
            }

            System.arraycopy(chunk, 0, message, totalBytes, received);
            totalBytes += received;
        }

        return Arrays.copyOf(message, totalBytes);  // Return the received message
    }

    public void close() {
        closed = true;
        channel.disconnect();
    }

    public boolean closed() {
        return closed;
    }

    private void writeToChannel(byte[] data) {
        int bytesWritten = 0;
        while (bytesWritten < data.length) {
            bytesWritten += channel.write(data, bytesWritten, data.length - bytesWritten);
        }
    }

    private byte[] readFromChannel(int length) throws EOFException {
        byte[] buffer = new byte[length];
        int bytesRead = 0;
        while (bytesRead < length) {
            int result = channel.read(buffer, bytesRead, length - bytesRead);
            if (result == -1) {
                throw new EOFException("End of stream reached.");
            }
            bytesRead += result;
        }
        return buffer;
    }
}
