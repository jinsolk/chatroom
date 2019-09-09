package tcp;

import java.net.*;
import java.util.Vector;
import java.io.*;

public class TCPServer {

	// The a listen socket to which Clients connect to
	public ServerSocket listenSocket = null;

	// The address to which the server will 'bind' to
	public InetSocketAddress address = null;
	
	// The Vector which maintains services 
	static Vector<Service> ar = new Vector<>();

	/*
	 * Constructor will take in an address as input to bind to
	 */
	public TCPServer(InetSocketAddress address) {
		this.address = address;
	}

	/*
	 * Has all the logic of the server. It does the following: Creates a new
	 * ServerSocket object Binds the socket to the address we are given
	 * 
	 * Starts listening to requests indefinitely Accepts an incoming connection from
	 * a client (blocking method) Creates and starts a new thread that deals with
	 * this new client Rinse and repeat
	 * 
	 */
	public void startServer() {
		Service s;
		int index = 1;
		
		try {
			this.listenSocket = new ServerSocket();
			this.listenSocket.bind(address);

			System.out.println("TCP Server started!");

			while (true) {
				Socket serviceSocket = listenSocket.accept();
				s = new Service(serviceSocket, "Client "+index);
				index++;
				
				Thread newServiceThread = new Thread(s);
				ar.add(s);
				newServiceThread.start();
				
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * A class that can be run in a thread to service a client
	 */
	private class Service implements Runnable {
		Socket serviceSocket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		String id;
		/*
		 * Sets the server, in and out attributes from the serviceSocket given
		 */
		private Service(Socket serviceSocket, String id) {
			try {
				this.id = id;
						
				this.serviceSocket = serviceSocket;
				this.out = new ObjectOutputStream(this.serviceSocket.getOutputStream());
				this.out.flush();
				this.in = new ObjectInputStream(this.serviceSocket.getInputStream());
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * The function that has the logic of the thread: Runs indefinitely expecting
		 * messages from the client We receive a message from the client from the "in"
		 * stream We write the message back to the client using the "out" stream
		 */
		public void run() {
			String nickName;
			try {
				while (true) {
					/* Receive message from the client */
					String message = (String) in.readObject();
					System.out.println(message);
					
					
					/* Echo the message back to the client */
					for (Service s : ar) {
						s.out.writeObject(message);
					}
					
				}
			}

			catch (Exception e) {
				e.printStackTrace();
			}

			finally {
				if (serviceSocket != null)

					try {
						serviceSocket.close();
					}

					catch (Exception e) {
						e.printStackTrace();
					}
			}

		}
	}

	/*
	 * Create a new TCPServer with some port number Starts the server
	 */
	public static void main(String args[]) {
		int port = 3000;
		InetSocketAddress serverAddress = new InetSocketAddress(port);

		TCPServer server = new TCPServer(serverAddress);
		server.startServer();
	}
}

