package communicationChannel;

import java.io.IOException;

public abstract class Channel {  
	
	int read(byte[] bytes, int offset, int length) throws IOException;
	int write(byte[] bytes, int offset, int length) throws IOException;
	
	void disconnect();  
	boolean disconnected(); 
}
