/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns.server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Home
 */
public class client {
    private static InetAddress host;
    private static final int PORT = 4000;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;

    public static void main(String[] args) throws SocketException, IOException {
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException uhEx) {
            System.out.println("Host ID not found!");
            System.exit(1);
        }
        accessServer();
    }

    private static void accessServer() throws SocketException, IOException {
        try {

            datagramSocket = new DatagramSocket();
            //Set up stream for keyboard entry...
            Scanner userEntry = new Scanner(System.in);
            String message = "", response = "";
            while (true) {
                System.out.println("Enter Name / Enter 'quit' to exit");
                message = userEntry.nextLine();
                if(message.equalsIgnoreCase("quit"))break;
                outPacket = new DatagramPacket(message.getBytes(), message.length(), host, PORT); //Step 2.
                //Step 3...
                datagramSocket.send(outPacket);
                buffer = new byte[256]; //Step 4.
                inPacket = new DatagramPacket(buffer, buffer.length);//Step 5.
                datagramSocket.receive(inPacket);
                response = new String(inPacket.getData(), 0, inPacket.getLength());
                System.out.println(response);
            }
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {
            System.out.println("\n* Closing connection... *");
            datagramSocket.close(); //Step 8.
        }
    }
}
