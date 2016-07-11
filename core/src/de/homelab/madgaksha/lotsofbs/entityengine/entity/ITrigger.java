package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;

public interface ITrigger {
	public void callbackTrigger(Entity e, ETrigger t);
}
