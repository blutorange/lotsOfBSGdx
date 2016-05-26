package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public abstract class EntityMaker  {
	private final boolean initializedSuccessfully; 
	protected EntityMaker() {
		// load to ram
		IResource<? extends Enum<?>, ?>[] resourceList = requestedResources();
		if (resourceList != null && !ResourceCache.loadToRam(resourceList)) initializedSuccessfully = false;
		else initializedSuccessfully = true;
	}
	
	public void setup(Entity e) {
		
	}
	
	public boolean isInitializedSuccessfully() {
		return initializedSuccessfully;
	}
	
	protected abstract IResource<? extends Enum<?>, ?>[] requestedResources();
}
