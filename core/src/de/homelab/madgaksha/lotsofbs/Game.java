package de.homelab.madgaksha.lotsofbs;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchGame;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchModel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.bitmapFontRasterSize;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.currentMonitorHeight;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.currentMonitorWidth;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.game;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameClock;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameScore;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.maxMonitorHeight;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.maxMonitorWidth;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.shapeRenderer;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.statusScreen;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGameFixed;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportPixel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.homelab.madgaksha.lotsofbs.audiosystem.AwesomeAudio;
import de.homelab.madgaksha.lotsofbs.audiosystem.MusicPlayer;
import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.PlayerMaker;
import de.homelab.madgaksha.lotsofbs.i18n.I18n;
import de.homelab.madgaksha.lotsofbs.layer.ALayer;
import de.homelab.madgaksha.lotsofbs.layer.EntityLayer;
import de.homelab.madgaksha.lotsofbs.layer.PauseLayer;
import de.homelab.madgaksha.lotsofbs.level.FixedGameViewport;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;
import de.homelab.madgaksha.lotsofbs.shader.CustomShaderProgram;
import de.homelab.madgaksha.lotsofbs.shader.FragmentShader;
import de.homelab.madgaksha.lotsofbs.util.Clock;

public class Game implements ApplicationListener {

	private final static Logger LOG = Logger.getLogger(Game.class);

	public final static int VIEWPORT_GAME_AR_NUM = 8;
	public final static int VIEWPORT_GAME_AR_DEN = 9;
	public final static float VIEWPORT_INFO_WIDTH_MIN_S = 0.3f;
	public final static float VIEWPORT_INFO_HEIGHT_MIN_S = 0.3f;
	/** 8/9 */
	public final static float VIEWPORT_GAME_AR = (float) VIEWPORT_GAME_AR_NUM / (float) VIEWPORT_GAME_AR_DEN;
	/** 9/8 */
	public final static float VIEWPORT_GAME_AR_INV = (float) VIEWPORT_GAME_AR_DEN / (float) VIEWPORT_GAME_AR_NUM;

	private final static String highScoreFile = "./toku.ten";

	/**
	 * This will cause slowdown on slow devices, but game logic would get messed
	 * up for high dt.
	 */
	public final static float MAX_DELTA_TIME = 0.2f;
	public final static float MIN_DELTA_TIME = 0.005f;

	private final List<ALayer> layerStack = new ArrayList<ALayer>(10);
	private final List<ALayer> layerStackPopQueue = new ArrayList<ALayer>(10);
	private final List<ALayer> layerStackPushQueue = new ArrayList<ALayer>(10);
	private ALayer entityLayer;

	/** Controls the speed of the game. */
	private float timeScalingFactor = 1.0f;

	/** Whether the game is active. */
	private boolean running = false;
	/** Whether the application was requested to exit. */
	private boolean exitRequested = false;

	/** Screen resolution before the latest resize. */
	private int lastWidth;
	private int lastHeight;

	private PauseLayer pauseLayer;

	// TODO
	// remove me, for testing only
	private BitmapFont debugFont;
	// testing end

	public final GameParameters params;
	private TextureRegion backgroundImage = null;
	private Rectangle backgroundImageRectangle = new Rectangle();

	/** Custom shaders for the game. */
	private CustomShaderProgram customShaderProgramBatchGame = null;

	private boolean gameLost = false;

	/**
	 * @param params
	 *            Screen size, fps etc. that were requested.
	 */
	public Game(GameParameters params) {
		// Read requested level and player.
		this.params = params;
		player = params.requestedPlayer;
		level = params.requestedLevel;

		// Set locale if it has not been set yet.
		if (!I18n.isInitiated()) {
			if (params.requestedLocale != null)
				I18n.init(params.requestedLocale);
			else
				I18n.init(Locale.getDefault());
		}

		game = this;
	}

	@Override
	public void create() {
		// Set logging level.
		Gdx.app.setLogLevel(params.requestedLogLevel);

		// Check monitor resolution and get highest possible resolution.
		readMonitorInfo();
		lastWidth = Gdx.graphics.getWidth();
		lastHeight = Gdx.graphics.getHeight();

		// Set size at which freetype fonts will be rasterized.
		bitmapFontRasterSize = Gdx.graphics.getDisplayMode().width / 80;
		if (bitmapFontRasterSize < 5)
			bitmapFontRasterSize = 5;

		// Simple pause menu screen.
		try {
			pauseLayer = new PauseLayer(true);
		} catch (IOException e) {
			LOG.debug("failed to initialize pause layer", e);
			exitRequested = true;
			Gdx.app.exit();
			return;
		}

		// Setup audio system.
		AwesomeAudio.initialize();

		// Load player data.
		if (!player.loadToRam()) {
			LOG.error("failed to load player resources");
			exitRequested = true;
			Gdx.app.exit();
			return;
		}

		// Load background image for weird aspect ratios.
		if ((backgroundImage = level.getBackgroundImage()) == null) {
			LOG.error("failed to load background image");
			exitRequested = true;
			Gdx.app.exit();
			return;
		}

		// Initialize resource pool.
		ResourcePool.init();

		// Create music player.
		MusicPlayer.getInstance().loadNext(level.getBgm());
		MusicPlayer.getInstance().transition(2.0f);

		// Create batches.
		batchGame = new SpriteBatch();
		batchPixel = new SpriteBatch();
		batchModel = new ModelBatch();
		shapeRenderer = new ShapeRenderer();

		// Initialize the entity engine.
		gameEntityEngine = new PooledEngine(level.getEntityPoolInitialSize(), level.getEntityPoolPoolMaxSize(),
				level.getComponentPoolInitialSize(), level.getComponentPoolMaxSize());
		EntityLayer.addEntityListeners();

		// Create the player entity.
		playerEntity = PlayerMaker.getInstance().makePlayer(player);
		playerHitCircleEntity = PlayerMaker.getInstance().makePlayerHitCircle(player);
		if (playerEntity == null || playerHitCircleEntity == null) {
			exitRequested = true;
			Gdx.app.exit();
			return;
		}

		// Initialize map and load level.
		if (!level.initialize(batchGame)) {
			exitRequested = true;
			Gdx.app.exit();
			return;
		}

		// Get viewports and set them up.
		viewportGame = level.getGameViewport(currentMonitorWidth, currentMonitorHeight);
		try {
			statusScreen = level.getStatusScreen(currentMonitorWidth, currentMonitorHeight);
		} catch (IOException e) {
			LOG.error("failed to initialize status screen", e);
			exitRequested = true;
			Gdx.app.exit();
			return;
		}

		viewportPixel = new ScreenViewport();
		viewportPixel.update(currentMonitorWidth, currentMonitorHeight, true);
		viewportGameFixed = new FixedGameViewport(currentMonitorWidth, currentMonitorHeight, true);
		computeBackgroundImageRectangle(currentMonitorWidth, currentMonitorHeight);
		
		

		// Initialize the layer stack.
		// Start off with a layer stack containing only the entity engine.
		try {
			entityLayer = new EntityLayer();
		} catch (IOException e) {
			LOG.debug("could not initialize entity layer", e);
			exitRequested = true;
			Gdx.app.exit();
			return;
		}
		layerStack.add(entityLayer);
		entityLayer.addedToStack();

		// Setup the player entity.
		PlayerMaker.getInstance().setupPlayer(player);
		PlayerMaker.getInstance().setupPlayerHitCircle(player);

		// Keep track of the time.
		gameClock = new Clock();

		// TODO remove me for release
		if (DebugMode.activated)
			createDebugStuff();

		statusScreen.forPlayer(playerHitCircleEntity);

		EntityLayer.addMainEntitiesToMap();

		// Start the game.
		running = true;
	}

	@Override
	public void render() {
		// Clear the screen before drawing.
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (customShaderProgramBatchGame != null && running)
			customShaderProgramBatchGame.update(Gdx.graphics.getRawDeltaTime() * timeScalingFactor);

		// Render background first.
		renderBackground();

		// Now render the game.
		renderGame();

		// Render info window last.
		renderStatusScreen();

		// TODO remove me for release
		// Render debug.
		if (DebugMode.activated)
			renderDebug();

		// Process layer stack queue.
		// Must be done at once after the update and
		// render methods, or sync becomes an issue.
		// Adding / removing layers does not happen
		// frequently, so we check if there is any
		// work to be done first.
		if (layerStackPopQueue.size() > 0) {
			layerStack.removeAll(layerStackPopQueue);
			for (ALayer layer : layerStackPopQueue)
				layer.removedFromStack();
			layerStackPopQueue.clear();
		}
		if (layerStackPushQueue.size() > 0) {
			layerStack.addAll(layerStackPushQueue);
			for (ALayer layer : layerStackPushQueue) {
				layer.addedToStack();
				layer.resize(currentMonitorWidth, currentMonitorHeight);
			}
			layerStackPushQueue.clear();
		}

		if (KeyMapDesktop.isPauseButtonJustPressed() && running)
			pause();
	}

	@Override
	public void resize(int width, int height) {
		if (!exitRequested) {
			computeBackgroundImageRectangle(width, height);

			// Update our viewports.
			viewportGame.update(width, height, false);
			viewportPixel.update(width, height, true);
			viewportGameFixed.update(width, height, true);
			statusScreen.resize(width, height);

			// Save current resolution.
			currentMonitorWidth = width;
			currentMonitorHeight = height;

			// Reload and raster bitmap fonts.
			bitmapFontRasterSize = viewportGame.getScreenWidth() / 40 - 1;
			if (bitmapFontRasterSize < 5)
				bitmapFontRasterSize = 5;
			if (width != lastWidth || height != lastHeight)
				ResourceCache.reloadAllBitmapFont();

			// Store old monitor resolution.
			lastWidth = Gdx.graphics.getWidth();
			lastHeight = Gdx.graphics.getHeight();

			// Resize all layers.
			for (ALayer layer : layerStack)
				layer.resize(width, height);
		}
	}

	@Override
	public void pause() {
		LOG.debug("pausing game");
		if (running) {
			pushLayer(pauseLayer);
			pauseLayer.setBlockUpdate(true);
		}
		running = false;
		MusicPlayer.getInstance().pause();
	}

	@Override
	public void resume() {
		// game should stay paused and resume only when pressing a button
		LOG.debug("resuming game");
		pauseLayer.disableInputThisFrame();
	}

	public void unpause() {
		LOG.debug("unpausing game");
		running = true;
		MusicPlayer.getInstance().play();
	}

	@Override
	public void dispose() {
		exitRequested = true;

		// Dispose music player.
		try {
			MusicPlayer.getInstance().dispose();
		} catch (Exception e) {
			LOG.error("could not dispose music player", e);
		}

		// Dispose sound player.
		try {
			SoundPlayer.getInstance().dispose();
		} catch (Exception e) {
			LOG.error("could not dispose sound player", e);
		}

		// Dispose loaded resources.
		try {
			ResourceCache.clearAll();
		} catch (Exception e) {
			LOG.error("could not clear resource cache", e);
		}

		// Dispose sprite batches.
		if (batchGame != null)
			batchGame.dispose();
		if (batchPixel != null)
			batchPixel.dispose();

		// Remove all entities and systems to trigger cleanup.
		// Observe the proper order, begin with topmost layer.
		for (int i = layerStack.size(); i-- > 0;) {
			layerStack.get(i).removedFromStack();
		}
		layerStack.clear();
		layerStackPopQueue.clear();
		layerStackPushQueue.clear();

		// Dispose background image.
		if (backgroundImage != null)
			backgroundImage.getTexture().dispose();

		// TODO remove me
		if (debugFont != null)
			debugFont.dispose();

		// Dispose temporary files.
		//LOG.debug("emptying temporary directory tempadx");
		// FileHandle dest = Gdx.files.local("tempadx/");
		// try {
		// dest.emptyDirectory();
		// } catch (GdxRuntimeException e) {
		// LOG.error("failed to empty temporary directory tempadx", e);
		// }

		// Dispose custom shaders.
		if (customShaderProgramBatchGame != null)
			customShaderProgramBatchGame.dispose();

	}

	private void renderBackground() {
		viewportPixel.apply(false);
		batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);
		batchPixel.begin();
		if (backgroundImage != null) {
			batchPixel.draw(backgroundImage, backgroundImageRectangle.x, backgroundImageRectangle.y,
					backgroundImageRectangle.width, backgroundImageRectangle.height);
		}
		batchPixel.end();
	}

	private void renderGame() {
		// Clear game window so that the background won't show.
		Gdx.gl.glScissor(viewportGame.getScreenX(), viewportGame.getScreenY(), viewportGame.getScreenWidth(),
				viewportGame.getScreenHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);

		// Update and render the game.
		final float deltaTime = running ? (timeScalingFactor
				* MathUtils.clamp(Gdx.graphics.getRawDeltaTime(), MIN_DELTA_TIME, MAX_DELTA_TIME)) : 0f;

		// Start with the topmost item on the layer stack and proceed
		// with the layers down on the stack only if the layers
		// above did not stop propagation.
		int j = 0;
		gameClock.update(deltaTime);
		for (int i = layerStack.size() - 1; i != -1; --i) {
			final ALayer layer = layerStack.get(i);
			// Save first layer that block drawing.
			if (layer.isBlockDraw() && j != 0)
				j = i;
			// Update layer.
			layer.update(deltaTime);
			// Check if layer block updating, if so, break loop.
			if (layer.isBlockUpdate()) {
				// But first search for the first layer that blocks drawing.
				while (j == 0 && ((--i) != -1))
					if (layerStack.get(i).isBlockDraw())
						j = i;
				break;
			}
		}

		// Render all layer that need to be rendered.
		final int size = layerStack.size();
		for (; j != size; ++j)
			layerStack.get(j).draw(deltaTime);
	}

	private void renderStatusScreen() {
		// Update status screen
		statusScreen.update(timeScalingFactor * Gdx.graphics.getRawDeltaTime());
		// Render status screen
		viewportPixel.apply(false);
		batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);
		shapeRenderer.setProjectionMatrix(viewportPixel.getCamera().combined);
		statusScreen.render();
	}

	private void renderDebug() {
		viewportPixel.apply(false);
		int cnt = 0;
		for (Entity e : gameEntityEngine.getEntities()) {
			cnt += e.getComponents().size();
		}
		batchPixel.begin();
		debugFont.draw(batchPixel, "fps: " + (int) (1.0f / Gdx.graphics.getDeltaTime()), 0.0f,
				viewportGame.getScreenHeight());
		debugFont.draw(batchPixel, "entities: " + gameEntityEngine.getEntities().size(), 0.0f,
				viewportGame.getScreenHeight() - 30.0f);
		debugFont.draw(batchPixel, "components: " + cnt, 0.0f, viewportGame.getScreenHeight() - 60.0f);
		batchPixel.end();
	}

	// TODO remove me for release
	private void createDebugStuff() {
		debugFont = new BitmapFont(Gdx.files.internal("font/debugFont.fnt"));
		debugFont.setColor(Color.RED);
	}

	private void readMonitorInfo() {
		maxMonitorWidth = 0;
		maxMonitorHeight = 0;
		for (Monitor m : Gdx.graphics.getMonitors()) {
			for (DisplayMode dm : Gdx.graphics.getDisplayModes(m)) {
				if (dm.width > maxMonitorWidth)
					maxMonitorWidth = dm.width;
				if (dm.height > maxMonitorHeight)
					maxMonitorHeight = dm.height;
			}
		}
		currentMonitorWidth = Gdx.graphics.getDisplayMode().width;
		currentMonitorHeight = Gdx.graphics.getDisplayMode().height;
	}

	private void computeBackgroundImageRectangle(int width, int height) {
		float cx = width * 0.5f;
		float cy = height * 0.5f;
		float hw = backgroundImage.getRegionWidth();
		float hh = backgroundImage.getRegionHeight();
		float scale = cx / hw;
		scale = Math.max(scale, cy / hh);
		hw *= scale;
		hh *= scale;
		backgroundImageRectangle.set(cx - hw, cy - hh, hw + hw, hh + hh);
	}

	/**
	 * Requests the layer to be remove from the layer stack. It is removed at
	 * the end of the update/render loop.
	 * 
	 * @param layer
	 *            Layer to be removed.
	 */
	public void popLayer(ALayer layer) {
		layerStackPopQueue.add(layer);
	}

	/**
	 * Requests the layer to be added to the layer stack. It is added at the end
	 * of the update/render loop.
	 * 
	 * @param layer
	 *            Layer to be added.
	 */
	public void pushLayer(ALayer layer) {
		layerStackPushQueue.add(layer);
	}

	public void setFragmentShaderBatchGame(FragmentShader fs) {
		if (customShaderProgramBatchGame != null)
			customShaderProgramBatchGame.dispose();
		customShaderProgramBatchGame = new CustomShaderProgram(fs);
		customShaderProgramBatchGame.apply(batchGame);
	}

	public void setGlobalTimeScale(float ts) {
		timeScalingFactor = Math.max(0.0f, ts);
	}

	/**
	 * Called when a level has been completed successfully. Write the highscore
	 * and exit the game.
	 */
	public void gameover() {
		FileHandle handle = Gdx.files.local(highScoreFile);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = handle.read(1024);
		} catch (GdxRuntimeException e) {
			LOG.error("could not open score file", e);
		}
		try {
			os = Gdx.files.local(highScoreFile).write(false, 1024);
			gameScore.writeScore(is, os);
		} finally {
			if (os != null)
				IOUtils.closeQuietly(os);
		}
		if (is != null)
			IOUtils.closeQuietly(is);
		this.exitRequested = true;
		Gdx.app.exit();
	}

	public void setGameLost() {
		gameLost = true;
	}

	public boolean isGameLost() {
		return gameLost;
	}

}