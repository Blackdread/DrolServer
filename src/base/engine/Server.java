package base.engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.HashMap;

import base.utils.Configuration;

/**
*
* @author Yoann CAPLAIN
*/
public class Server implements Runnable{

	private String nameServer;
	private int port = Configuration.getPort();
    private ServerSocket s_server;
    private boolean isServerOn = false;
    
    private HashMap<Integer, ClientServer> hashClients = new HashMap<Integer, ClientServer>();
    
    private int ID_partie = 0;
    private HashMap<Integer, Partie> hashPartie = new HashMap<Integer, Partie>();
    
    private boolean continuer = true;
    
    /**
     * -1 = no limits
     */
    private int maxPartieAtTheSameTime = -1;
	
	public Server(String nameServer){
		this.nameServer = nameServer;
		try{
			s_server = new ServerSocket(port);
        }catch(Exception e){e.printStackTrace();}
	}
	
	public void run(){
		 System.out.println("Debut du serveur: "+nameServer);
	     int i = 0;
	     isServerOn = true;
	     while(continuer){
            try{
	            ClientServer client = new ClientServer(this, s_server.accept(), i);
	            System.out.println("Nouveau client: "+nameServer+" et id "+i);
	            addClient(client);
	            i++;
            
            }catch(Exception e){
            	e.printStackTrace();
            }
	     }
	        
        try{
        	s_server.close();
        }catch(Exception e){e.printStackTrace();}
        System.out.println("Fin du serveur: "+nameServer);
	}
	
	// TODO supprimer tous les thread qui ont ete instancie
	public void stopServer(){
		continuer = false;
		isServerOn = false;
		try {
			s_server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	synchronized public void creePartie(ClientServer host){
		// TODO envoyer un message a celui qui a voulu si ca c fait ou pas
		if(maxPartieAtTheSameTime == -1 || hashPartie.size() <= maxPartieAtTheSameTime){
			Runnable tmp = new Salon(host, ID_partie);
			hashPartie.put(ID_partie, (Partie) tmp);
			ID_partie++;
			host.setPartie((Partie)tmp);
			Thread tmp2 = new Thread(tmp);
			tmp2.start();
		}
	}
	synchronized public void rejoindrePartie(final int idPartie, ClientServer host){
		Partie tmp = hashPartie.get(idPartie);
		if(tmp != null){
			tmp.playerJoinGame(host);
			host.setPartie(tmp);
		}
	}
	
	synchronized public void lancerPartie(Salon s){
		Runnable tmp = new Jeu(s);
		hashPartie.remove(((Partie)tmp).getId());
		hashPartie.put(((Partie)tmp).getId(), (Partie) tmp);
		Thread t = new Thread(tmp);
		t.start();
		
		
		s.stopPartie();
	}
	
	synchronized public void addClient(ClientServer client){
    	hashClients.put(client.getId(), client);
    }
	
    synchronized public void removeClient(int id){
    	hashClients.remove(id);
    }
	
    @Deprecated
    synchronized public Collection<Partie> getPartie(){
    	return hashPartie.values();
    }
    
    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

	public int getMaxPartieAtTheSameTime() {
		return maxPartieAtTheSameTime;
	}

	public void setMaxPartieAtTheSameTime(int maxPartieAtTheSameTime) {
		this.maxPartieAtTheSameTime = maxPartieAtTheSameTime;
	}

	public synchronized boolean isServerOn() {
		return isServerOn;
	}
}
