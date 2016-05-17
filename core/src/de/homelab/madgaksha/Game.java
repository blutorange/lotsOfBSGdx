package de.homelab.madgaksha;

import static de.homelab.madgaksha.GlobalBag.batchGame;
import static de.homelab.madgaksha.GlobalBag.batchInfo;
import static de.homelab.madgaksha.GlobalBag.batchScreen;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.musicPlayer;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.soundPlayer;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.viewportInfo;
import static de.homelab.madgaksha.GlobalBag.viewportScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.audiosystem.AwesomeAudio;
import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.layer.ALayer;
import de.homelab.madgaksha.layer.EntityLayer;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcecache.ResourceCache;

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
	
	// TODO remove me
	// only for testing
	public static Vector2 testA = new Vector2();
	ParticleEffect bombEffect;
	private BitmapFont debugFont; 
	// testing end
	
	private final GameParameters params;
	private Texture backgroundImage;		
	
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
	}

	@Override
	public void create() {
		//testing
		bombEffect = new ParticleEffect();
		bombEffect.load(Gdx.files.internal("particle/sparkleEffect.p"), Gdx.files.internal("particle"));
		bombEffect.setDuration(500000);
		bombEffect.start();
		
		// Setup audio system.
		AwesomeAudio.initialize();

		// Set logging level.
		Gdx.app.setLogLevel(params.requestedLogLevel);

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

		// Create music player.
		musicPlayer = new MusicPlayer();
		musicPlayer.loadNext(level.getBgm());
		musicPlayer.transition(2.0f);

		// Create sound player.
		soundPlayer = new SoundPlayer();
		
		// Load background image.
		backgroundImage = level.getBackgroundImage();

		// Create sprite batches.
		batchScreen = new SpriteBatch();
		batchGame = new SpriteBatch();
		batchInfo = new SpriteBatch();
	
		batchInfo.disableBlending();

		// Initialize map.
		if (!level.initialize(batchGame)) {
			Gdx.app.exit();
			return;
		};
		
        // Get viewports for the game, info and screen windows.
		viewportGame = level.getGameViewport(params.requestedWidth, params.requestedHeight);
		viewportInfo = level.getInfoViewport(params.requestedWidth, params.requestedHeight);
		viewportScreen = level.getScreenViewport(params.requestedWidth, params.requestedHeight);
				
		// Initialize the layer stack.
		// Start off with a layer stack containing only the entity engine.
		// This initializes the entity engine as well.
		entityLayer = new EntityLayer();
		layerStack.add(entityLayer);
		
		//TODO remove me for release
		createDebugFont();
		
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
		// Update our viewports.
		viewportGame.update(width, height);
		viewportInfo.update(width, height);
		viewportScreen.update(width, height);
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

		// Remove all entities and systems to trigger cleanup.
		for (ALayer layer : layerStack) {
			layer.removedFromStack();
		}
		layerStack.clear();
		layerStackPopQueue.clear();
		layerStackPushQueue.clear();

		// TODO remove me
		debugFont.dispose();
		bombEffect.dispose();
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
		int mode = running ? ALayer.PROCESS_FULLY : ALayer.DRAW_ONLY;
		int i = layerStack.size();
		while (mode == ALayer.PROCESS_FULLY && --i >= 0) {
			mode -= layerStack.get(i).processFully(deltaTime);
		}
		while (mode == ALayer.DRAW_ONLY && --i >= 0) {
			mode -= layerStack.get(i).drawOnly(deltaTime);
		}
		
		//TODO remove me
		//testing
		batchGame.begin();
		bombEffect.setPosition(1000.0f, 1000.0f);
		bombEffect.draw(batchGame,0.25f*deltaTime);
		bombEffect.setPosition(1400.0f, 1000.0f);
		bombEffect.draw(batchGame,0.25f*deltaTime);
		bombEffect.setPosition(1000.0f, 1400.0f);
		bombEffect.draw(batchGame,0.25f*deltaTime);
		bombEffect.setPosition(1400.0f, 1400.0f);
		bombEffect.draw(batchGame,0.25f*deltaTime);
		batchGame.end();
		//if (bombEffect.isComplete()) bombEffect.reset();
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
	
	//TODO remove me for release
	private Vector2 screenPos = new Vector2();
	public void renderDebug() {
		viewportScreen.apply(false);
		batchScreen.begin();
		final Vector2 pos = viewportScreen.unproject(screenPos.set(0.0f,0.0f));
		debugFont.draw(batchScreen, "fps: " + (int)(1.0f/Gdx.graphics.getRawDeltaTime()), pos.x, pos.y);
		batchScreen.end();
	}
	
	//TODO remove me for release
	public void createDebugFont() {
		debugFont = new BitmapFont(Gdx.files.internal("font/debugFont.fnt"));
		debugFont.setColor(Color.RED);
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