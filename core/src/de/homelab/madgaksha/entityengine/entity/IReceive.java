package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

public interface IReceive {
	public void callbackTouched(Entity e);
}
