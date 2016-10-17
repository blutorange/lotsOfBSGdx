package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor2;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import java.util.Locale;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.PlayerMaker;
import de.homelab.madgaksha.lotsofbs.i18n.I18n;
import de.homelab.madgaksha.lotsofbs.level.LevelMock;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.PEstelle;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;

/**
 * Utility that checks whether all game resources exist. You should run this
 * with the working directory set to the assets directory for the actual game.
 *
 * @author madgaksha
 */
public class MyFancySceneEditor2 implements ApplicationListener {
	private Logger LOG;
	private Stage stage;
	private Table table;
	private Skin skin;

	private MyFancySceneEditor2() throws GdxRuntimeException {
	}

	@Override
	public void create() {
		LOG = Logger.getLogger(MyFancySceneEditor2.class);
		Logger.setDefaultLevel(Logger.ALL);

		setupGameDependencies();

		stage = new Stage(new ExtendViewport(1920, 1080));
		Gdx.input.setInputProcessor(stage);
		initialize();

		LOG.debug("new editor created");
	}

	private void initialize() {
	}

	@Override
	public void resize(final int width, final int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		final float deltaTime = Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		ResourcePool.clearAllParticleEffect();
		ResourceCache.clearAll();
	}

	public static void main(final String args[]) {
		final MyFancySceneEditor2 editor = new MyFancySceneEditor2();
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		@SuppressWarnings("unused")
		final LwjglApplication lwjglApplication = new LwjglApplication(editor, config);
	}

	private void setupGameDependencies() {
		ResourcePool.init();
		I18n.init(Locale.ENGLISH);
		batchPixel = new SpriteBatch();
		gameEntityEngine = new PooledEngine();
		level = new LevelMock();
		cameraTrackingComponent = new ManyTrackingComponent();
		player = new PEstelle();
		playerEntity = PlayerMaker.getInstance().makePlayer();
		playerBattleStigmaEntity = PlayerMaker.getInstance().makePlayerBattleStigma(playerEntity, player);
		playerHitCircleEntity = PlayerMaker.getInstance().makePlayerHitCircle(player);
		level.initialize(batchPixel);
		PlayerMaker.getInstance().setupPlayer(player);
		PlayerMaker.getInstance().setupPlayerHitCircle(player);
	}
}