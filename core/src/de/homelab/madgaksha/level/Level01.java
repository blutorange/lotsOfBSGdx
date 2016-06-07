package de.homelab.madgaksha.level;

import static de.homelab.madgaksha.GlobalBag.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.maps.MapProperties;

import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.CutsceneEventProvider;
import de.homelab.madgaksha.cutscenesystem.event.EventTextbox;
import de.homelab.madgaksha.cutscenesystem.event.EventWait;
import de.homelab.madgaksha.cutscenesystem.textbox.EFaceVariation;
import de.homelab.madgaksha.enums.ESpeaker;
import de.homelab.madgaksha.layer.CutsceneLayer;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EFreeTypeFontGenerator;
import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETextbox;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ETiledMap;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcepool.EventTextboxPool;;

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
		game.pushLayer(new CutsceneLayer(new CutsceneEventProvider() {
			private EventTextbox event;
			public void initialize() {
				event = EventTextboxPool.getInstance().obtain();
				event.setTextbox(ETextbox.FC_BLUE);
				event.setTextColor(Color.WHITE);
				event.setFont(EFreeTypeFontGenerator.MAIN_FONT);
			}
			
			@Override
			public ACutsceneEvent nextCutsceneEvent(int i) {
				switch (i) {
				case 0:
					return new EventWait(2.5f);
				case 1:
					event.setLines("ただの散歩のつもりだったけど、ここはどこ？\nそれで空に浮いているってどういうこと？？\nもう、わけ分かんない！");
					event.setSpeaker(ESpeaker.ESTELLE);
					event.setFaceVariation(EFaceVariation.ANGRY);
					break;
				case 2:
					event.setLines("ボーズでヨシュアとブレイザーらしく\n行動するのに一所懸命に頑張ってたけど、\n町をでた途端こんなことに。。。");
					event.setSpeaker(ESpeaker.ESTELLE);
					event.setFaceVariation(EFaceVariation.EVASIVE);
					break;
				case 3:
					event.setLines("とりあえず、もやもやするよりこの辺りで\nヒント探しにでも行こうかな。。。！");
					event.setFaceVariation(EFaceVariation.SHOU_GA_NAI);
					break;
				default:
					return null;
				}
				return event;
			}
		}));
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
