package base.views;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

import base.engine.Game;
import base.engine.Server;
import base.engine.gui.ListeDeroulante;

public class AdministrationView extends View {

	
	private Server server;
	private MouseOverArea butRetour, butCreerServer;
	private ListeDeroulante listeServers;
	
	private TextField text;
	
	@Override
	public void initResources() {
		

	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		
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
		
		
	}

	@Override
	public int getID() {
		return Game.ADMINISTRATION_VIEW_ID;
	}

}
