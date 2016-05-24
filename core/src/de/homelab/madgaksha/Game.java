package de.homelab.madgaksha;

import static de.homelab.madgaksha.GlobalBag.batchGame;
import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.bitmapFontRasterSize;
import static de.homelab.madgaksha.GlobalBag.currentMonitorHeight;
import static de.homelab.madgaksha.GlobalBag.currentMonitorWidth;
import static de.homelab.madgaksha.GlobalBag.game;
import static de.homelab.madgaksha.GlobalBag.gameClock;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.maxMonitorHeight;
import static de.homelab.madgaksha.GlobalBag.maxMonitorWidth;
import static de.homelab.madgaksha.GlobalBag.musicPlayer;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.shapeRenderer;
import static de.homelab.madgaksha.GlobalBag.soundPlayer;
import static de.homelab.madgaksha.GlobalBag.statusScreen;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.homelab.madgaksha.audiosystem.AwesomeAudio;
import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.layer.ALayer;
import de.homelab.madgaksha.layer.EntityLayer;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.ResourcePool;
import de.homelab.madgaksha.util.Clock;

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
	/**
	 * This will cause slowdown on slow devices, but game logic would get messed
	 * up for high dt.
	 */
	public final static float MAX_DELTA_TIME = 0.1f;

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

	//TODO
	// remove me, for testing only
	private BitmapFont debugFont;
	// testing end

	private final GameParameters params;
	private TextureRegion backgroundImage = null;
	private Rectangle backgroundImageRectangle = new Rectangle();

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
		if (!i18n.isInitiated()) {
			if (params.requestedLocale != null)
				i18n.init(params.requestedLocale);
			else
				i18n.init(Locale.getDefault());
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

		// Setup audio system.
		AwesomeAudio.initialize();

		// Start with loading resources.
		for (IResource<? extends Enum<?>,?> r : level.getRequiredResources()) {
			LOG.debug("fetch " + r);
			if (!ResourceCache.loadToRam(r)) {
				Gdx.app.exit();
				return;
			}
		}
		for (IResource<? extends Enum<?>,?> r : player.getRequiredResources()) {
			LOG.debug("fetch " + r);
			if (!ResourceCache.loadToRam(r)) {
				Gdx.app.exit();
				return;
			}
		}
		if ((backgroundImage = level.getBackgroundImage()) == null) {
			Gdx.app.exit();
			return;
		}
		
		// Initialize resource pool.
		ResourcePool.init();

		// Create music player.
		musicPlayer = new MusicPlayer();
		musicPlayer.loadNext(level.getBgm());
		musicPlayer.transition(2.0f);

		// Create sound player.
		soundPlayer = new SoundPlayer();

		// Create batches.
		batchGame = new SpriteBatch();
		batchPixel = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		
		// Initialize the entity engine.
		gameEntityEngine = new Engine();

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
		computeBackgroundImageRectangle(currentMonitorWidth, currentMonitorHeight);
		
		// Initialize the layer stack.
		// Start off with a layer stack containing only the entity engine.
		entityLayer = new EntityLayer();
		layerStack.add(entityLayer);
		entityLayer.addedToStack();

		// Keep track of the time.
		gameClock = new Clock();
		
		// TODO remove me for release
		createDebugFont();

		// Start the game.
		running = true;
	}

	@Override
	public void render() {
		// Clear the screen before drawing.
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render background first.
		renderBackground();

		// Now render the game.
		renderGame();

		// Render info window last.
		renderStatusScreen();

		//TODO remove me for release
		// Render debug.
		renderDebug();

		// Process layer stack queue.
		// Must be done at once after the update and
		// render methods, or sync becomes an issue.
		// Adding / removing layers does not happen
		// frequently, so we check if there is any
		// work to be done first.
		if (layerStackPopQueue.size() > 0) {
			layerStack.removeAll(layerStackPopQueue);
			layerStackPopQueue.clear();
			for (ALayer layer : layerStackPopQueue)
				layer.removedFromStack();
		}
		if (layerStackPushQueue.size() > 0) {
			layerStack.addAll(layerStackPushQueue);
			layerStackPushQueue.clear();
			for (ALayer layer : layerStackPushQueue)
				layer.addedToStack();
		}
	}

	@Override
	public void resize(int width, int height) {
		if (!exitRequested) {
			computeBackgroundImageRectangle(width, height);
			
			// Update our viewports.
			viewportGame.update(width, height, false);
			viewportPixel.update(width, height, true);
			statusScreen.update(width, height);
			
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
		}
	}

	@Override
	public void pause() {
		running = false;
		if (musicPlayer != null)
			musicPlayer.pause();
	}

	@Override
	public void resume() {
		running = true;
		if (musicPlayer != null)
			musicPlayer.play();
	}

	@Override
	public void dispose() {
		exitRequested = true;

		// Dispose music player.
		try {
			if (musicPlayer != null)
				musicPlayer.dispose();
		} catch (Exception e) {
			LOG.error("could not dispose music player", e);
		}

		// Dispose sound player.
		try {
			if (soundPlayer != null)
				soundPlayer.dispose();
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
		for (ALayer layer : layerStack) {
			layer.removedFromStack();
		}
		layerStack.clear();
		layerStackPopQueue.clear();
		layerStackPushQueue.clear();

		// Dispose background image.
		if (backgroundImage != null)
			backgroundImage.getTexture().dispose();

		// TODO remove me
		if (debugFont != null) debugFont.dispose();
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
		final float deltaTime = timeScalingFactor * Math.min(Gdx.graphics.getRawDeltaTime(), MAX_DELTA_TIME);

		// Start with the topmost item on the layer stack and proceed
		// with the layers down on the stack only if the layers
		// above did not stop propagation.
		int j = 0;
		if (running) {
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
		} else {
			// Game is paused.
			// Still need to search for the first layer that blocks drawing.
			int i = layerStack.size();
			while (j == 0 && ((--i) != -1))
				if (layerStack.get(i).isBlockDraw())
					j = i;
		}

		// Render all layer that need to be rendered.
		final int size = layerStack.size();
		for (; j != size; ++j)
			layerStack.get(j).draw(deltaTime);
	}

	private void renderStatusScreen() {
		viewportPixel.apply(false);
		batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);
		shapeRenderer.setProjectionMatrix(viewportPixel.getCamera().combined);
		statusScreen.render();
	}

	private void renderDebug() {
		viewportPixel.apply(false);
		batchPixel.begin();
		debugFont.draw(batchPixel, "fps: " + (int) (1.0f / Gdx.graphics.getRawDeltaTime()), 0.0f, viewportGame.getScreenHeight());
		batchPixel.end();
	}

	// TODO remove me for release
	private void createDebugFont() {
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
		float cx = width*0.5f;
		float cy = height*0.5f;
		float hw = backgroundImage.getRegionWidth();
		float hh = backgroundImage.getRegionHeight();
		float scale = cx/hw;
		scale = Math.max(scale, cy/hh);
		hw *= scale;
		hh *= scale;
		backgroundImageRectangle.set(cx-hw, cy-hh, hw+hw, hh+hh);
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
	

}