package de.homelab.madgaksha.entityengine.entity;

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
	
	@Override
	public void setup(Entity e) {
		super.setup(e);
		// TODO Auto-generated method stub
	}
	
	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
}
