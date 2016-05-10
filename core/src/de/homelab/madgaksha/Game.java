package de.homelab.madgaksha;

import java.util.Locale;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.audiosystem.AwesomeAudio;
import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcecache.Resources.Ebgm;
import entitysystem.DisposableEngine;

public class Game implements ApplicationListener {
	
	private final static Logger LOG = Logger.getLogger(Game.class);

    public final static int VIEWPORT_GAME_AR_NUM = 8;
    public final static int VIEWPORT_GAME_AR_DEN = 9;
    public final static float VIEWPORT_INFO_WIDTH_MIN_S = 0.3f;
    public final static float VIEWPORT_INFO_HEIGHT_MIN_S = 0.3f;
	public final static float VIEWPORT_GAME_AR = (float)VIEWPORT_GAME_AR_NUM/(float)VIEWPORT_GAME_AR_DEN;
	public final static float VIEWPORT_GAME_AR_INV = (float)VIEWPORT_GAME_AR_DEN/(float)VIEWPORT_GAME_AR_NUM;

	/**
	 * For playing music. No other instance should be created. 
	 */
	public static MusicPlayer musicPlayer = null;
	
	/** For drawing the main game. **/
	SpriteBatch batchGame;
	/** For drawing the info screen, score etc.*/
	SpriteBatch batchInfo;
	/** For drawing the background directly to the screen.*/
	SpriteBatch batchScreen;

	/** Entity engine ASHLEY */
	DisposableEngine entityEngine;
	
	Texture img;
	
	private int testx = 160;
	private int testy = 160;
	private float test1 = 0.0f;
	private float test2 = 0.0f;
	private float test3 = 0.0f;
	private float test4 = 0.0f;
	private float test5 = 0.0f;
	private float test6 = 0.0f;
	private float test7 = 100.0f;
	
	private PerspectiveCamera testc;
	
	private final GameParameters params;
	private final ALevel level;
	private Viewport viewportGame;
	private Viewport viewportInfo;
	private Viewport viewportScreen;
	
	/**
	 * @param params Screen size, fps etc. that were requested.
	 */
	public Game(GameParameters params) {
		this.params = params;
		this.level = params.requestedLevel;
		// Set locale if it has not been set yet.
		if (!i18n.isInitiated()) {
			if (params.requestedLocale != null)
				i18n.init(params.requestedLocale);
			else i18n.init(Locale.getDefault());
		}
	}
	
	@Override
	public void create () {
		LOG.debug("creating new game");

		// Get viewports for the game, info and screen components.
		viewportGame = level.getGameViewport(params.requestedWidth, params.requestedHeight);
		viewportInfo = level.getInfoViewport(params.requestedWidth, params.requestedHeight);
		viewportScreen = level.getScreenViewport(params.requestedWidth, params.requestedHeight);
		
		// Create a new entity engine.
		entityEngine = new DisposableEngine();
		
		// Setup audio system.
		AwesomeAudio.initialize();
		
		// Set logging level.
		Gdx.app.setLogLevel(params.requestedLogLevel);
		
		// Create music player.
		Game.musicPlayer = new MusicPlayer();
		Game.musicPlayer.loadNext(Ebgm.TEST_ADX_STEREO);
		Game.musicPlayer.playNext();
				
		// Create sprite batches.
		batchGame = new SpriteBatch();
		batchInfo = new SpriteBatch();
		batchScreen = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {		
		//TESTING
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) testx += 3;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) testx -= 3;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) testy -= 3;
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) testy += 3;
		
		test1 = Gdx.input.isKeyPressed(Input.Keys.Q) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.A) ? -0.5f : 0.0f;
		test2 = Gdx.input.isKeyPressed(Input.Keys.W) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.S) ? -0.5f : 0.0f;
		test3 = Gdx.input.isKeyPressed(Input.Keys.E) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.D) ? -0.5f : 0.0f;
		test4 += Gdx.input.isKeyPressed(Input.Keys.R) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.F) ? -0.5f : 0.0f;
		test5 += Gdx.input.isKeyPressed(Input.Keys.T) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.G) ? -0.5f : 0.0f;
		test6 += Gdx.input.isKeyPressed(Input.Keys.Z) ? 1.2f : Gdx.input.isKeyPressed(Input.Keys.H) ? -1.2f : 0.0f;
		test7 += Gdx.input.isKeyPressed(Input.Keys.O) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.L) ? -0.5f : 0.0f;
		
		testc.position.set(160,120,5);
		testc.far = test7 < 0.1f ? 0.1f : test7;
		
		testc.rotate(test1, 1, 0, 0);
		testc.rotate(test2, 0, 1, 0);
		testc.rotate(test3, 0, 0, 1);
		testc.translate(test4, test5, test6);
		
		testc.update();
		// TESTING END

		// Clear the screen before drawing.
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render background first.
		batchScreen.setProjectionMatrix(viewportScreen.getCamera().combined);
		batchScreen.begin();
		//TODO
		//ASHLEY: get all components with sprite drawing and draw them
		batchScreen.end();
		
		// Render game window next.
		batchGame.setProjectionMatrix(viewportGame.getCamera().combined);
		batchGame.begin();
		batchGame.draw(img, testx, testy);
		//TODO
		//ASHLEY: get all components with sprite drawing and draw them
		batchGame.end();
		
		// Render info window last.
		batchInfo.setProjectionMatrix(viewportInfo.getCamera().combined);
		batchInfo.begin();
		//TODO
		//ASHLEY: get all components with sprite drawing and draw them
		batchInfo.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// Dispose music player.
		try {
			Game.musicPlayer.dispose();
			ResourceCache.clearAll();
		}
		catch (Exception e) {
			LOG.error("error during cleanup",e);
		}
		
		// Dispose sprite batches.
		batchGame.dispose();
		batchInfo.dispose();
		batchScreen.dispose();

		//TODO
		// Remove all entities. Entities that need to do
		// clean-up must do so in their 
		entityEngine.dispose();
		entityEngine.removeAllEntities();
		
		img.dispose();
	}
}