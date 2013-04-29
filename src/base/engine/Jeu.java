package base.engine;

import java.io.File;
import java.util.ArrayList;

import base.engine.entities.HeroEntity;
import base.engine.entities.others.filters.FilterActivatorName;
import base.engine.entities.others.info.InfoTarget;
import base.engine.entities.others.triggers.TriggerTeleport;
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
		
		// On a qu'un level pour le moment
		engineManager.setCurrentLevelUsed(new LevelDrol(new File("levels/lvl_0.lvl"), new TileSet(ResourceManager.getSpriteSheet("sprite"), tp), engineManager));
		
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
			 * 
			 * A SUPPRIMER mais on garde car on a un seul niveau pour le moment.
			 * Plus tard il faudrait avoir un editeur et serializer le level pour ne plus avoir a faire ca
			 * 
			 */
			LevelDrol lvl = engineManager.getCurrentLevelUsed();
			
			TriggerTeleport trGauche = new TriggerTeleport(engineManager,"teleport", lvl.getLargeurTile()+2,lvl.getHauteurTile()+2,5,lvl.getHauteurTile()*(lvl.getHauteurNiveau()-2));
			TriggerTeleport trTeleportDroite = new TriggerTeleport(engineManager,"teleport2", lvl.getLargeurTile()*(lvl.getLargeurNiveau()-1)-5,lvl.getHauteurTile()+2,5,lvl.getHauteurTile()*(lvl.getHauteurNiveau()-2));
			trGauche.setRemoteDestination("infotargetDroite");	
			trTeleportDroite.setRemoteDestination("infotargetGauche");
			
			InfoTarget infGauche[] = new InfoTarget[6];
			for(int i=0;i<6;i++){
				infGauche[i] = new InfoTarget(engineManager,"infotargetGauche", lvl.getLargeurTile()*2, (lvl.getHauteurTile()+5)+(lvl.getHauteurTile()*4*i));
				engineManager.getInfoManager().addEntity(infGauche[i]);
				Deplacement.ajouterEntiteDansTiles(infGauche[i]);
			}
			
			InfoTarget infDroite[] = new InfoTarget[6];
			for(int i=0;i<6;i++){
				infDroite[i] = new InfoTarget(engineManager,"infotargetDroite", lvl.getLargeurTile()*lvl.getLargeurNiveau()-lvl.getLargeurTile()*3, (lvl.getHauteurTile()+5)+(lvl.getHauteurTile()*4*i));
				engineManager.getInfoManager().addEntity(infDroite[i]);
				Deplacement.ajouterEntiteDansTiles(infDroite[i]);
			}
			
			FilterActivatorName fil = new FilterActivatorName(engineManager,"filtername",true,"tirlinear");
			
			trGauche.setFilterEntityThatActivate(fil);	
			trTeleportDroite.setFilterEntityThatActivate(fil);
			engineManager.getFilterManager().addEntity(fil);//
			engineManager.addEntity(trGauche);
			engineManager.addEntity(trTeleportDroite);
			Deplacement.ajouterEntiteDansTiles(trGauche);	
			Deplacement.ajouterEntiteDansTiles(trTeleportDroite);
			
			
			HeroEntity hero = new HeroEntity("bla", engineManager, 500);
			hero.setLocation(70, 70);
			engineManager.addEntity(hero);
			Deplacement.deplacerEntity(engineManager,0, 0, hero.getId());
			
			listeDesJoueursDansLaPartie.getArrayPlayer().get(0).setIdEntityHePlays(hero.getId());
			
			listeDesJoueursDansLaPartie.diffTous(listeDesJoueursDansLaPartie.getArrayPlayer().get(0));
			/*
			 * Fin
			 * 
			 */
			
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
