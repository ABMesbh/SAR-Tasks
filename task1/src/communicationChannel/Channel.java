package communicationChannel;

public class Channel {
    private final CircularBuffer localInBuffer;
    private final CircularBuffer remoteOutBuffer;
    private volatile boolean isConnected;

    public Channel(CircularBuffer localInBuffer, CircularBuffer remoteOutBuffer) {
        this.localInBuffer = localInBuffer;
        this.remoteOutBuffer = remoteOutBuffer;
        this.isConnected = true;
    }

    public synchronized int write(byte[] bytes, int offset, int length) {
        if (!isConnected) {
            throw new RuntimeException("Channel is disconnected!");
        }

        int written = 0;
        for (int i = offset; i < offset + length; i++) {
            if (remoteOutBuffer.full()) {
                break; // If buffer is full, stop writing
            }
            remoteOutBuffer.push(bytes[i]);
            written++;
        }
        return written; // Return the number of bytes written
    }

    public synchronized int read(byte[] bytes, int offset, int length) {
        if (!isConnected) {
            throw new RuntimeException("Channel is disconnected!");
        }

        int read = 0;
        for (int i = offset; i < offset + length; i++) {
            if (localInBuffer.empty()) {
                break; // If buffer is empty, stop reading
            }
            bytes[i] = localInBuffer.pull();
            read++;
        }
        return read; // Return the number of bytes read
    }

    public void disconnect() {
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
