package de.homelab.madgaksha.level;

import static de.homelab.madgaksha.GlobalBag.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.maps.MapProperties;

import de.homelab.madgaksha.cutscenesystem.provider.CutsceneEventProvider;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.layer.CutsceneLayer;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ETiledMap;
import de.homelab.madgaksha.resourcecache.IResource;;

/**
 * Only for testing purposes.
 * 
 * @author madgaksha
 */
public class Level01 extends ALevel {
	private final static Logger LOG = Logger.getLogger(Level01.class);
	
	@Override
	protected ETexture requestedBackgroundImage() {
		return ETexture.MAIN_BACKGROUND;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IResource<? extends Enum<?>,?>[] requestedRequiredResources() {
		return new IResource[] {
				ETexture.JOSHUA_RUNNING,
				EMusic.ROCK_ON_THE_ROAD,
				EMusic.SOPHISTICATED_FIGHT,
				EMusic.SILVER_WILL,
				EMusic.FADING_STAR,
				ETiledMap.LEVEL_01
		};
	}

	@Override
	protected ETiledMap requestedTiledMap() {
		return ETiledMap.LEVEL_01;
	}	
	
	@Override
	protected EMusic requestedBgm() {
		return EMusic.ROCK_ON_THE_ROAD;
	}
	
	@Override
	protected EMusic requestedBattleBgm() {
		return EMusic.SOPHISTICATED_FIGHT;
	}
	@Override
	protected EMusic requestedGameOverBgm() {
		return EMusic.FADING_STAR;
	}
	
	@Override
	protected String requestedI18nNameKey() {
		return "level.01.name";
	}
	
	@Override
	protected void setupInitialGameViewport(GameViewport viewport) {
		viewport.getCamera().position.x = getMapData().getPlayerInitialPosition().x;
		viewport.getCamera().position.y = -50.0f*32.0f;
		viewport.getCamera().position.z = 1000.0f;
		viewport.getCamera().up.x = 0;
		viewport.getCamera().up.y = 1;
	}
	
	@Override
	public ETexture requestedIcon() {
		return ETexture.LEVEL_01_ICON;
	}

	@Override
	public int getComponentPoolInitialSize() {
		return 1000;
	}
	@Override
	public int getComponentPoolMaxSize() {
		return 40000;
	}
	@Override
	public int getEntityPoolInitialSize() {
		return 100;
	}
	@Override
	public int getEntityPoolPoolMaxSize() {
		return 10000;
	}
	
	@Override
	public void setupEnvironment(Environment environment) {
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f,0.4f,0.4f,1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 0.0f, -1.0f));
	}
	
	// =========================
	//      Callback methods
	// =========================
	
	public void initialDialog(MapProperties properties) {
		LOG.debug("initialDialog triggered");
		CutsceneEventProvider provider = new FileCutsceneProvider(Gdx.files.internal("cutscene/level01.initialDialog"));
		game.pushLayer(new CutsceneLayer(provider));
	}

	@Override
	protected float requestedEnemyTargetCrossAngularVelocity() {
		return -40.0f;
	}

	@Override
	protected ETexture requestedEnemyTargetCrossTexture() {
		return ETexture.TARGET_SELECT_CIRCLE_WHEEL;
	}

	@Override
	protected ESound requestedSoundOnBattleWin() {
		return ESound.POSAUNEN_CHORUS;
	}
}
