package de.homelab.madgaksha.lotsofbs.player.consumable;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.PainBarUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EModel;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;

public class ConsumableMidHeal extends AConsumable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ConsumableMidHeal.class);

	private final static long HEAL_RATIO_NUM = 40L;
	private final static long HEAL_RATIO_DEN = 100L;
	
	@Override
	public EModel getModel() {
		return EModel.ITEM_WEAPON_BASIC;
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource<?, ?>[] {
			EModel.ITEM_WEAPON_BASIC,
			ESound.HEAL1,
		};
	}

	@Override
	protected ESound requestedSoundOnAcquire() {
		return ESound.ACQUIRE_WEAPON;
	}

	@Override
	public void usedItem() {
		PainPointsComponent ppc = Mapper.painPointsComponent.get(playerHitCircleEntity);
		DamageQueueComponent dqc = Mapper.damageQueueComponent.get(playerHitCircleEntity);
		long heal =  (ppc.maxPainPoints / HEAL_RATIO_DEN) * HEAL_RATIO_NUM;
		PainBarUtils.queueHeal(heal, dqc);
	}

	@Override
	public boolean isConsumableNow() {
		return !PainBarUtils.isEntityFullyHealed(playerHitCircleEntity);
	}

	@Override
	public ESound getSoundOnConsumption() {
		return ESound.HEAL1;
	}
	
	@Override
	public float getDelayOnConsumption() {
		return 3.0f;
	}
}
