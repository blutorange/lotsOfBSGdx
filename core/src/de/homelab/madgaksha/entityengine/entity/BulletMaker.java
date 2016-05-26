package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.resourcecache.IResource;

public class BulletMaker extends EntityMaker {

	// Singleton
	private static class SingletonHolder {
		private static final BulletMaker INSTANCE = new BulletMaker();
	}
	public static BulletMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}
	private BulletMaker() {
		super();
	}
	
	public static Entity makeEntity(BulletShapeMaker bulletShape, BulletTrajectoryMaker bulletTrajectory) {
		Entity entity = gameEntityEngine.createEntity();
		getInstance().setup(entity, bulletShape, bulletTrajectory);
		return entity;
	}
	
	/**
	 * Adds the appropriate components to the entity so that it can be used as a bullet.
	 * @param e Entity to setup.
	 * @param bulletShape The bullet's shape.
	 * @param bulletTrajectory The bullet's trajectory.
	 */
	public void setup(Entity e, BulletShapeMaker bulletShape, BulletTrajectoryMaker bulletTrajectory) {
		super.setup(e);
			
		bulletShape.setup(e);
		bulletTrajectory.setup(e);
	}
	
	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
}
