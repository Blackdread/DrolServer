package base.engine;

/**
*
* @author Yoann CAPLAIN
*/
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
		// TODO si la partie est vide ou pas d'host, le 1er a entre devient l'host
		// Donc finalement pas besoin d'envoyer dans l'instanciation le host
	}
	
	public void playerLeftGame(ClientServer leavePlayer){
		listeDesJoueursDansLaPartie.removeClientServer(leavePlayer);
		// TODO si c le host qui part, il faut donner le host a un autre joueur, (si la partie est vide, elle est supprimer ??)
	}
}
