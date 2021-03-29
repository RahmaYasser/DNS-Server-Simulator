/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns.server;

/**
 *
 * @author Home
 */
public class URL {
    private String IP;
    private String hostName;
    private String type;
    private String canonicalName;
    private String serverName;
    
     public void setIp(String IP){
        this.IP = IP;
    }
    public String getIp(){
        return IP;
    }
    
     public void setHostName(String hostName){
        this.hostName = hostName;
    }
    public String getHostName(){
        return hostName;
    }
    
     public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
    
     public void setCanonicalName(String canonicalName){
         this.canonicalName = canonicalName;
     }
    public String getCanonicalName(){
        return canonicalName;
    }

    void setServerName(String serverName) {
        this.serverName = serverName;
    }

    String getServerName() {
        return serverName;
    }
}
