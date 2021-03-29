/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Home
 */
public class local_server {
    private static DatagramSocket serverSocket;

    public static URL arr[] = new URL[10];
    public static int serverPorts[] = new int[3];

    private static int x=2;
    public static void main(String[] args) throws IOException {

        serverPorts[0] = 1234;
        serverPorts[1] = 1235;
        serverPorts[2] = 1236;
        /* loading file
        //
        //
         */

        try {
            File file = new File("local.txt");    //creates a new file instance  
            FileReader fr = new FileReader(file);   //reads the file  
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream  
            StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters  
            String line;
            ///////
            Scanner s = new Scanner(file);
            /////
            String Null = "null";
            for (int i = 0; i < arr.length; i++) {
                arr[i] = new URL();
                arr[i].setCanonicalName(Null);
                arr[i].setHostName(Null);
                arr[i].setIp(Null);
                arr[i].setType(Null);
            }
            int count = 0;
            while ((line = br.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line);
                arr[count].setHostName(s.next());
                arr[count].setIp(s.next());
                if (tokens.countTokens() == 2) {
                    arr[count].setType("A");
                    arr[count].setCanonicalName("null");
                } else {
                    arr[count].setType("CName");
                    arr[count].setCanonicalName(s.next());

                }
                count++;
            }

            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO code application logic here
        System.out.println("Server started.");
        byte[] buffer = new byte[512];
        serverSocket = new DatagramSocket(4000);
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);
                System.out.println("SERVER: Accepted connection.");
                
                String clientRequest = new String(packet.getData(), 0, packet.getLength());
                String serverResponse = "";
                boolean found = false;
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].getHostName().equalsIgnoreCase(clientRequest)) {
                        arr[i].setServerName("local_dns");
                        serverResponse = arr[i].getHostName() + " " + arr[i].getIp() + " " + arr[i].getType() + " " + arr[i].getServerName();
                        
                        if (!(arr[i].getCanonicalName().equalsIgnoreCase("null"))) {
                            String tmp = serverResponse + " " + arr[i].getCanonicalName();
                            serverResponse = tmp;
                        }
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    DatagramSocket datagramSocket;
                    datagramSocket = new DatagramSocket();
                    byte[] buffer2;
                    buffer2 = new byte[256];
                    DatagramPacket outPacket, inPacket;
                    for (int p = 0; p < 3; p++) {
                        InetAddress host;
                        host = InetAddress.getLocalHost();
                        outPacket = new DatagramPacket(clientRequest.getBytes(), clientRequest.length(), host, serverPorts[p]); //Step 2.
                        datagramSocket.send(outPacket);
                        buffer2 = new byte[256]; //Step 4.
                        inPacket = new DatagramPacket(buffer2, buffer2.length);//Step 5.
                        datagramSocket.receive(inPacket);
                        serverResponse = new String(inPacket.getData(), 0, inPacket.getLength());
                        System.out.println(serverResponse);
                        if (!serverResponse.equalsIgnoreCase("not found")) {
                            break;
                        }
                    }
                }
                
                String finlaResponse;
                if (!serverResponse.equalsIgnoreCase("not found")){
                    String[] words = serverResponse.split(" ");
                    
                    
                    finlaResponse = "Client Requested : " + words[0] + "\n" + "URL : " + words[0] + "\n" + "Query type = " + words[2] + "\n" + "IP Address : " + words[1] + "\n" + "server name : " + words[3] + "\n";
                    
                    if (words.length==5){
                        String tmp = finlaResponse + "Canonical name : " + words[4]+ "\n" + "Alians " + words[0] + "\n";
                        finlaResponse = tmp;
                        if (!found){
                            arr[x].setCanonicalName(words[4]);
                        }
                    }
                    if (!found){
                        arr[x].setHostName(words[0]);
                        arr[x].setIp(words[1]);
                        arr[x].setType(words[2]);
                        arr[x].setServerName("local_dns");
                        x++;
                    }
                }
                else{
                    finlaResponse = serverResponse;
                }
                DatagramSocket threadSocket = new DatagramSocket();
                Thread t = new Thread(new CLIENTConnection(threadSocket, packet, finlaResponse));
                t.start();
            } catch (Exception e) {
                System.out.println("Error in connection attempt.");
            }
        }

    }
}
