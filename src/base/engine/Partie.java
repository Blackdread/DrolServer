package base.engine;

import java.util.ArrayList;

import org.lwjgl.Sys;

import base.engine.entities.BasicEntity;

/**
* A maintenir avec la classe InfoPartie qui est creer pour le cote client
* @author Yoann CAPLAIN
*/
public abstract class Partie implements Runnable{

	protected int id;
	
	/**
	 * Master of the game
	 * Gere les options etc
	 */
	protected ClientServer host;
	protected EngineManager engineManager = new EngineManager(true,true);
	
	protected Diffusion listeDesJoueursDansLaPartie = new Diffusion();
	
	protected boolean continuer = true;
	
	/**
	 * True means players are playing (passed loading transition)
	 */
	protected boolean playingGame = false;
	
	public Partie(ClientServer host, int id){
		this.host = host;
		playerJoinGame(host);
		engineManager.getNetworkEngine().setPartie(this);
	}
	
	@Override
	public void run(){
		long temp = getTime();
		long stockDelta;
		
		int test = 0;
		
		while(continuer){
			stockDelta = getTime() - temp;
			if((int)stockDelta >= 10){
				
				engineManager.update((int)stockDelta);
				
				temp = getTime();
				
				//System.out.println("Server updated "+ (int)stockDelta);
				test++;
			}
			/* 1er test
			// Pas sur de vouloir faire ca, le mieux serait d'envoyer seulement les entites qui ont changes 
			
			if(test >= 10 && playingGame){
				if(host != null){
					host.getOut().receiveMessage(engineManager.getCurrentLevelUsed());
				}else
					System.err.println("host null");
				test = 0;
			}
			//*/
			
			//* 2eme test
			if(test >= 20 && playingGame){
				for(BasicEntity v : engineManager.getCurrentLevelUsed().getArrayEntite().values()){
					if(v != null){
						listeDesJoueursDansLaPartie.diffTous(v);
						//System.out.println("server "+v.getTargetName()+" "+v.getId()+" "+v.getX()+" "+v.getY());
					}
				}
				
				test = 0;
				//System.out.println("listes entite envoye "+ (int)stockDelta);
			}
			//*/
			/* 3eme test
			if(test >= 10 && playingGame){
				ArrayList<BasicEntity> array = new ArrayList<BasicEntity>();
				for(BasicEntity v : engineManager.getCurrentLevelUsed().getArrayEntite().values())
					array.add(v);
				
				listeDesJoueursDansLaPartie.diffTous(array);
			
			}
			//*/
			
		}
	}
	
	/**
	 * 
	 * @param newPlayer
	 */
	synchronized public void playerJoinGame(ClientServer newPlayer){
		listeDesJoueursDansLaPartie.addClientServer(newPlayer);
		if(host == null)
			host = newPlayer;
		// TODO notifier les autres joueurs
	}
	
	synchronized public void playerLeftGame(ClientServer leavePlayer){
		listeDesJoueursDansLaPartie.removeClientServer(leavePlayer);
		// TODO si c le host qui part, il faut donner le host a un autre joueur, (si la partie est vide, elle est supprimer)
		// TODO notifier les autres joueurs
		if(host.equals(leavePlayer)){
			host = null;
			if(listeDesJoueursDansLaPartie.size() >= 1)
				for(ClientServer v : listeDesJoueursDansLaPartie.getClients())
					if(v != null){
						host = v;
						break;
					}
		}
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
