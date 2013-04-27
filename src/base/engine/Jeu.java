package base.engine;

import java.io.File;
import java.util.ArrayList;

import base.engine.entities.HeroEntity;
import base.engine.entities.Zombi;
import base.engine.levels.LevelDrol;
import base.engine.logics.Deplacement;
import base.tile.TilePropriety;
import base.tile.TileSet;
import base.utils.ResourceManager;

/**
*
* @author Yoann CAPLAIN
*/
public class Jeu extends Partie {

	protected Barriere barriereAttenteJoueurs;
	
	/**
	 * True means players are playing (passed loading transition)
	 */
	protected boolean playingGame = false;
	
	
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
		
		for(ClientServer v : listeDesJoueursDansLaPartie.getClients())
			if(v != null)
				v.setPartie(this);
		
		ArrayList<TilePropriety> tp = new ArrayList<TilePropriety>();
		tp.add(new TilePropriety(0, false, "fodfnd"));
		tp.add(new TilePropriety(1, false, "fond"));
		tp.add(new TilePropriety(2, true, "fodnd"));
		tp.add(new TilePropriety(3, false, "fonssd"));
		
		engineManager.setCurrentLevelUsed(new LevelDrol(new File("levels/lvl_0.lvl"), new TileSet(ResourceManager.getSpriteSheet("sprite"), tp), engineManager));
		
		
		/*
		 * 
		 * A SUPPRIMER
		 * 
		 */
		/*
		HeroEntity hero = new HeroEntity("bla", engineManager, 500);
		hero.setLocation(70, 70);
		
		Zombi z = new Zombi("zombi", engineManager, 10);
		z.setLocation(140, 40);
		
		engineManager.getCurrentLevelUsed().addEntity(hero);
		engineManager.getCurrentLevelUsed().addEntity(z);
		Deplacement.deplacerEntity(engineManager,0, 0, hero.getId());
		Deplacement.deplacerEntity(engineManager,0, 0, z.getId());
		engineManager.getIA().addEntity(hero);
		engineManager.getIA().addEntity(z);
		//*/
	}

	@Override
	public void run(){
		/*
		 * Chargement du niveau et attente des joueurs
		 */
		if(engineManager.getCurrentLevelUsed() != null){
			engineManager.getCurrentLevelUsed().loadLevel();
		
			while(!barriereAttenteJoueurs.isMax()){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {e.printStackTrace();}
				System.out.println("Attente des joueurs "+barriereAttenteJoueurs.getCompteur());
			}
			
			while(!engineManager.getCurrentLevelUsed().isLoadOver()){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {e.printStackTrace();}
				System.out.println("Attente load level server");
			}
			
			/*
			HeroEntity hero = new HeroEntity("bla", engineManager, 500);
			hero.setLocation(70, 70);
			
			Zombi z = new Zombi("zombi", engineManager, 10);
			z.setLocation(140, 40);
			
			engineManager.getCurrentLevelUsed().addEntity(hero);
			engineManager.getCurrentLevelUsed().addEntity(z);
			Deplacement.deplacerEntity(engineManager,0, 0, hero.getId());
			Deplacement.deplacerEntity(engineManager,0, 0, z.getId());
			engineManager.getIA().addEntity(hero);
			engineManager.getIA().addEntity(z);
			//*/
			playingGame = true;
			
			engineManager.getNetworkEngine().sendToPlayersToChangeViewToGame();
			
			super.run();
		}else
			System.err.println("Level null dans Server lors du run du jeu");
		
		// TODO quand on sort du run principal, creer un nouveau salon, transferer les joueurs restants dedans et ils pourront continuer a jouer plus tard
		if(listeDesJoueursDansLaPartie.size() > 0){
			
			
		}
	}
	
	@Override
	synchronized public void playerJoinGame(ClientServer newPlayer) {
		super.playerJoinGame(newPlayer);
		
	}
	
	@Override
	synchronized public void playerLeftGame(ClientServer leavePlayer){
		super.playerLeftGame(leavePlayer);
		barriereAttenteJoueurs.decrementMaxValue();
		
	}
	
	synchronized public void playerEndedLoading(ClientServer player){
		barriereAttenteJoueurs.incrementCompteur();
		// TODO notifier les autres client que le player a fini le loading
	}

	public synchronized boolean isPlayingGame() {
		return playingGame;
	}

}
