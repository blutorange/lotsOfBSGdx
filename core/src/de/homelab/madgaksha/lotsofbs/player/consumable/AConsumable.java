package de.homelab.madgaksha.lotsofbs.player.consumable;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ConeDistributionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.ItemMaker;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.APlayer;
import de.homelab.madgaksha.lotsofbs.player.IMapItem;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

public abstract class AConsumable implements IMapItem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AConsumable.class);

	private EConsumable type;
	private ESound soundOnAcquire;
	
	void setType(EConsumable type) throws IllegalStateException {
		if (this.type != null) throw new IllegalStateException();
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

	@Override
	public void setup(Entity e, MapProperties props) {
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
		if (cdc == null) return;
		
		SoundPlayer.getInstance().play(soundOnAcquire);
		
		// Setup new item entity.
		final Entity item = gameEntityEngine.createEntity();
		ItemMaker.getInstance().setupCirclingItem(item, this, cdc, pc);
		
		// Add new entity to engine.
		gameEntityEngine.addEntity(item);
	}
}