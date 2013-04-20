package base.engine;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;

public class Diffusion {

	private HashMap<Integer, ClientServer> hash = new HashMap<Integer, ClientServer>();
    
	public Diffusion(){
		
	}
	
    public void diffTousSaufEmetteur(String m, int id){
        PrintWriter tmp;
        for(Entry<Integer, ClientServer> v : hash.entrySet()){
            if(v != null && v.getValue() != null)
                if(v.getKey() != id)
                    if(v.getValue().getS() != null){
                        try {
                            tmp = new PrintWriter(new OutputStreamWriter(v.getValue().getS().getOutputStream()));
                            tmp.println(""+m);
                            tmp.flush();
                        } catch (IOException ex) {
                        ex.printStackTrace();
                        }
                    }
        }
        
    }
    
    synchronized public void addClientServer(ClientServer client){
    	hash.put(client.getId(), client);
    }

}
