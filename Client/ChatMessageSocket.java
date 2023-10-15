package Client;
import java.net.Socket;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class ChatMessageSocket {
	private Socket socket;
	
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private final int Port = 9000;
    

    public ChatMessageSocket(String address) throws IOException {
        socket = new Socket(address, Port);
        objectOut = new ObjectOutputStream(socket.getOutputStream());        
        objectIn = new ObjectInputStream(socket.getInputStream());
    }
	
	public void send(Object message) {
        try {
            objectOut.writeObject(message);
            objectOut.flush();
            // System.out.println("SENDED");
        } catch (IOException e) {
            close();
        }
	}

	public Object readData() {
        Object obj = null;
        try {
            obj = objectIn.readObject();
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            close();
        }
        return obj;
    }
	
	
	public boolean isActive() { return !socket.isClosed(); }
	
	public void close() {
        // System.out.println("CLOSE SOCKET");
        try {
            objectOut.close();
            objectIn.close();
            socket.close();
            
        } catch (IOException e) {
            // e.printStackTrace();
        }
	}
}
