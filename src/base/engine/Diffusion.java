package base.engine;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;

/**
*
* @author Yoann CAPLAIN
*/
public class Diffusion {

	private HashMap<Integer, ClientServer> hash = new HashMap<Integer, ClientServer>();
    
	public Diffusion(){
		
	}
	
	synchronized public void diffTousSaufEmetteur(Message mes, int id){
        for(Entry<Integer, ClientServer> v : hash.entrySet()){
            if(v != null && v.getValue() != null)
                if(v.getKey() != id)
                    if(v.getValue().getOut() != null){
                    	v.getValue().getOut().receiveMessage(mes);
                    }
        }
        
    }
    
    synchronized public void addClientServer(ClientServer client){
    	hash.put(client.getId(), client);
    }
    synchronized public void removeClientServer(ClientServer client){
    	hash.remove(client.getId());
    }
    
    synchronized public int size(){
    	return hash.size();
    }

}
