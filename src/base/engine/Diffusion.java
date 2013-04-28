package base.engine;

import java.util.ArrayList;
import java.util.Collection;
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
	
	synchronized public void diffTous(Object mes){
        for(Entry<Integer, ClientServer> v : hash.entrySet()){
            if(v != null && v.getValue() != null)
                if(v.getValue().getOut() != null)
                	v.getValue().getOut().receiveMessage(mes);
        }
        
    }
	
	synchronized public void diffTousSaufEmetteur(Object mes, int id){
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
    
    synchronized public Collection<ClientServer> getClients(){
    	return hash.values();
    }
    
    synchronized public int size(){
    	return hash.size();
    }
    
    synchronized public ArrayList<Player> getArrayPlayer(){
    	ArrayList<Player> retour = new ArrayList<Player>();
    	for(ClientServer v : hash.values()){
    		if(v != null)
    			retour.add(v.getPlayer());
    	}
    	retour.trimToSize();
    	return retour;
    }

}
