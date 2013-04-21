package base.engine;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import base.utils.ResourceManager;

import base.utils.Configuration;
import base.views.*;

/**
 * 
 * @author Yoann CAPLAIN
 * @since 27 10 2012
 */
public class Game extends StateBasedGame {
	private static AppGameContainer container;

	/**
	 * The current name of the project.
	 */
	public static final String NAME = "DrolServer By Yoann CAPLAIN and Nicolas DUPIN 20-04-2013";
	/**
	 * The current version of the project.
	 */
	public static final String VERSION = "Version 1.0 Beta";
	
	private static ArrayList<View> states;
	
	public static final int MINIMUM_SCREEN_HAUTEUR = 768;
	
	// State IDS
	public static final int RESOURCES_STATE_ID = 0;
	public static final int MAIN_MENU_VIEW_ID = 1;
	public static final int OPTIONS_VIEW_ID = 4;
	public static final int CREDITS_VIEW_ID = 8;
	public static final int ADMINISTRATION_VIEW_ID = 16;
	
	public static final int LAST_VIEW_ID = 1024;
	
	public Game(String configFileLocation, String resourceJarLocation) throws IOException, SlickException {
		super(NAME);
		// Initialize resources
		ResourceManager.init(resourceJarLocation);
		// Initialize configuration
		Configuration.init(configFileLocation);
		
		states = new ArrayList<View>();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new ResourcesView(container));
		addState(new MainMenuView());
		addState(new OptionsView());
		addState(new CreditsView());
		addState(new AdministrationView());
		
		addState(new LastView());
		
	}
	
	private void applyCurrentConfiguration(AppGameContainer container) throws IOException, SlickException {
		Configuration.updateConfigFile();
		container.setDisplayMode(Configuration.getWidth(), Configuration.getHeight(), Configuration.isFullScreen());  
		container.setTargetFrameRate(Configuration.getTargetFPS());
		container.setSmoothDeltas(Configuration.isSmoothDeltas());
		container.setVSync(Configuration.isVSynch());
		container.setMusicVolume(Configuration.getMusicVolume());
		container.setMusicOn(Configuration.isMusicOn());
		container.setSoundVolume(Configuration.getSoundVolume());
		container.setShowFPS((Configuration.isDebug()) ? true : false);
		container.setVerbose((Configuration.isDebug()) ? true : false);
	}
	
	/**
	 * Apply the current configuration to the game container of the game
	 * context.
	 * 
	 * @throws IOException
	 *             If the configuration loading failed.
	 * @throws SlickException
	 *             If the configuration loading failed.
	 */
	public void applyCurrentConfiguration() throws IOException, SlickException {
		applyCurrentConfiguration((AppGameContainer) this.getContainer());
	}

	/**
	 * Entry point to launch the game.
	 * 
	 * @throws SlickException
	 * @throws IOException
	 * @throws LWJGLException 
	 */
	public void launch() throws SlickException, IOException {
		container = new AppGameContainer(this);
		
		// Icon		ici car ca a besoin d'etre mis avant que le container ne commence
		/*
		String icon[] = {"resources/others/ico16.png", "resources/others/ico24.png","resources/others/ico32.png"};
		try {
			container.setIcons(icon);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		//*/
		
		container.setMinimumLogicUpdateInterval(10);
		container.setMaximumLogicUpdateInterval(10);
		container.setUpdateOnlyWhenVisible(false);
		container.setAlwaysRender(true);
		
		// Apply Configuration
		applyCurrentConfiguration(container);

		// Start the game
		container.start();
	}
	
	@Override
	public void addState(GameState state) {
		super.addState(state);
		states.add((View) state);
	}

	public static View getStateByIndex(int index) {
		return (View) states.get(index);
	}
	
	public static void changeResolution(int width, int height) throws SlickException{
		((AppGameContainer) Game.container).setDisplayMode(width, height, Configuration.isFullScreen());
	}

	/**
	 * Plus simple que de devoir recharger que celles dont on a besoin. C'est un petit jeu apres tout :)
	 * C'est Slick 2D qui a ete mal pense sur sa facon de fonctionner, le RessourceManager ne devrait pas tous charger mais le
	 * faire progressivement selon la vue ou on se trouve.
	 */
	@Deprecated
	public static void rechargerToutesLesRessources(){
		for(View v : states)
			if(v!=null){
				v.initResources();
			}
		
	}

}
