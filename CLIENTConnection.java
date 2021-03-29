/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Home
 */
public class CLIENTConnection implements Runnable{
    private String serverResponse;
    private DatagramPacket packet;
    private static DatagramPacket outPacket;
    private DatagramSocket clientSocket;

    public CLIENTConnection(DatagramSocket clientSocket, DatagramPacket packet,String serverResponse) throws IOException
    {
        this.serverResponse = serverResponse;
        this.clientSocket = clientSocket;
        this.packet = packet;
    }
    
    
    @Override
    public void run()
    {
        InetAddress clientAddress = packet.getAddress();
        int clientPort = packet.getPort();
        
        outPacket = new DatagramPacket(serverResponse.getBytes(),serverResponse.length(),clientAddress,clientPort);
         try { 
             clientSocket.send(outPacket);
         } catch (IOException ex) {
             Logger.getLogger(CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}
