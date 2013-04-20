package base.engine;

import java.net.Socket;

/**
 *
 * @author 
 */
public class ClientServerOut implements Runnable{
    
    private Socket s;
    private ClientServer clientServer;
    
    public ClientServerOut(Socket soc, ClientServer clientServer){
      s = soc;
      this.clientServer = clientServer;
    }
    
    public ClientServerOut(String addr, int port){
        try{
        	s = new Socket(addr, port);
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void run(){
        System.out.println("clientOut demarrer");
        try{
           
            
        }catch(Exception e){e.printStackTrace();}
         System.out.println("fin clientOut");   
    }
    
}
