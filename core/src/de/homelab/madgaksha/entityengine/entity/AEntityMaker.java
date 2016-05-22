package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public abstract class AEntityMaker extends Entity {
	public AEntityMaker() {
		super();
		// load to ram
		IResource<? extends Enum<?>, ?>[] resourceList = requestedResources();
		if (resourceList != null)
			for (IResource<? extends Enum<?>, ?> resource : requestedResources())
				ResourceCache.loadToRam(resource);
	}
	
	protected abstract IResource<? extends Enum<?>, ?>[] requestedResources();
	public abstract void reinitializeEntity();
}
