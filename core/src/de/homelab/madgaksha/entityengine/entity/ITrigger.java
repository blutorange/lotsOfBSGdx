package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.ETrigger;

public interface ITrigger {
	public void callbackTrigger(Entity e, ETrigger t);
}
