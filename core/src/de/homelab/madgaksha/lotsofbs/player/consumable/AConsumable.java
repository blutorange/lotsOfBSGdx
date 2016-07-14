package de.homelab.madgaksha.lotsofbs.player.consumable;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ConeDistributionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IEntityCallback;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IEntityFeedback;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.ItemMaker;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.AItem;
import de.homelab.madgaksha.lotsofbs.player.APlayer;
import de.homelab.madgaksha.lotsofbs.player.IConsumableMapItem;
import de.homelab.madgaksha.lotsofbs.player.IMapItem;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

public abstract class AConsumable extends AItem implements IConsumableMapItem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AConsumable.class);

	private EConsumable type;
	private ESound soundOnAcquire;

	void setType(EConsumable type) throws IllegalStateException {
		if (this.type != null)
			throw new IllegalStateException();
		this.type = type;
	}

	public EConsumable getType() {
		return type;
	}

	@Override
	public boolean initialize() {
		soundOnAcquire = requestedSoundOnAcquire();
		return ResourceCache.loadToRam(requestedRequiredResources());

	}

	protected abstract ESound requestedSoundOnAcquire();

	/**
	 * May be overridden by specific items for further custom initialization.
	 * Remember to call super().
	 */
	@Override
	public void setup(Entity entity, MapProperties props, ComponentQueueComponent queueForAcquisition) {
		ComponentQueueComponent cqcCollect = Mapper.componentQueueComponent.get(entity);
		// Check whether player can hold any more items when item activates
		// or hits player.
		cqcCollect.applicable = APPLICABLE_WHEN_FREE_ITEM_SLOTS;
		queueForAcquisition.applicable = APPLICABLE_WHEN_FREE_ITEM_SLOTS;
		// Reset items currently moving towards the player when he cannot hold
		// more items any longer.
		TimedCallbackComponent tcc = gameEntityEngine.createComponent(TimedCallbackComponent.class);
		tcc.setup(CHECK_FREE_SLOTS_WHILE_ITEM_APPROACHING, 0.5f,TimedCallbackComponent.UNLIMITED_NUMBER_OF_CALLBACKS);
		cqcCollect.add.add(tcc);
	}

	/**
	 * Can be overridden for other values.
	 * 
	 * @return The default axis of rotation around which the item model rotates.
	 */
	@Override
	public Vector3 getDefaultAxisOfRotation() {
		return new Vector3(1.0f, 1.0f, 0.0f);
	}

	/**
	 * Can be overridden for other values.
	 * 
	 * @return The angular velocity at which the item model rotates.
	 */
	@Override
	public float getDefaultAngularVelocity() {
		return 45.0f;
	}

	@Override
	public boolean isSupportedByPlayer(APlayer player) {
		return type == null ? true : player.supportsConsumable(type);
	}

	/**
	 * Can be overridden for other values.
	 * 
	 * @see IMapItem#getActivationAreaScaleFactor()
	 */
	@Override
	public float getActivationAreaScaleFactor() {
		return 3.5f;
	}

	@Override
	public void gotItem() {
		final ConeDistributionComponent cdc = Mapper.coneDistributionComponent.get(playerHitCircleEntity);
		final PositionComponent pc = Mapper.positionComponent.get(playerHitCircleEntity);
		
		SoundPlayer.getInstance().play(soundOnAcquire);

		// Setup new item entity.
		final Entity item = gameEntityEngine.createEntity();
		ItemMaker.getInstance().setupCirclingItem(item, this, cdc, pc);

		// Add new entity to engine.
		gameEntityEngine.addEntity(item);
	}

	private final static IEntityFeedback APPLICABLE_WHEN_FREE_ITEM_SLOTS = new IEntityFeedback() {
		@Override
		public boolean check(Entity entity, Object data) {
			ConeDistributionComponent cdc = Mapper.coneDistributionComponent.get(playerHitCircleEntity);
			return cdc != null && cdc.distributionPoints.size() < player.getMaximumHoldableItems();
		}
	};

	IEntityCallback CHECK_FREE_SLOTS_WHILE_ITEM_APPROACHING = new IEntityCallback() {
		@Override
		public void run(Entity entity, Object data) {
			ConeDistributionComponent cdc = Mapper.coneDistributionComponent.get(playerHitCircleEntity);
			if (cdc != null && cdc.distributionPoints.size() >= player.getMaximumHoldableItems()) {
				entity.remove(TimedCallbackComponent.class);
				ItemMaker.resetItemToWaitingState(entity);
			}
		}
	};
}