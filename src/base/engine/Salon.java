package base.engine;

/**
*
* @author Yoann CAPLAIN
*/
public class Salon extends Partie {

	public Salon(ClientServer host) {
		super(host);
		
	}

	
	public void playerJoinGame(ClientServer newPlayer) {
		super.playerJoinGame(newPlayer);
		
	}
	
	@Override
	public void playerLeftGame(ClientServer leavePlayer){
		super.playerLeftGame(leavePlayer);
	}

}
