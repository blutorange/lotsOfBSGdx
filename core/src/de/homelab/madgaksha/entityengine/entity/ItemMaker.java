package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.ModelComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.IMapItem;
import de.homelab.madgaksha.resourcecache.IResource;

public class ItemMaker extends EntityMaker implements IReceive {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EntityMaker.class);

	// Singleton
	private static class SingletonHolder {
		private static final ItemMaker INSTANCE = new ItemMaker();
	}
	public static ItemMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}	
	private ItemMaker () {
		super();
	}
	
	/**
	 * 
	 * @param entity Entity to setup.
	 * @param shape Shape of the callback entity.
	 * @param trigger Type of trigger for the callback.
	 * @param callback Callback to be called. Its signature must be <code>void myCallback(MapProperties)</code> and it must be declared for a subclass of {@link ALevel}.
	 * @param properties Map properties passed to the callback function.
	 * @param loop How many times the callback should loop (be triggered).
	 * @param interval Interval between loops in seconds.
	 * @return Whether the entity could be setup.
	 */
	public boolean setup(Entity entity, Shape2D shape, IMapItem mapItem, Float angularVelocity, Vector3 axis) {
		
		if (!mapItem.isSupportedByPlayer(player)) {
			LOG.info("player " + player + " does not support weapon " + mapItem);
			return false;
		}
		
		super.setup(entity);
		
		// Apply defaults.
		if (axis == null) axis = mapItem.getDefaultAxisOfRotation();
		if (angularVelocity == null) angularVelocity = mapItem.getDefaultAngularVelocity();
		
		// Check axis is not null vector.
		if (axis.len2() < 0.00001f) {
			axis.x = 1.0f;
			axis.y = 1.0f;
			axis.z = 1.0f;
		}
		
		// Normalize axis to unit length.
		axis.nor();
		
		// Create basic components.
		ReceiveTouchComponent rtc = new ReceiveTouchGroup01Component(this); 
		ModelComponent mc = new ModelComponent(mapItem.getModel());
		BoundingBoxRenderComponent bbrc = new BoundingBoxRenderComponent(mc);
		BoundingBoxCollisionComponent bbcc = new BoundingBoxCollisionComponent(bbrc);
		PositionComponent pc = new PositionComponent(MakerUtils.makePositionAtCenter(shape));
		TemporalComponent tc = new TemporalComponent();
		RotationComponent rc = new RotationComponent(axis);
		AngularVelocityComponent avc = new AngularVelocityComponent(angularVelocity);
		
		// Add components.
		entity
			.add(avc)
			.add(bbrc)
			.add(bbcc)
			.add(mc)
			.add(pc)
			.add(tc)
			.add(rtc)
			.add(rc);
		
		// Proceed with item-specific setup.
		mapItem.setup(entity);
		
		return true;
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
	@Override
	public void callbackTouched(Entity me, Entity you) {
		// TODO Auto-generated method stub
		LOG.debug("touched item");
	}
	
}