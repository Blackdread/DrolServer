package base.engine;

import java.net.Socket;

/**
*
* @author Yoann CAPLAIN
*/
public class ClientServer {
	
    private int id;
    private Socket s;
    private Server server;
    private String pseudo = "";
    
    private Player player;	// TODO sur ?
    
    /**
     * Peut valoir null s'il n'est pas sur une partie
     */
    private Partie partie;

	private ClientServerIn in;
    private ClientServerOut out;
    
    public ClientServer(Server ser, Socket s, int id){
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

	public void setPartie(Partie partie) {
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
    public Partie getPartie() {
		return partie;
	}
}
