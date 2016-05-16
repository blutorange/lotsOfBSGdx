package de.homelab.madgaksha;

import java.util.Locale;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.audiosystem.AwesomeAudio;
import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.entityengine.component.InputComponent;
import de.homelab.madgaksha.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.entityengine.entitysystem.BirdsViewSpriteSystem;
import de.homelab.madgaksha.entityengine.entitysystem.CameraTracingSystem;
import de.homelab.madgaksha.entityengine.entitysystem.DanmakuSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantPositionSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantRotationSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantScaleSystem;
import de.homelab.madgaksha.entityengine.entitysystem.InputVelocitySystem;
import de.homelab.madgaksha.entityengine.entitysystem.MovementSystem;
import de.homelab.madgaksha.entityengine.entitysystem.NewtonianForceSystem;
import de.homelab.madgaksha.entityengine.entitysystem.PostEffectSystem;
import de.homelab.madgaksha.entityengine.entitysystem.SpriteAnimationSystem;
import de.homelab.madgaksha.entityengine.entitysystem.SpriteRenderSystem;
import de.homelab.madgaksha.entityengine.entitysystem.TemporalSystem;
import de.homelab.madgaksha.entityengine.entitysystem.ViewportUpdateSystem;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.enums.Gravity;
import de.homelab.madgaksha.enums.TrackingOrientationStrategy;
import de.homelab.madgaksha.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.level.GameViewport;
import de.homelab.madgaksha.level.InfoViewport;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.resourcecache.EAnimationList;
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

	/**
	 * For playing music. No other instance should be created.
	 */
	private static MusicPlayer musicPlayer = null;

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
	public static Vector2 testA = new Vector2();
	public static float testx = 0.5f;
	public static float testy = 0.5f;
	public static float testx2 = 0.5f;
	public static float testy2 = 0.5f;
	public static float test1 = 0.0f;
	public static float test2 = 0.0f;
	public static float test3 = 0.0f;
	public static float test4 = 0.0f;
	public static float test5 = 0.0f;
	public static float test6 = 0.0f;
	public static float test7 = 100.0f;
	ParticleEffect bombEffect;
	// testing end
	
	private BitmapFont debugFont; 

	private final GameParameters params;
	private final ALevel level;
	private final APlayer player;
	
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
		this.player = params.requestedPlayer;
		this.level = params.requestedLevel;
		GlobalBag.level = this.level;
		
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
		//testing
		bombEffect = new ParticleEffect();
		bombEffect.load(Gdx.files.internal("particle/sparkleEffect.p"), Gdx.files.internal("particle"));
		bombEffect.setDuration(500000);
		bombEffect.scaleEffect(2.0f);
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
		Game.musicPlayer = new MusicPlayer();
		Game.musicPlayer.loadNext(level.getBgm());
		Game.musicPlayer.transition(2.0f);

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
		GlobalBag.viewportGame = viewportGame;
		
		// Create a new entity engine and setup basic entity systems.
		createEntityEngine();
		
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
			LOG.error("could not clear music player", e);
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
		if (entityEngine != null) {
			entityEngine.removeAllEntities();
			for (EntitySystem es : entityEngine.getSystems()) {
				entityEngine.removeSystem(es);
			}
		}

		// TODO
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
		if (running) {
			entityEngine.update(deltaTime);
		} else {
			final SpriteRenderSystem spriteRenderSystem = entityEngine.getSystem(SpriteRenderSystem.class);
			if (spriteRenderSystem != null) {
				viewportGame.apply();
				spriteRenderSystem.update(deltaTime);
			}
			// entityEngine.getSystem(Draw3dSystem.class).update(deltaTime);
		}
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
//		bombEffect.update(deltaTime);
		if (bombEffect.isComplete()) bombEffect.reset();
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
	

	public void createEntityEngine() {
		entityEngine = new Engine();

		entityEngine.addSystem(new BirdsViewSpriteSystem(viewportGame));
		entityEngine.addSystem(new SpriteAnimationSystem());
		entityEngine.addSystem(new SpriteRenderSystem(viewportGame, batchGame));
		entityEngine.addSystem(new CameraTracingSystem(viewportGame));
		entityEngine.addSystem(new ViewportUpdateSystem());
		entityEngine.addSystem(new GrantPositionSystem());
		entityEngine.addSystem(new GrantRotationSystem());
		entityEngine.addSystem(new GrantScaleSystem());
		entityEngine.addSystem(new NewtonianForceSystem());
		entityEngine.addSystem(new MovementSystem());
		entityEngine.addSystem(new DanmakuSystem());
		entityEngine.addSystem(new InputVelocitySystem());
		entityEngine.addSystem(new PostEffectSystem());
		entityEngine.addSystem(new TemporalSystem());

		GlobalBag.playerEntity = createPlayerEntity();
		
		Entity yourEntity = new Entity();
		SpriteForDirectionComponent sfdc2 = new SpriteForDirectionComponent(EAnimationList.JOSHUA_RUNNING,
				ESpriteDirectionStrategy.ZENITH);
		SpriteAnimationComponent sac2 = new SpriteAnimationComponent(sfdc2);
		yourEntity.add(new SpriteComponent(sac2));
		yourEntity.add(new PositionComponent(level.getMapWidthW()/2.0f, 50.0f*32.0f));
		yourEntity.add(sfdc2);
		yourEntity.add(sac2);
		yourEntity.add(new RotationComponent(true));
		yourEntity.add(new BoundingSphereComponent(70.0f));
		yourEntity.add(new DirectionComponent(90.0f));
		yourEntity.add(new TemporalComponent());

		Entity myCamera = new Entity();
		ManyTrackingComponent mtc = new ManyTrackingComponent(level.getMapXW(), level.getMapYW(), level.getMapWidthW(),
				level.getMapHeightW());
		mtc.focusPoints.add(GlobalBag.playerEntity);
		mtc.focusPoints.add(yourEntity);
		mtc.playerPoint = GlobalBag.playerEntity;
		mtc.bossPoint = yourEntity;
		mtc.gravity = Gravity.SOUTH;
		mtc.trackingOrientationStrategy = TrackingOrientationStrategy.RELATIVE;
		myCamera.add(mtc);
		myCamera.add(new PositionComponent(1920/4,1080/2));
		myCamera.add(new RotationComponent());
		
		myCamera.add(new ShouldPositionComponent(new ExponentialGrantStrategy(0.6f, 0.25f)));
		myCamera.add(new ShouldRotationComponent(new ExponentialGrantStrategy(0.6f, 0.25f)));
		myCamera.add(new ViewportComponent(viewportGame));
		myCamera.add(new TemporalComponent());

		
//		Entity myProjectile = new Entity();
//		myProjectile.add(new PositionComponent(1920.0f/4.0f,500.0f));
//		myProjectile.add(new VelocityComponent(60.0f,0.0f));
//		myProjectile.add(new TrajectoryComponent());
//		myProjectile.add(new ForceComponent());
//		myProjectile.add(new SpriteComponent(ETexture.TEST_PROJECTILE));
		
		entityEngine.addEntity(GlobalBag.playerEntity);
		entityEngine.addEntity(yourEntity);
		entityEngine.addEntity(myCamera);
//		entityEngine.addEntity(myProjectile);

	}

	private Entity createPlayerEntity() {
		Entity playerEntity = new Entity();

		SpriteForDirectionComponent sfdc = new SpriteForDirectionComponent(player.getAnimationList(),
				ESpriteDirectionStrategy.ZENITH);
		SpriteAnimationComponent sac = new SpriteAnimationComponent(sfdc);
		SpriteComponent sc = new SpriteComponent(sac);
		
		playerEntity.add(new LeanEffectComponent(30.0f,0.10f,10.0f));
		playerEntity.add(new HoverEffectComponent(8.0f, 1.0f));
		
		playerEntity.add(new ShouldRotationComponent(new ExponentialGrantStrategy(0.1f)));
		playerEntity.add(new ShouldScaleComponent(new ExponentialGrantStrategy(0.1f)));
		
		playerEntity.add(sc);
		playerEntity.add(sac);
		playerEntity.add(sfdc);

		playerEntity.add(new BoundingSphereComponent(player.getBoundingCircle().radius*3.0f));
		playerEntity.add(new PositionComponent(level.getPlayerInitialPosition(), true));
		playerEntity.add(new VelocityComponent(0.0f, 0.0f));
		playerEntity.add(new RotationComponent(true));
		playerEntity.add(new ScaleComponent());
		playerEntity.add(new DirectionComponent());

		playerEntity.add(new InputComponent(player.getMovementSpeed()));
		playerEntity.add(new TemporalComponent());
		return playerEntity;

	}
}