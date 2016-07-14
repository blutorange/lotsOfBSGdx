package de.homelab.madgaksha.lotsofbs.player.consumable;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EModel;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;

public class ConsumableLowHeal extends AConsumable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ConsumableLowHeal.class);

	@Override
	public EModel getModel() {
		return EModel.ITEM_TOKUGI_OUKAMUSOUGEKI;
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource<?, ?>[] {
			EModel.ITEM_TOKUGI_OUKAMUSOUGEKI,
		};
	}

	@Override
	protected ESound requestedSoundOnAcquire() {
		return ESound.ACQUIRE_WEAPON;
	}
}
