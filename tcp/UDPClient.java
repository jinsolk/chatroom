package tcp;
import java.net.*;
import java.io.*;

/** 	getByName returns the IP address of a host, given the hostname.
	A hostname is the name of the server consisting of numbers and
	letters, and that uniquely identifies the server.

	To find the hostname of a windows machine: open a command prompt,
	type ipconfig/all, and press enter. The hostname will appear at 
        the top, under the section Windows IP Configuration. 

	To find the IP of a windows machine: follow the above steps. The IP 
	address will be under the sections Wireless LAN adapter or 
	Ethernet adapter depending on your type of internet connection
        (wireless or wired respecitvely).

	To find the hostname of a linux machine: open a terminal, type 
	hostname, and enter.

	To find the IP address of a linux machine: open a terminal, either
	type "ip addr show" or ifconfig. The IP address will be under the
	appropriate section depending on your type of internet connection
	(wireless or wired).
		    	
	*/

public class UDPClient 
{
	public static void main(String args[])
	{	
		DatagramSocket socket = null;
		
		try
		{			
			socket = new DatagramSocket();
			byte request_packet [] = args[0].getBytes();
	  
			DatagramPacket request = new DatagramPacket(request_packet, request_packet.length, InetAddress.getByName(args[1]), 6700);
		
			for(int i = 0; i < 10; i++)
			{
				socket.send(request);
				byte reply_packet[] = new byte[1000];
		
				DatagramPacket reply = new DatagramPacket(reply_packet, reply_packet.length);
		
				socket.receive(reply);

				System.out.println("Client Received: " + new String(reply.getData()));
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
