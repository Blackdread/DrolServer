package base.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

import base.engine.EngineManager;
import base.engine.Game;
import base.utils.Configuration;


/**
 * This class represent advance game state like "in game" phases.
 * 
 * @author Yoann CAPLAIN
 * 
 */
public abstract class View extends BasicGameState {

	protected GameContainer container;
	protected static Game game;
	protected static int lastViewID = 0;
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		lastViewID = this.getID();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.container = container;
		this.game = (Game) game;
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if(Configuration.isDebug())
			g.drawString(""+Configuration.getScaleFenetre(), 5, 30);
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		switch (key) {
		case Input.KEY_F1:
			takeScreenShot();
			break;
		default:
			break;
		}
	}

	private void takeScreenShot() {
		try {
			Image image = new Image(container.getWidth(), container.getHeight());
			container.getGraphics().copyArea(image, 0, 0);
			ImageOut.write(image, "screenshot/screenshot_" + new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss").format(Calendar.getInstance().getTime()) + ".jpg");
		} catch (Exception e) {
			System.err.println("Could not save screenshot: " + e.getMessage());
		}
	}

	/**
	 * Developer must initialize the state resources here.
	 * 
	 * @param container
	 *            The game container associated to the game context.
	 * @param game
	 *            The Game context.
	 */
	public abstract void initResources();

	/**
	 * Retourne a la vue precedente
	 * Avec transition de fadeOut et fadeIn
	 */
	protected void gotoPreviousView(){
		container.setMouseGrabbed(false);
		game.enterState(lastViewID, new FadeOutTransition(), new FadeInTransition());
	}
	/**
	 * Retourne a la vue precedente
	 * @param out transition out
	 * @param in transition in
	 */
	protected void gotoPreviousView(Transition out, Transition in){
		container.setMouseGrabbed(false);
		game.enterState(lastViewID, out, in);
	}
}
