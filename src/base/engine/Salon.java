package base.engine;

/**
*
* @author Yoann CAPLAIN
*/
public class Salon extends Partie {

	public Salon(ClientServer host, int id) {
		super(host, id);
		this.id = id;
	}

	public Salon(Jeu jeu){
		super(jeu.getHost(), jeu.getId());
		engineManager = jeu.getEngineManager();
		listeDesJoueursDansLaPartie = jeu.getListeDesJoueursDansLaPartie();
		continuer = true;
	}
	
	public void playerJoinGame(ClientServer newPlayer) {
		super.playerJoinGame(newPlayer);
		
	}
	
	@Override
	public void playerLeftGame(ClientServer leavePlayer){
		super.playerLeftGame(leavePlayer);
	}

}
