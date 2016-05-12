package de.homelab.madgaksha;

import java.util.Locale;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.audiosystem.AwesomeAudio;
import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.entitysystem.SpriteRenderSystem;
import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.level.GameViewport;
import de.homelab.madgaksha.level.InfoViewport;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcecache.Resources.EMusic;
import de.homelab.madgaksha.resourcecache.Resources.ETexture;

public class Game implements ApplicationListener {

	private final static Logger LOG = Logger.getLogger(Game.class);

	public final static int VIEWPORT_GAME_AR_NUM = 8;
	public final static int VIEWPORT_GAME_AR_DEN = 9;
	public final static float VIEWPORT_INFO_WIDTH_MIN_S = 0.3f;
	public final static float VIEWPORT_INFO_HEIGHT_MIN_S = 0.3f;
	public final static float VIEWPORT_GAME_AR = (float) VIEWPORT_GAME_AR_NUM / (float) VIEWPORT_GAME_AR_DEN;
	public final static float VIEWPORT_GAME_AR_INV = (float) VIEWPORT_GAME_AR_DEN / (float) VIEWPORT_GAME_AR_NUM;
	/**
	 * This will cause slowdown on slow devices, but game logic would get messed
	 * up for high dt.
	 */
	public final static float MAX_DELTA_TIME = 0.1f;

	/**
	 * For playing music. No other instance should be created.
	 */
	public static MusicPlayer musicPlayer = null;

	/** For drawing the info screen, score etc. */
	private static SpriteBatch batchInfo;
	/** For drawing the game window. */
	private static SpriteBatch batchGame;
	/** For drawing the background directly to the screen. */
	private static SpriteBatch batchScreen;

	/** Entity engine ASHLEY */
	private Engine entityEngine;

	/** Controls the speed of the game. */
	private float timeScalingFactor = 1.0f;

	/** Whether the game is active. */
	private boolean running = false;

	// TODO remove me
	// only for testing
	private float testx = 0.5f;
	private float testy = 0.5f;
	private float test1 = 0.0f;
	private float test2 = 0.0f;
	private float test3 = 0.0f;
	private float test4 = 0.0f;
	private float test5 = 0.0f;
	private float test6 = 0.0f;
	private float test7 = 100.0f;
	// testing end

	private final GameParameters params;
	private final ALevel level;

	private Texture backgroundImage;
	private static GameViewport viewportGame;
	private static InfoViewport viewportInfo;
	private static Viewport viewportScreen;

	/**
	 * @param params
	 *            Screen size, fps etc. that were requested.
	 */
	public Game(GameParameters params) {
		this.params = params;
		this.level = params.requestedLevel;
		// Set locale if it has not been set yet.
		if (!i18n.isInitiated()) {
			if (params.requestedLocale != null)
				i18n.init(params.requestedLocale);
			else
				i18n.init(Locale.getDefault());
		}
	}

	@Override
	public void create() {
		LOG.debug("creating new game");

		// Get viewports for the game, info and screen windows.
		viewportGame = level.getGameViewport(params.requestedWidth, params.requestedHeight);
		viewportInfo = level.getInfoViewport(params.requestedWidth, params.requestedHeight);
		viewportScreen = level.getScreenViewport(params.requestedWidth, params.requestedHeight);

		// Create a new entity engine and setup basic entity systems.
		createEntityEngine();

		// Setup audio system.
		AwesomeAudio.initialize();

		// Set logging level.
		Gdx.app.setLogLevel(params.requestedLogLevel);

		// Create music player.
		Game.musicPlayer = new MusicPlayer();
		Game.musicPlayer.loadNext(EMusic.TEST_ADX_STEREO);
		Game.musicPlayer.playNext();

		// Load background image.
		backgroundImage = level.getBackgroundImage();

		// Create sprite batches.
		batchScreen = new SpriteBatch();
		batchGame = new SpriteBatch();
		batchInfo = new SpriteBatch();

		batchScreen.disableBlending();
		batchGame.disableBlending();
		batchInfo.disableBlending();

		// Start the game.
		running = true;
	}

	@Override
	public void render() {
		// TODO
		// ============================================================
		// TESTING STARTS HERE
		// SHOULD BE REMOVED
		// ============================================================
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			testx += 3;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			testx -= 3;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			testy -= 3;
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			testy += 3;
		test1 = Gdx.input.isKeyPressed(Input.Keys.Q) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.A) ? -0.5f : 0.0f;
		test2 = Gdx.input.isKeyPressed(Input.Keys.W) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.S) ? -0.5f : 0.0f;
		test3 = Gdx.input.isKeyPressed(Input.Keys.E) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.D) ? -0.5f : 0.0f;
		test4 = Gdx.input.isKeyPressed(Input.Keys.R) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.F) ? -0.5f : 0.0f;
		test5 = Gdx.input.isKeyPressed(Input.Keys.T) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.G) ? -0.5f : 0.0f;
		test6 = Gdx.input.isKeyPressed(Input.Keys.Z) ? 1.2f : Gdx.input.isKeyPressed(Input.Keys.H) ? -1.2f : 0.0f;
		test7 += Gdx.input.isKeyPressed(Input.Keys.O) ? 0.5f : Gdx.input.isKeyPressed(Input.Keys.L) ? -0.5f : 0.0f;
		// ============================================================
		// TESTING ENDS HERE
		// ============================================================

		// Clear the screen before drawing.
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render background screen first.
		renderScreen();

		// Now render the game.
		renderGame();

		// Render info window last.
		renderInfo();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

		// Update our viewports.
		viewportGame.update(width, height);
		viewportInfo.update(width, height);
		viewportScreen.update(width, height);
	}

	@Override
	public void pause() {
		running = false;
	}

	@Override
	public void resume() {
		running = true;
	}

	@Override
	public void dispose() {
		// Dispose music player.
		try {
			Game.musicPlayer.dispose();
		} catch (Exception e) {
			LOG.error("could not clear music player", e);
		}

		// Dispose loaded resources.
		try {
			ResourceCache.clearAll();
		} catch (Exception e) {
			LOG.error("could not clear resource cache", e);
		}

		// Dispose sprite batches.
		batchInfo.dispose();
		batchGame.dispose();
		batchScreen.dispose();

		// Remove all entities and systems to trigger cleanup.
		entityEngine.removeAllEntities();
		for (EntitySystem es : entityEngine.getSystems()) {
			entityEngine.removeSystem(es);
		}

		// TODO
		// dispose sprites etc.
	}

	public void renderScreen() {
		viewportScreen.apply(false);

		batchScreen.setProjectionMatrix(viewportScreen.getCamera().combined);
		batchScreen.begin();

		if (backgroundImage != null)
			batchScreen.draw(backgroundImage, 0.0f, 0.0f);

		batchScreen.end();
	}

	public void renderGame() {
		viewportGame.apply(false);

		viewportGame.getCamera().translate(test1, test2, test3);
		viewportGame.getCamera().rotate(test4, 1, 0, 0);
		viewportGame.getCamera().rotate(test5, 0, 1, 0);
		viewportGame.getCamera().rotate(test6, 0, 0, 1);
		viewportGame.getCamera().far += test7;
		viewportGame.getCamera().update();

		// Clear game window so that the background won't show.
		Gdx.gl.glScissor(viewportGame.getScreenX(), viewportGame.getScreenY(), viewportGame.getScreenWidth(),
				viewportGame.getScreenHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);

		// Update the game.
		final float deltaTime = timeScalingFactor * Math.min(Gdx.graphics.getRawDeltaTime(), MAX_DELTA_TIME);
		if (running) {
			entityEngine.update(deltaTime);
		} else {
			final SpriteRenderSystem draw2dSystem = entityEngine.getSystem(SpriteRenderSystem.class);
			if (draw2dSystem != null)
				draw2dSystem.update(deltaTime);
			// entityEngine.getSystem(Draw3dSystem.class).update(deltaTime);
		}
	}

	public void renderInfo() {
		viewportInfo.apply(false);

		batchInfo.setProjectionMatrix(viewportInfo.getCamera().combined);
		batchInfo.begin();

		if (viewportInfo.isLandscapeMode()) {

		} else { // if (viewportInfo.isPortraitMode())
		}

		batchInfo.end();
	}

	public void createEntityEngine() {
		entityEngine = new Engine();

		entityEngine.addSystem(new SpriteRenderSystem(viewportGame, batchGame));

		Entity myEntity = new Entity();
		myEntity.add(new PositionComponent(0.5f, 0.5f));
		myEntity.add(new SpriteComponent(new Sprite(ResourceCache.getTexture(ETexture.BADLOGIC))));
		entityEngine.addEntity(myEntity);

	}
}