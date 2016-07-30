package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

public abstract class EntityMaker {
	private final boolean initializedSuccessfully;

	protected EntityMaker() {
		// load to ram
		IResource<? extends Enum<?>, ?>[] resourceList = requestedResources();
		if (resourceList != null && !ResourceCache.loadToRam(resourceList))
			initializedSuccessfully = false;
		else
			initializedSuccessfully = true;
	}

	/**
	 * @param e The entity to be setup. 
	 */
	public void setup(Entity e) {

	}

	public boolean isInitializedSuccessfully() {
		return initializedSuccessfully;
	}

	protected abstract IResource<? extends Enum<?>, ?>[] requestedResources();
}
