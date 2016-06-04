package de.homelab.madgaksha.level;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.maps.MapProperties;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.cutscenesystem.FaceTextbox;
import de.homelab.madgaksha.cutscenesystem.Textbox;
import de.homelab.madgaksha.layer.TextboxLayer;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EBitmapFont;
import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ENinePatch;
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
		//TODO
		//add some cutscene maker util
		//add some callback
		//InputVelocitySystem ivc = GlobalBag.gameEntityEngine.getSystem(InputVelocitySystem.class);
		//if (ivc != null) ivc.setProcessing(false);
		LOG.debug("initialDialog triggered");
		Textbox[] tb = TextboxLayer.POOL;
		tb[0] = new FaceTextbox("I was just taking a casual stroll, relaxing from work.\nWhere is this? And why am I floating in air?\nCome to think of it, the tomatoes I ate looked kind of\nbad... But this still can't be real!",
				EBitmapFont.MAIN_FONT,
				ENinePatch.TEXTBOX_BLUE, "Phantom Joshua", ETexture.FACE_ESTELLE_01);
		tb[1] = new FaceTextbox("I must have got out of band the wrong... let's do this!",
				EBitmapFont.MAIN_FONT,
				ENinePatch.TEXTBOX_BLUE, "Phantom Estelle", ETexture.FACE_ESTELLE_01);
		GlobalBag.game.pushLayer(new TextboxLayer(tb,0,2));
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
