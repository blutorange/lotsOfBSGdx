package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

public abstract class AEntityMaker extends Entity {
	public AEntityMaker() {
		super();
	}
	
	public abstract void reinitializeEntity();
}
