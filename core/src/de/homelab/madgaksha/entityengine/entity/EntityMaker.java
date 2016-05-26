package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public abstract class EntityMaker {
	protected EntityMaker() {
		// load to ram
		IResource<? extends Enum<?>, ?>[] resourceList = requestedResources();
		if (resourceList != null)
			for (IResource<? extends Enum<?>, ?> resource : requestedResources())
				ResourceCache.loadToRam(resource);
	}
	
	public void setup(Entity e) {
		
	}
	
	protected abstract IResource<? extends Enum<?>, ?>[] requestedResources();
}
