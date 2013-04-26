package base.views;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

import base.engine.Game;
import base.engine.Partie;
import base.engine.Server;
import base.engine.gui.ElementString;
import base.engine.gui.ListeDeroulante;
import base.utils.ResourceManager;
import base.utils.Timer;

public class AdministrationView extends View {

	
	private Server server;
	private MouseOverArea butRetour, butCreerServer;
	private ListeDeroulante listeServers;
	private Timer timerRefreshServers;
	private Rectangle shapeListeServers;
	
	private TextField text;
	
	@Override
	public void initResources() {
		
		final int MARGIN = 30;
		int x = container.getWidth();
		int y = container.getHeight();
		Image tmp = ResourceManager.getImage("butRetour");
		
		int larg = tmp.getWidth();
		int haut = tmp.getHeight();
		int yBut = y - haut - MARGIN;
		
		shapeListeServers = new Rectangle(MARGIN, 50, x/2 - MARGIN - 10, yBut - 50);
		
		listeServers = new ListeDeroulante(container, ResourceManager.getImage("transparent"), (int)shapeListeServers.getX()+5 , (int)shapeListeServers.getY()+5);
		listeServers.setScrolled(true);
		listeServers.setMaxElementsToDraw((int)shapeListeServers.getHeight()/20);
	
		butRetour = new MouseOverArea(container, ResourceManager.getImage("butRetour"), MARGIN, yBut, larg, haut);
		butRetour.setMouseOverImage(ResourceManager.getImage("butRetourOver"));
		
		butCreerServer = new MouseOverArea(container, tmp, tmp.getWidth() + MARGIN*2, yBut, larg, haut);
		butCreerServer.setMouseOverImage(ResourceManager.getImage("MenuJouerOver"));
		
		timerRefreshServers = new Timer(7000);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame sbGame, int delta) throws SlickException {
		super.update(container, sbGame, delta);
		
		if(timerRefreshServers.isTimeComplete()){
			if(server != null){
				//if(listeServers.size() != server.getPartie().size()) -> enlever car c 
				//vite fait et je veux aussi verifier si le nb de joueur est tjr bon sauf que je peux pas 
				//acceder facilement a ce nb contenu dans la liste deroulante vu que c dans un String
					mettreAJourListePartie();	// mode bourrin, on vide la liste et on refait
				
			}else
				listeServers.clearList();
			timerRefreshServers.resetTime();
		}else
			timerRefreshServers.update(delta);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		int nb = 0;
		if(server != null){
			nb = server.getPartie().size();
			g.drawString("Server : ON", 5, 5);
		}else
			g.drawString("Server : OFF", 5, 5);
		g.drawString("Listes des parties ouvertes : "+nb, shapeListeServers.getX()+shapeListeServers.getWidth()/2-container.getDefaultFont().getWidth("Listes des parties ouvertes :"+nb)/2, shapeListeServers.getY()-container.getDefaultFont().getHeight("Listes des parties ouvertes :"+nb) - 2);
		
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		g.setColor(Color.gray);
		g.fill(shapeListeServers);
		g.setDrawMode(Graphics.MODE_NORMAL);
		
		g.setColor(Color.white);
		g.draw(shapeListeServers);
		
		listeServers.renderString(container, g);
		
		butRetour.render(container, g);
		butCreerServer.render(container, g);
		
		super.render(container, game, g);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		switch(key){
		case Input.KEY_ESCAPE:
			gotoPreviousView();
			break;
		}
	}
	
	@Override
	public void mouseReleased(int but, int x, int y) {
		
		if(butRetour.isMouseOver())
			gotoPreviousView();
		else if(butCreerServer.isMouseOver()){
			if(server == null){
				// On le creer
				server = new Server("Serveur jeu Drol");
				Thread tmp = new Thread(server);
				tmp.start();
				System.out.println("Serveur demarrer");
			}else{
				// On le supprime
				server.stopServer();
				server = null;
				System.out.println("Serveur arreter");
			}
		}
	}
	
	private void mettreAJourListePartie(){
		listeServers.clearList();
		for(Partie v : server.getPartie())
			if(v != null){
				listeServers.addElement(new ElementString(container, ResourceManager.getImage("transparent").getScaledCopy(10, 
						container.getDefaultFont().getHeight("1")+2), 0,0,
						"Id: "+v.getId()+" Nb joueurs: "+v.getListeDesJoueursDansLaPartie().size()));
			}
	}

	@Override
	public int getID() {
		return Game.ADMINISTRATION_VIEW_ID;
	}

}
