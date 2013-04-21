package base.engine;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author Yoann CAPLAIN
 */
public class ClientServerIn implements Runnable{
    private Socket s;
    private ClientServer clientServer;
    private boolean continuer = true;
     
    public ClientServerIn(Socket soc, ClientServer clientServer){
    	s = soc;
    	this.clientServer = clientServer;
    }
      
    public ClientServerIn(String addr, int port){
        try{
        	s = new Socket(addr, port);
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    public void run(){
        System.out.println("clientIn demarrer");
        try{
        	ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        	
        	while(continuer){
	        	Object ob = in.readObject();
	        	if(ob instanceof Message){
	        		Message ob2 = (Message)ob;
	        		switch(ob2.instruction){
	        		case MessageKey.I_START_NEW_GAME:
	        			if(clientServer.getPartie() == null)
	        				clientServer.getServer().creePartie(clientServer);
	        			break;
	        		case MessageKey.I_JOIN_GAME:
	        			if(clientServer.getPartie() == null){
	        				if(ob2.i_data.containsKey(MessageKey.P_ID))
	    					{
	    						clientServer.getServer().rejoindrePartie(ob2.i_data.get(MessageKey.P_ID), clientServer);
	    					}
	        			}
	        			break;
	        		case MessageKey.I_LEAVE_GAME:
	        			// TODO
	        			break;
	        		default:
	        			if(clientServer.getPartie() != null)
	        				clientServer.getPartie().engineManager.receiveMessage(ob2);
	        			break;
	        		}
	        	}
        	}
        	
        }catch(Exception e){e.printStackTrace();}
        System.out.println("clienIn fin "+clientServer.getId());
    }
}
