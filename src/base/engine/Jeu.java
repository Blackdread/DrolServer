package base.engine;

/**
*
* @author Yoann CAPLAIN
*/
public class Jeu extends Partie {

	protected Barriere barriereAttenteJoueurs;
	
	@Deprecated
	public Jeu(ClientServer host, int id) {
		super(host, id);
		
	}
	
	public Jeu(Salon s)
	{
		super(s.getHost(), s.id);
		engineManager = s.getEngineManager();
		listeDesJoueursDansLaPartie = s.getListeDesJoueursDansLaPartie();
		continuer = true;
		barriereAttenteJoueurs = new Barriere(0, listeDesJoueursDansLaPartie.size());
	}

	@Override
	public void run(){
		/*
		 * Chargement du niveau et attente des joueurs
		 */
		if(engineManager.getCurrentLevelUsed() != null){
			engineManager.getCurrentLevelUsed().loadLevel();
		
			while(!barriereAttenteJoueurs.isMax())
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {e.printStackTrace();}
			
			while(!engineManager.getCurrentLevelUsed().isLoadOver())
			{
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			super.run();
		}else
			System.err.println("Level null dans Server lors du run du jeu");
		
		// TODO quand on sort du run principal, creer un nouveau salon, transferer les joueurs restants dedans et ils pourront continuer a jouer plus tard
		if(listeDesJoueursDansLaPartie.size() > 0){
			
			
		}
	}
	
	@Override
	public void playerJoinGame(ClientServer newPlayer) {
		super.playerJoinGame(newPlayer);
		
	}
	
	@Override
	public void playerLeftGame(ClientServer leavePlayer){
		super.playerLeftGame(leavePlayer);
		barriereAttenteJoueurs.decrementMaxValue();
		
	}

}
