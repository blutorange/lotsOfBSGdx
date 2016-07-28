package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import java.util.Locale;

import org.apache.commons.io.IOUtils;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
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
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.basic.BasicTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.clipdata.DrawableClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.clipdata.ShakeClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.BeginningEndButton;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.DetailsWindow;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.EnemySelector;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.ClipTable;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.FpsInput;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.FrameCountInput;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.FrameSeekButton;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.PlayPauseButton;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.view.Preview;

/**
 * Utility that checks whether all game resources exist.
 * You should run this with the working directory set to the assets directory for the actual game.
 * 
 * @author madgaksha
 */
public class MyFancySceneEditor implements ApplicationListener, TimelineProvider {
	private Logger LOG;	

	private Preview preview;
	private Stage stage;
	private Table table;
	private Skin skin;
	private ModelTimeline timeline;
	
	private MyFancySceneEditor() throws GdxRuntimeException {
	}

	@Override
	public void create() {
		LOG = Logger.getLogger(MyFancySceneEditor.class);
		Logger.setDefaultLevel(Logger.ALL);
		
		setupGameDependencies();
		
		stage = new Stage(new ExtendViewport(1920,1080));
		Gdx.input.setInputProcessor(stage);
		initialize();
		
		LOG.debug("new editor created");
	}

	private void initialize() {	
		skin = new Skin(Gdx.files.internal("skin/default/uiskin.json"));
		timeline = BasicTimeline.newTimeline(15f);
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new ShakeClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		timeline.newTrack(0f, 1f, null).newClip(0f, 1f, new DrawableClipData());
		
		table = new Table(skin);
		table.debug();
		table.setFillParent(true);
		
		Table previewTable = new Table();
		ClipTable eventTable = new ClipTable(skin, this);

		preview = new Preview(this, skin);
		
		DetailsWindow detailsWindow = new DetailsWindow(this, skin);
		
		FrameSeekButton buttonNextFrame = new FrameSeekButton(skin, "next", this, true);
		FrameSeekButton buttonPreviousFrame = new FrameSeekButton(skin, "prev", this, false);
		PlayPauseButton buttonPlayPause = new PlayPauseButton(skin, "play", "pause", this);
		BeginningEndButton buttonBeginning = new BeginningEndButton(skin, "beg", this, true);
		BeginningEndButton buttonEnd = new BeginningEndButton(skin, "end", this, false);
		FpsInput fpsInput = new FpsInput(this, skin);
		FrameCountInput frameInput = new FrameCountInput(this, skin);
		EnemySelector enemySelector = new EnemySelector(skin);
		
		Table hgPreviewControls = new Table(skin);
		
		previewTable.setDebug(true);
		previewTable.setTransform(true);
		previewTable.add(preview).expand().fill();
		previewTable.row();
		previewTable.add(hgPreviewControls);
		
		hgPreviewControls.add(fpsInput);
		hgPreviewControls.add(buttonBeginning).size(32f);
		hgPreviewControls.add(buttonPreviousFrame).size(32f);
		hgPreviewControls.add(buttonPlayPause).size(32f);
		hgPreviewControls.add(buttonNextFrame).size(32f);
		hgPreviewControls.add(buttonEnd).size(32f);
		hgPreviewControls.add(frameInput);
		hgPreviewControls.add(enemySelector);
		
		ScrollPane scrollEventTable = new ScrollPane(eventTable, skin);
					
		SplitPane split2 = new SplitPane(previewTable, scrollEventTable, true, skin);		
		SplitPane split1 = new SplitPane(split2, detailsWindow, false, skin);
		
		table.add(split1).expand().fill();
		table.setFillParent(true);
		table.invalidateHierarchy();
		
		
		stage.addActor(table);
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		final float deltaTime = Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
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
		IOUtils.closeQuietly(preview);
		stage.dispose();
		ResourcePool.clearAllParticleEffect();
		ResourceCache.clearAll();		
	}
	
	public static void main(String args[]) {
		MyFancySceneEditor editor = new MyFancySceneEditor();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(editor, config);
	}

	@Override
	public ModelTimeline getTimeline() {
		return timeline;
	}
	
	private void setupGameDependencies() {
		ResourcePool.init();
		I18n.init(Locale.ENGLISH);
		batchPixel = new SpriteBatch();
		gameEntityEngine = new PooledEngine();
		level = new LevelMock();
		cameraTrackingComponent = new ManyTrackingComponent();
		player = new PEstelle();
		playerEntity = PlayerMaker.getInstance().makePlayer(player);
		playerBattleStigmaEntity = PlayerMaker.getInstance().makePlayerBattleStigma(playerEntity, player);
		playerHitCircleEntity = PlayerMaker.getInstance().makePlayerHitCircle(player);
		level.initialize(batchPixel);
		PlayerMaker.getInstance().setupPlayer(player);
		PlayerMaker.getInstance().setupPlayerHitCircle(player);
	}
}