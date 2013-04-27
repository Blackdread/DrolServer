package base.engine;

import org.lwjgl.Sys;

/**
*
* @author Yoann CAPLAIN
*/
public abstract class Partie implements Runnable{

	protected int id;
	
	/**
	 * Master of the game
	 * Gere les options etc
	 */
	protected ClientServer host;
	protected EngineManager engineManager = new EngineManager();
	
	protected Diffusion listeDesJoueursDansLaPartie = new Diffusion();
	
	protected boolean continuer = true;
	
	public Partie(ClientServer host, int id){
		this.host = host;
		playerJoinGame(host);
		engineManager.getNetworkEngine().setPartie(this);
	}
	
	@Override
	public void run(){
		long temp = getTime();
		long stockDelta;
		
		while(continuer){
			stockDelta = getTime() - temp;
			if((int)stockDelta >= 10){
				
				engineManager.update((int)stockDelta);
				
				temp = getTime();
			}
		}
	}
	
	/**
	 * 
	 * @param newPlayer
	 */
	synchronized public void playerJoinGame(ClientServer newPlayer){
		listeDesJoueursDansLaPartie.addClientServer(newPlayer);
		// TODO notifier les autres joueurs
	}
	
	synchronized public void playerLeftGame(ClientServer leavePlayer){
		listeDesJoueursDansLaPartie.removeClientServer(leavePlayer);
		// TODO si c le host qui part, il faut donner le host a un autre joueur, (si la partie est vide, elle est supprimer)
		// TODO notifier les autres joueurs
	}
	
	public void stopPartie(){
		continuer = false;
	}
	
    public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public ClientServer getHost() {
		return host;
	}

	public EngineManager getEngineManager() {
		return engineManager;
	}

	public Diffusion getListeDesJoueursDansLaPartie() {
		return listeDesJoueursDansLaPartie;
	}

	public boolean isContinuer() {
		return continuer;
	}

	public void setContinuer(boolean continuer) {
		this.continuer = continuer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
