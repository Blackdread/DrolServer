package base.views;

import java.io.IOException;

import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;

import base.utils.Resolution;
import base.engine.gui.ListeDeroulante;
import base.engine.Game;
import base.utils.Configuration;
import base.utils.ResourceManager;


/**
 * Menu associated to the options.
 * 
 * @author Yoann CAPLAIN
 * 
 */
public class OptionsView extends View {

	private Image background, listeElement, listeElementOver;;

	private MouseOverArea butQuitter, butFullscreen;
	private ListeDeroulante listeDerTailleScreen;
	private RoundedRectangle zone[] = new RoundedRectangle[3];
	
	@SuppressWarnings("deprecation")
	@Override
	public void initResources() {
		final int MARGIN = 30;
		int zoneX1 = container.getWidth()/3;
		int zoneX2 = zoneX1*2 + MARGIN;
		
		
		int zoneY1max = container.getHeight()*8/10;
		
		zone[0] = new RoundedRectangle(MARGIN, MARGIN, zoneX1 - MARGIN,zoneY1max, 5);
		zone[1] = new RoundedRectangle(zoneX1 + MARGIN, MARGIN, zoneX1 - MARGIN,zoneY1max, 5);
		zone[2] = new RoundedRectangle(zoneX2, MARGIN, zoneX1 - MARGIN * 2,zoneY1max, 5);
		
		// TODO PLACER les elements dans les differentes zones
		
		background = ResourceManager.getImage("background_options_view").getScaledCopy(container.getWidth(), container.getHeight());
		
		butQuitter = new MouseOverArea(container, ResourceManager.getImage("MenuQuitter"), container.getWidth()/10, container.getHeight()-container.getHeight()/10 - 50, ResourceManager.getImage("MenuQuitterOver").getWidth(), ResourceManager.getImage("MenuQuitterOver").getHeight());
		butQuitter.setMouseOverImage(ResourceManager.getImage("MenuQuitterOver"));
		
		listeElement = ResourceManager.getImage("listeElement");
		listeElementOver = ResourceManager.getImage("listeElementOver");
		
		butFullscreen = new MouseOverArea(container, ResourceManager.getImage("fullscreen"), 50, 50, 150, 50);
		butFullscreen.setMouseOverImage(ResourceManager.getImage("fullscreenOver"));
		
		listeDerTailleScreen = new ListeDeroulante((AppGameContainer)container, ResourceManager.getImage("listeDeroulante").getScaledCopy(150, 40), 250, 55);
		listeDerTailleScreen.setMouseOverImage(ResourceManager.getImage("listeDeroulanteOver").getScaledCopy(150, 40));
		
		DisplayMode modes[] = Resolution.getAvailableResolution();
		if(modes!=null)
		for (int i=0;i<modes.length;i++) {
            DisplayMode current = modes[i];
            if(current.getHeight() >= Game.MINIMUM_SCREEN_HAUTEUR)
            	if(!(new Resolution(current.getWidth(), current.getHeight()).equals(new Resolution(container.getWidth(), container.getHeight()))))
            		listeDerTailleScreen.addElementResolution(container, listeElement, current.getWidth(), current.getHeight(), 150, 25);
        }
		listeDerTailleScreen.chercherElementUsed();
		listeDerTailleScreen.applyImageOverAllElement(listeElementOver);
		
	}

	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		for(int iii=0;iii<3;iii++){
			g.setColor(Color.gray);
			g.fill(zone[iii]);
			g.setColor(Color.orange);
			g.draw(zone[iii]);
		}
		g.setDrawMode(Graphics.MODE_NORMAL);
		
		butFullscreen.render(container, g);
		g.setColor(Color.white);

		butQuitter.render(container, g);
		super.render(container, game, g);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		switch(key){
		case Input.KEY_ESCAPE:
			//goToMenu();
			gotoPreviousView();
			break;
		}
	}
	@Override
	public void mouseReleased(int but, int x, int y) {
		super.mouseReleased(but, x, y);
		
		if(butQuitter.isMouseOver())
			gotoPreviousView();
		
		if(butFullscreen.isMouseOver())
			inverseFullscreen();
		
		listeDerTailleScreen.isMouseOver();
	}

	@Override
	public int getID() {
		return Game.OPTIONS_VIEW_ID;
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy){
		super.mouseDragged(oldx, oldy, newx, newy);
	}
	
	private void inverseFullscreen(){
		if(!container.isFullscreen())
			try{
				container.setFullscreen(true);
				Configuration.setFullScreen(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		else
			try{
				container.setFullscreen(false);
				Configuration.setFullScreen(false);
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	@Override
	protected void gotoPreviousView(){
		try {
			Configuration.saveNewConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.gotoPreviousView();
	}

}
