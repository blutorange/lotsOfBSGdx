package de.homelab.madgaksha.lotsofbs.player.tokugi;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.TimeUtils;

import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EFancyScene;
import de.homelab.madgaksha.lotsofbs.resourcecache.EModel;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

public class TokugiOukaMusougeki extends ATokugi {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TokugiOukaMusougeki.class);

	private final static long COOLDOWN = 30000;
	private long previousTime = TimeUtils.millis() - COOLDOWN;

	@Override
	protected ETexture requestedSign() {
		return ETexture.TOKUGI_OUKAMUSOUGEKI_SIGN;
	}

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.TOKUGI_OUKAMUSOUGEKI_ICON_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.TOKUGI_OUKAMUSOUGEKI_ICON_SUB;
	}

	@Override
	public EModel getModel() {
		return EModel.ITEM_TOKUGI_OUKAMUSOUGEKI;
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource<?, ?>[] { ETexture.TOKUGI_OUKAMUSOUGEKI_ICON_MAIN, ETexture.TOKUGI_OUKAMUSOUGEKI_ICON_SUB,
				ETexture.TOKUGI_OUKAMUSOUGEKI_SIGN, EFancyScene.OUKA_MUSOUGEKI_BIN, EModel.ITEM_TOKUGI_OUKAMUSOUGEKI, };
	}

	@Override
	public boolean openFire(Entity player, float deltaTime) {
		long currentTime = TimeUtils.millis();
		if (currentTime - previousTime < COOLDOWN || !isSomethingTargetted()) {
			SoundPlayer.getInstance().play(ESound.CANNOT_USE);
			return false;
		}
		previousTime = currentTime;
		level.pushFancyScene(ResourceCache.getEventFancyScene(EFancyScene.OUKA_MUSOUGEKI_BIN), new Runnable() {
			@Override
			public void run() {
				dealFinalDamagePoint();
			}
		});
		ComponentUtils.convertAllActiveBulletsToScoreBullets();
		return true;
	}

	@Override
	protected long requestedRequiredScore() {
		return 60000;
	}

	@Override
	protected long requestedRemainingPainPoints() {
		return 420000000 / 100 * 21;
	}

	@Override
	protected float requestedSignDelay() {
		return 0.1f;
	}
}
