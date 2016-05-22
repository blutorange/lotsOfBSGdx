package de.homelab.madgaksha;

import static de.homelab.madgaksha.GlobalBag.batchGame;
import static de.homelab.madgaksha.GlobalBag.batchInfo;
import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.batchScreen;
import static de.homelab.madgaksha.GlobalBag.bitmapFontRasterSize;
import static de.homelab.madgaksha.GlobalBag.currentMonitorHeight;
import static de.homelab.madgaksha.GlobalBag.currentMonitorWidth;
import static de.homelab.madgaksha.GlobalBag.game;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.maxMonitorHeight;
import static de.homelab.madgaksha.GlobalBag.maxMonitorWidth;
import static de.homelab.madgaksha.GlobalBag.musicPlayer;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.soundPlayer;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.viewportInfo;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;
import static de.homelab.madgaksha.GlobalBag.viewportScreen;

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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
	
	// TODO remove me
	// only for testing
	public static Vector2 testA = new Vector2();
	//ParticleEffect bombEffect;
	private BitmapFont debugFont; 
	// testing end
	
	private final GameParameters params;
	private Texture backgroundImage = null;		
	
	/**@param params Screen size, fps etc. that were requested. */
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
		//TODO remove me
		//testing
//		bombEffect = new ParticleEffect();
//		bombEffect.load(Gdx.files.internal("particle/sparkleEffect.p"), Gdx.files.internal("particle"));
//		bombEffect.setDuration(500000);
//		bombEffect.start();

		// Set logging level.
		Gdx.app.setLogLevel(params.requestedLogLevel);
	
		// Check monitor resolution and get highest possible resolution.
		readMonitorInfo();
		lastWidth = Gdx.graphics.getWidth();
		lastHeight = Gdx.graphics.getHeight();		
		
		// Set size at which freetype fonts will be rasterized.
		bitmapFontRasterSize = Gdx.graphics.getDisplayMode().width/80;
		if (bitmapFontRasterSize < 5) bitmapFontRasterSize = 5;
		
		// Setup audio system.
		AwesomeAudio.initialize();

		// Start with loading resources.
		for (IResource r : level.getRequiredResources()) {
			LOG.debug("fetch " + r);
			if (!ResourceCache.loadToRam(r)) {
				Gdx.app.exit();
				return;
			}
		}
		for (IResource r : player.getRequiredResources()) {
			LOG.debug("fetch " + r);
			if (!ResourceCache.loadToRam(r)) {
				Gdx.app.exit();
				return;
			}
		}
		
		// Initialize resource pool.
		ResourcePool.init();

		// Create music player.
		musicPlayer = new MusicPlayer();
		musicPlayer.loadNext(level.getBgm());
		musicPlayer.transition(2.0f);

		// Create sound player.
		soundPlayer = new SoundPlayer();
		
		// Create sprite batches.
		batchScreen = new SpriteBatch();
		batchGame = new SpriteBatch();
		batchInfo = new SpriteBatch();
		batchPixel = new SpriteBatch();
	
		batchInfo.disableBlending();

		// Initialize the entity engine.
		gameEntityEngine = new Engine();

		// Initialize map and load level.
		if (!level.initialize(batchGame)) {
			exitRequested = true;
			Gdx.app.exit();
			return;
		};
				
        // Get viewports for the game, info and screen windows.
		viewportGame = level.getGameViewport(params.requestedWidth, params.requestedHeight);
		viewportInfo = level.getInfoViewport(params.requestedWidth, params.requestedHeight);
		viewportScreen = level.getScreenViewport(params.requestedWidth, params.requestedHeight);
		viewportPixel = new ScreenViewport();
		
		// Initialize the layer stack.
		// Start off with a layer stack containing only the entity engine.
		entityLayer = new EntityLayer();
		layerStack.add(entityLayer);
		entityLayer.addedToStack();
		
		//TODO remove me for release
		createDebugFont();
		
		// Load background image.
		backgroundImage = level.getBackgroundImage();
				
		// Start the game.
		running = true;
	}

	@Override
	public void render() {
		// Clear the screen before drawing.
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render background screen first.
		renderScreen();

		// Now render the game.
		renderGame();

		// Render info window last.
		renderInfo();
		
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
			for (ALayer layer : layerStackPopQueue) layer.removedFromStack();
		}
		if (layerStackPushQueue.size() > 0) {
			layerStack.addAll(layerStackPushQueue);
			layerStackPushQueue.clear();
			for (ALayer layer : layerStackPushQueue) layer.addedToStack();
		}
	}

	@Override
	public void resize(int width, int height) {
		if (!exitRequested) {
			// Update our viewports.
			viewportGame.update(width, height);
			viewportInfo.update(width, height);
			viewportScreen.update(width, height);
			viewportPixel.update(width, height);
			currentMonitorWidth = width;
			currentMonitorHeight = height;
			bitmapFontRasterSize = viewportGame.getScreenWidth()/40-1;
			if (bitmapFontRasterSize < 5) bitmapFontRasterSize = 5;
			if (width != lastWidth || height != lastHeight) ResourceCache.reloadAllBitmapFont();
			lastWidth = Gdx.graphics.getWidth();
			lastHeight = Gdx.graphics.getHeight();
		}
	}

	@Override
	public void pause() {
		running = false;
		if (musicPlayer != null) musicPlayer.pause();
	}

	@Override
	public void resume() {
		running = true;
		if (musicPlayer != null) musicPlayer.play();
	}

	@Override
	public void dispose() {
		exitRequested = true;
		
		// Dispose music player.
		try {
			if (musicPlayer != null) musicPlayer.dispose();
		} catch (Exception e) {
			LOG.error("could not dispose music player", e);
		}

		// Dispose sound player.
		try {
			if (soundPlayer != null) soundPlayer.dispose();
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
		if (batchInfo != null) batchInfo.dispose();
		if (batchGame != null) batchGame.dispose();
		if (batchScreen != null) batchScreen.dispose();
		if (batchPixel != null) batchPixel.dispose();

		// Remove all entities and systems to trigger cleanup.
		for (ALayer layer : layerStack) {
			layer.removedFromStack();
		}
		layerStack.clear();
		layerStackPopQueue.clear();
		layerStackPushQueue.clear();

		// Dipose background image.
		if (backgroundImage != null) backgroundImage.dispose();

		
		// TODO remove me
		debugFont.dispose();
		//bombEffect.dispose();
	}

	private void renderScreen() {
		viewportScreen.apply(false);
		batchScreen.setProjectionMatrix(viewportScreen.getCamera().combined);
		batchScreen.begin();
		if (backgroundImage != null)
			batchScreen.draw(backgroundImage, 0.0f, 0.0f);

		batchScreen.end();
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
			for (int i = layerStack.size() - 1; i != -1; --i) {
				final ALayer layer = layerStack.get(i);
				// Save first layer that block drawing.
				if (layer.isBlockDraw() && j != 0) j = i;
				// Update layer.
				layer.update(deltaTime);
				// Check if layer block updating, if so, break loop.
				if (layer.isBlockUpdate()) {
					// But first search for the first layer that blocks drawing.
					while (j==0 && ((--i) != -1)) if (layerStack.get(i).isBlockDraw()) j = i;
					break;
				}
			}
		}
		else {
			// Game is paused.
			// Still need to search for the first layer that blocks drawing.
			int i = layerStack.size();
			while (j==0 && ((--i) != -1)) if (layerStack.get(i).isBlockDraw()) j = i;
		}
	
		// Render all layer that need to be rendered.
		final int size = layerStack.size();
		for (; j != size; ++j) layerStack.get(j).draw(deltaTime);
		
		//TODO remove me
		//testing
//		if (Gdx.input.isKeyPressed(Keys.C) && layerStack.size() < 2) {
//			SpeakerTextbox[] tb = new SpeakerTextbox[2];
//			tb[0] = new FaceTextbox("I was just taking a casual stroll, relaxing from work.\nWhere is this? And why am I floating in air?\nCome to think of it, the tomatoes I ate looked kind of\nbad... But this still can't be real!",
//					EBitmapFont.MAIN_FONT,
//					ENinePatch.TEXTBOX_BLUE, "Phantom Joshua", ETexture.FACE_ESTELLE_01);
//			tb[1] = new FaceTextbox("I must have got out of band the wrong... let's do this!",
//					EBitmapFont.MAIN_FONT,
//					ENinePatch.TEXTBOX_BLUE, "Phantom Estelle", ETexture.FACE_ESTELLE_01);
//			this.pushLayer(new TextboxLayer(tb)); 
//		}
//		batchGame.begin();
//		bombEffect.setPosition(1000.0f, 1000.0f);
//		bombEffect.draw(batchGame);
//		bombEffect.setPosition(1400.0f, 1000.0f);
//		bombEffect.draw(batchGame);
//		bombEffect.setPosition(1000.0f, 1400.0f);
//		bombEffect.draw(batchGame);
//		bombEffect.setPosition(1400.0f, 1400.0f);
//		bombEffect.draw(batchGame, deltaTime);
//		batchGame.end();
//		if (bombEffect.isComplete()) bombEffect.reset();
	}

	private void renderInfo() {
		viewportInfo.apply(false);

		batchInfo.setProjectionMatrix(viewportInfo.getCamera().combined);
		batchInfo.begin();

		if (viewportInfo.isLandscapeMode()) {

		} else { // if (viewportInfo.isPortraitMode())
		}

		batchInfo.end();
	}
	
	//TODO remove me for release
	private Vector2 screenPos = new Vector2();
	private void renderDebug() {
		viewportScreen.apply(false);
		batchScreen.begin();
		final Vector2 pos = viewportScreen.unproject(screenPos.set(0.0f,0.0f));
		debugFont.draw(batchScreen, "fps: " + (int)(1.0f/Gdx.graphics.getRawDeltaTime()), pos.x, pos.y);
		batchScreen.end();
	}
	
	//TODO remove me for release
	private void createDebugFont() {
		debugFont = new BitmapFont(Gdx.files.internal("font/debugFont.fnt"));
		debugFont.setColor(Color.RED);
	}

	private void readMonitorInfo() {
		maxMonitorWidth = 0;
		maxMonitorHeight = 0;
		for (Monitor m : Gdx.graphics.getMonitors()) {
			for (DisplayMode dm : Gdx.graphics.getDisplayModes(m)) {
				if (dm.width > maxMonitorWidth) maxMonitorWidth = dm.width;
				if (dm.height > maxMonitorHeight) maxMonitorHeight = dm.height;
			}
		}
		currentMonitorWidth = Gdx.graphics.getDisplayMode().width;
		currentMonitorHeight = Gdx.graphics.getDisplayMode().height;
	}
	
	
	/** Requests the layer to be remove from the layer stack.
	 * It is removed at the end of the update/render loop.
	 * @param layer Layer to be removed.
	 */
	public void popLayer(ALayer layer) {
		layerStackPopQueue.add(layer);
	}
	/** Requests the layer to be added to the layer stack.
	 * It is added at the end of the update/render loop.
	 * @param layer Layer to be added.
	 */
	public void pushLayer(ALayer layer) {
		layerStackPushQueue.add(layer);
	}
	
	
}