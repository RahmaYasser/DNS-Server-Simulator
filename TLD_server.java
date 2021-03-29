/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns.server;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
import java.net.*;

/**
 *
 * @author Home
 */
public class TLD_server {
    private static final int PORT = 1235;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;
    public static URL[] TLDarr = new URL[4];

    public static void main(String[] args) {
        
        
        try{
        File file=new File("TLD.txt");    //creates a new file instance  
        FileReader fr=new FileReader(file);   //reads the file  
        BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
        StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
        String line;  
        ///////
        Scanner s = new Scanner(file);
        /////
        
        String x = "null";
            for (int i = 0; i < TLDarr.length; i++) {
                TLDarr[i] = new URL();
                TLDarr[i].setCanonicalName(x);
                TLDarr[i].setHostName(x);
                TLDarr[i].setIp(x);
                TLDarr[i].setType(x);
            }
        int count=0;
        while((line=br.readLine())!=null)  {
           StringTokenizer tokens = new StringTokenizer(line);
           TLDarr[count].setHostName(s.next());
           TLDarr[count].setIp(s.next());
           if(tokens.countTokens() == 2){
               TLDarr[count].setType("A");
               TLDarr[count].setCanonicalName("null");
           }
           else{            
               TLDarr[count].setType("CName");
               TLDarr[count].setCanonicalName(s.next());   
               
           }
           count++;
        }
        
        fr.close();
        }
     catch(IOException e)  {  
         e.printStackTrace(); 
     }
        
        
        System.out.println("Opening port...\n");
        try {
            datagramSocket
                    = new DatagramSocket(PORT);
        } catch (SocketException sockEx) {
            System.out.println(
                    "Unable to attach to port!");
            System.exit(1);
        }
        handleClient();
    }

    private static void handleClient() {
        try {
            String request, response= "not found";
            do {
                buffer = new byte[256]; //Step 2.
                inPacket = new DatagramPacket( buffer, buffer.length);
                datagramSocket.receive(inPacket);//Step 4.
                System.out.println("packet recieved");
                InetAddress clientAddress = inPacket.getAddress(); //Step 5.
                int clientPort = inPacket.getPort(); //Step 5.
                request = new String(inPacket.getData(), 0, inPacket.getLength()); //Step 6.
                System.out.println(request);
                for(int i=0;i<TLDarr.length;i++){
                    if(TLDarr[i].getHostName().equalsIgnoreCase(request)){
                         TLDarr[i].setServerName("tld_dns");
                        response = TLDarr[i].getHostName() + " " + TLDarr[i].getIp() + " " + TLDarr[i].getType() + " " + TLDarr[i].getServerName();
                        
                        if (!(TLDarr[i].getCanonicalName().equalsIgnoreCase("null"))) {
                            String tmp = response + " " + TLDarr[i].getCanonicalName();
                            response = tmp;
                        }
                        break;
                    }
                }
                outPacket = new DatagramPacket(response.getBytes(),  response.length(), clientAddress, clientPort); 
                
                datagramSocket.send(outPacket); //Step 8.
                System.out.println(response);
                response="not found";
            } while (true);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
