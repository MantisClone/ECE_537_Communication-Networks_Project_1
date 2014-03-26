/**
 * 
 */
package edu.wisc.simpleparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author David
 *
 */
public class Main {

	private static void parsePcapFile(PcapParser pp, String fileName)
	{
		ArrayList <Packet> overallPacketArray = new ArrayList<Packet>();
		ArrayList <TCPPacket> tcpPacketArray = new ArrayList<TCPPacket>();
		ArrayList <UDPPacket> udpPacketArray = new ArrayList<UDPPacket>();
		ArrayList <IPPacket> ipPacketArray = new ArrayList<IPPacket>();
		ArrayList <Packet> otherPacketArray = new ArrayList<Packet>();
		Packet packet;
		
		System.out.println("Parsing " + fileName);
		while((packet = pp.getPacket()) != Packet.EOF)
		{
			// Add packet to the overall Array
			overallPacketArray.add(packet);
			
			// Sort the packets according to packet type for easy grouping.
			if (packet instanceof TCPPacket)
			{
				tcpPacketArray.add((TCPPacket) packet);
			}
			else if (packet instanceof UDPPacket)
			{
				udpPacketArray.add((UDPPacket) packet);
			}
			else if (packet instanceof IPPacket)
			{
				ipPacketArray.add((IPPacket) packet);
			}
			else
			{
				otherPacketArray.add(packet);
			}
		}
		
		System.out.println("TCP Packets: " + tcpPacketArray.size());
		System.out.println("UDP Packets: " + udpPacketArray.size());
		System.out.println("IP Packets: " + ipPacketArray.size());
		System.out.println("Other Packets: " + otherPacketArray.size());		
		
		System.out.println("Creating " + fileName + "_Output.txt");
		
		try {
			File file = new File(fileName + "_Output.txt");
			
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			int count = 0;
			String output;
			for(Packet p : overallPacketArray)
			{
				count++;
				if (p instanceof TCPPacket)
				{
					output = count + "\t" +
							"TCP\t" + 
							((TCPPacket)p).timestamp + "\t" + 
							((TCPPacket)p).data.length + "\n";
					bw.write(output);
				}
				else if (p instanceof UDPPacket)
				{
					output = count + "\t" +
							"UDP\t" + 
							((UDPPacket)p).timestamp + "\t" + 
							((UDPPacket)p).data.length + "\n";
					bw.write(output);				}
				else if (p instanceof IPPacket)
				{
					//Unknown if this part is necessary
					//bw.write(((IPPacket)p).timestamp + " ");
				}
				else
				{
					//Unknown if this is necessary
				}

			}
			bw.flush();
			bw.close();
			
			System.out.println("Done");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PcapParser pp = new PcapParser();
		pp.openFile("/Users/David/Desktop/ECE_537_Communication-Networks_Project_1/simpleparser/VoIP.pcap");
		parsePcapFile(pp, "VoIP");
		pp.closeFile();
		
		pp.openFile("/Users/David/Desktop/ECE_537_Communication-Networks_Project_1/simpleparser/BitTorrent.pcap");
		parsePcapFile(pp, "BitTorrent");
		pp.closeFile();
		
		pp.openFile("/Users/David/Desktop/ECE_537_Communication-Networks_Project_1/simpleparser/FTP.pcap");
		parsePcapFile(pp, "FTP");
		pp.closeFile();
		
		pp.openFile("/Users/David/Desktop/ECE_537_Communication-Networks_Project_1/simpleparser/HTTP_Facebook.pcap");
		parsePcapFile(pp, "HTTP_Facebook");
		pp.closeFile();
	}

}
