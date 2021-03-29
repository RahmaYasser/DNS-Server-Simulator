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
public class authoritative_server {
    private static final int PORT = 1236;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;
    public static URL arr[] = new URL[4];

    public static void main(String[] args) {
        try{
        File file=new File("authoritative.txt");    //creates a new file instance  
        FileReader fr=new FileReader(file);   //reads the file  
        BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
        StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
        String line;  
        ///////
        Scanner s = new Scanner(file);
        //////
        String x = "null";
            for (int i = 0; i < arr.length; i++) {
                arr[i] = new URL();
                arr[i].setCanonicalName(x);
                arr[i].setHostName(x);
                arr[i].setIp(x);
                arr[i].setType(x);
            }
        int count=0;
        while((line=br.readLine())!=null)  {
           StringTokenizer tokens = new StringTokenizer(line);
           arr[count].setHostName(s.next());
           arr[count].setIp(s.next());
           if(tokens.countTokens() == 2){
               arr[count].setType("A");
               arr[count].setCanonicalName("null");
           }
           else{            
               arr[count].setType("CName");
               arr[count].setCanonicalName(s.next());   
               
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
               
                InetAddress clientAddress = inPacket.getAddress(); //Step 5.
                int clientPort = inPacket.getPort(); //Step 5.
                request = new String(inPacket.getData(), 0, inPacket.getLength()); //Step 6.
                System.err.println(request);
                for(int i=0;i<arr.length;i++){
                    if(arr[i].getHostName().equalsIgnoreCase(request)){
                         arr[i].setServerName("authoritative_dns");
                        response = arr[i].getHostName() + " " + arr[i].getIp() + " " + arr[i].getType() + " " + arr[i].getServerName();
                        
                        if (!(arr[i].getCanonicalName().equalsIgnoreCase("null"))) {
                            String tmp = response + " " + arr[i].getCanonicalName();
                            response = tmp;
                        }
                        break;
                    }
                }
                outPacket = new DatagramPacket(response.getBytes(),  response.length(), clientAddress, clientPort); 
                datagramSocket.send(outPacket); //Step 8.
                System.err.println(response);
                response="not found";
            } while (true);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
