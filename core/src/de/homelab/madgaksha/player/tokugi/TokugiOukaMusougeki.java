package de.homelab.madgaksha.player.tokugi;

import static de.homelab.madgaksha.GlobalBag.level;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EModel;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;

public class TokugiOukaMusougeki extends ATokugi {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TokugiOukaMusougeki.class);
	private final static String PATH = "cutscene/fancyScene/ougiOukaMusougeki/ougiOukaMusougeki.event";

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
		return EModel.ITEM_WEAPON_BASIC;
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource<?,?>[]{
			ETexture.TOKUGI_OUKAMUSOUGEKI_ICON_MAIN,
			ETexture.TOKUGI_OUKAMUSOUGEKI_ICON_SUB,
			ETexture.TOKUGI_OUKAMUSOUGEKI_SIGN,
			EModel.ITEM_WEAPON_BASIC,
		};
	}

	@Override
	public void openFire(Entity player, float deltaTime) {
		level.pushCutsceneLayer(PATH, new Runnable() {
			@Override
			public void run() {
				dealFinalDamagePoint();
			}
		});
	}

	@Override
	protected long requestedRequiredScore() {
		return 60000;
	}

	@Override
	protected long requestedRemainingPainPoints() {
		return 420000000/100*21;
	}

	@Override
	protected float requestedSignDelay() {
		return 1.0f;
	}
}
