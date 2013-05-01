package base.engine;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
*
* @author Yoann CAPLAIN
*/
public class ClientServer {
	
    private int id;
    private Socket s;
    private Server server;
    private String pseudo = "";
    
    private Player player;
    
    /**
     * Peut valoir null s'il n'est pas dans une partie
     */
    private Partie partie;

	private ClientServerIn in;
    private ClientServerOut out;
    
    public ClientServer(Server ser, Socket s, int id){
    	try {
			s.setTcpNoDelay(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.s = s;
        server = ser;
        this.id = id;
        in = new ClientServerIn(s,this);
        out = new ClientServerOut(s,this);
        
        Thread tIn = new Thread(in);
        tIn.start();
        Thread tOut = new Thread(out);
        tOut.start();
    }
    
    /**
     * Fonction appeler lorsque la connexion avec le joueur est perdu ou qu'il a quitter tout simplement
     * on notifie la partie, on supprime le client du server et on arrete les Threads (in et out)
     */
    public void clientDisconnected(){
    	if(partie != null){
    		partie.playerLeftGame(this);
    		
    		partie = null;
    	}
    	server.clientDisconnected(this);
    	
    	stopThreads();
    	
    	try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    /**
     * Peut etre utile si on a tue son unite ou s'il a change d'id, etc
     * @throws IOException 
     */
    public void envoyerAuJoueurSonObjectPlayer() throws IOException{
    	//out.receiveMessage(player);
    	out.envoyer(player);
    }
    
    public void stopThreads(){
    	in.setContinuer(false);
    	out.setContinuer(false);
    }

	synchronized public void setPartie(Partie partie) {
		this.partie = partie;
	}

	public Server getServer() {
		return server;
	}

	public int getId() {
		return id;
	}

	public String getPseudo() {
		return pseudo;
	}

	public ClientServerIn getIn() {
		return in;
	}

	public ClientServerOut getOut() {
		return out;
	}
	 /**
     * Peut valoir null s'il n'est pas sur une partie
     */
	synchronized public Partie getPartie() {
		return partie;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
