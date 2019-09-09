package tcp;

import java.net.*;
import java.util.Scanner;

import java.io.*;

public class TCPClient {

	// The address the Client will connect to
	public InetSocketAddress serverAddress = null;

	// The client socket
	public Socket clientSocket = null;

	// The in and out stream that we use to talk to the server
	public ObjectOutputStream out = null;
	public ObjectInputStream in = null;
	
	// User's Nickname
	public String nickName;

	/*
	 * The constructor takes the address of the server we want to connect to
	 */
	public TCPClient(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	/*
	 * Starts the client functionality: Creates a new socket and connects to the
	 * server Sets the in and out streams from the connected socket
	 * 
	 * Runs indefinitely expecting input from the client on the console: Get the
	 * message from the console via scanner.nextLine() Send the message to the
	 * Server (via out) Receive the message from the Server (via in)
	 */
	public void startClient() {
		
		clientService s;
		
		try {
			this.clientSocket = new Socket();
			this.clientSocket.connect(this.serverAddress);

			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();

			// Program without a line below works fine. But why together?
			// in = new ObjectInputStream(clientSocket.getInputStream());
			

			Scanner scanner = new Scanner(System.in);
			System.out.println("PLEASE TYPE IN YOUR NICK NAME");
			nickName = scanner.nextLine().trim();

			
			System.out.println(nickName+" ready to send messages!");


			// Create a thread to deal with receiving from server
			s = new clientService(clientSocket);
			Thread newClientThread = new Thread(s);
			newClientThread.start();
				
			
			while (true) {
				String message = scanner.nextLine().trim();
				out.writeObject(nickName+" : "+message);
//				System.out.println(message);
								
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (clientSocket != null) {
				
				try {
					System.out.println("Connection closed!\n");
					clientSocket.close();
					
				} catch (IOException e) {
					e.printStackTrace();
					
				}
			}
		}
	}
	
	
	/*
	 * A class that can be run in a thread to service a client
	 */
	private class clientService implements Runnable {
		Socket serviceSocket = null;
		ObjectInputStream in = null;
//		ObjectOutputStream out = null;

		/*
		 * Sets the server, in and out attributes from the serviceSocket given
		 */
		private clientService(Socket serviceSocket) {
			try {
				this.serviceSocket = serviceSocket;
				this.in = new ObjectInputStream(this.serviceSocket.getInputStream());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * The function that has the logic of the thread: Runs indefinitely expecting
		 * messages from the server. We receive a message from the server from the "in"
		 * stream. We print the message. 
		 */
		public void run() {
			String sender;
			try {
				while (true) {

					
					/* Receive message from the client */
					String message = (String) in.readObject();
					
					sender = message.substring(0,message.indexOf(':')-1);
					
					if (message.contains("-exit-")) serviceSocket.close();
					
					if (nickName.equals(sender)) {
						message="You: "+message.substring(message.indexOf(':')+1);
					}
					
					System.out.println(message);
					
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
	 * Create a TCPClient using an address running at some port (it has to be the
	 * same port the server is running on!)
	 * 
	 * Starts the client functionality.
	 */
	public static void main(String args[]) {
		int port = 3000;
		System.out.println("Enter IP address of server you are connecting to. Type localhost if local");
		Scanner scanner = new Scanner(System.in);
		
		String ip = scanner.nextLine().trim();

		
		InetSocketAddress serverAddress = new InetSocketAddress(ip, port);

		TCPClient client = new TCPClient(serverAddress);
		client.startClient();
	}
}
