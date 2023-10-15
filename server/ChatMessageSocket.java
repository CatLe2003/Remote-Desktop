package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.lang.Object;



public class ChatMessageSocket {
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private final int Port = 9000;
	public ChatMessageSocket() throws IOException {
		InetAddress address = InetAddress.getLocalHost();
		System.out.println(address.toString());
		serverSocket = new ServerSocket(Port);
		socket = serverSocket.accept();
		objectOut = new ObjectOutputStream(socket.getOutputStream());
		objectIn = new ObjectInputStream(socket.getInputStream());
	}


	public void send(Object message) {
		try {				
			objectOut.writeObject(message);;
			objectOut.flush();

		} catch (IOException e) {
			close();
		}
	}


	public boolean isActive() { return !socket.isClosed(); }

	public Object readData() {
		Object obj = null;
		
		try {
			obj = objectIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			close();
		}
		return obj;
	}
	

	
	public void close() {
		// System.out.println("CLOSE SOCKET");
		try {
			objectOut.close();
			objectIn.close();
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
