package implementation;

// Copied from the last SAR session
public class ChannelImpl extends Channel {
    private final int port;
    private final CircularBuffer in;
    private CircularBuffer out;
    private ChannelImpl remoteChannel;
    private boolean disconnected = false;
    private String remoteName;

    public ChannelImpl(BrokerImpl broker, int port) {
        super(broker);
        this.port = port;
        this.in = new CircularBuffer(256);
    }

    public void connect(ChannelImpl remoteChannel, String remoteName) {
        this.remoteChannel = remoteChannel;
        this.remoteName = remoteName;
        this.out = remoteChannel.in;
        remoteChannel.out = this.in;
    }

    @Override
    public void disconnect() {
        synchronized (this) {
            if (disconnected) {
                return;
            }
            disconnected = true;
        }
        synchronized (out) {
            out.notifyAll();
        }
        synchronized (in) {
            in.notifyAll();
        }
    }

    @Override
    public boolean disconnected() {
        return disconnected;
    }

    @Override
    public int read(byte[] bytes, int offset, int length) {
        int nbytes = 0;
        synchronized (in) {
            if (disconnected) {
                throw new IllegalStateException("Read error - Channel is disconnected.");
            }
            while (nbytes == 0 && !disconnected) {
                if (in.empty()) {
                    try {
                        in.wait();
                    } catch (InterruptedException ignored) {}
                }
                while (nbytes < length && !in.empty()) {
                    bytes[offset + nbytes] = in.pull();
                    nbytes++;
                }
            }
            
        }
        return nbytes;
    }

    @Override
    public int write(byte[] bytes, int offset, int length) {
        int nbytes = 0;
        synchronized (out) {
            if (disconnected) {
                throw new IllegalStateException("Write error - Channel is disconnected.");
            }
            while (nbytes == 0 && !disconnected) {
                if (out.full()) {
                    try {
                        out.wait();
                    } catch (InterruptedException ignored) {}
                }
                while (nbytes < length && !out.full() && !disconnected) {
                    out.push(bytes[offset + nbytes]);
                    nbytes++;
                }
            }
            
            out.notifyAll();
        }
        return nbytes;
    }
}
