package tcp;
import java.net.*;
import java.io.*;

public class UDPServer 
{
	public static void main(String args[])
	{	
		DatagramSocket socket = null;
		
		try
		{	
			socket = new DatagramSocket(6700);
			byte receive_packet [] = new byte[1000];
			
			while(true){
				
				DatagramPacket packet = new DatagramPacket(receive_packet, receive_packet.length);
				socket.receive(packet);
				
				System.out.println("Server Received: " + new String(packet.getData()));
				
				DatagramPacket reply = new DatagramPacket(packet.getData(), packet.getLength(), packet.getAddress(), packet.getPort());
				socket.send(reply);
			}
			
		}
		catch(SocketException se)
		{
			se.printStackTrace();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			if(socket != null)
				socket.close();
		}
	}
}
