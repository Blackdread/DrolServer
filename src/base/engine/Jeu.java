package base.engine;

/**
*
* @author Yoann CAPLAIN
*/
public class Jeu extends Partie {

	public Jeu(ClientServer host) {
		super(host);
		
	}

	@Override
	public void playerJoinGame(ClientServer newPlayer) {
		super.playerJoinGame(newPlayer);
		
	}
	
	@Override
	public void playerLeftGame(ClientServer leavePlayer){
		super.playerLeftGame(leavePlayer);
	}

}
