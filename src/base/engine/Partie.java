package base.engine;

public abstract class Partie {

	/**
	 * Master of the game
	 * Gere les options etc
	 */
	protected ClientServer host;
	protected EngineManager engineManager = new EngineManager();
	
	protected Diffusion listeDesJoueursDansLaPartie = new Diffusion();;
	
	public Partie(ClientServer host){
		this.host = host;
		playerJoinGame(host);
	}
	
	/**
	 * 
	 * @param newPlayer
	 */
	public void playerJoinGame(ClientServer newPlayer){
		listeDesJoueursDansLaPartie.addClientServer(newPlayer);
	}
}
