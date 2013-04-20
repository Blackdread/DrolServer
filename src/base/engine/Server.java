package base.engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

import base.utils.Configuration;

public class Server implements Runnable{

	private String nameServer;
	private int port = Configuration.getPort();
    private ServerSocket s_server;
    
    private HashMap<Integer, ClientServer> hashClients = new HashMap<Integer, ClientServer>();
    
    private int ID_partie = 0;
    private HashMap<Integer, Partie> hashPartie = new HashMap<Integer, Partie>();
    
    private boolean continuer = true;
	
	public Server(String nameServer){
		this.nameServer = nameServer;
		try{
			s_server = new ServerSocket(port);
        }catch(Exception e){e.printStackTrace();}
	}
	
	public void run(){
		 System.out.println("Debut du serveur: "+nameServer);
	        int i = 0;
	        while(continuer){
	            try{
		            ClientServer client = new ClientServer(this, s_server.accept(), i);
		            System.out.println("Nouveau client: "+nameServer+" et id "+i);
		            hashClients.put(i, client);
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
		try {
			s_server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void creePartie(ClientServer host){
		Partie tmp = new Salon(host);
		hashPartie.put(ID_partie, tmp);
		ID_partie++;
		host.setPartie(tmp);
	}
	public void rejoindrePartie(final int idPartie, ClientServer host){
		Partie tmp = hashPartie.get(idPartie);
		if(tmp != null){
			tmp.playerJoinGame(host);
			host.setPartie(tmp);
		}
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
    
    synchronized public void removeClient(int id){
    	hashClients.remove(id);
    }
}
