package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

public interface ITrigger {
	public void callbackStartup();
	public void callbackTouch(Entity e);
	public void callbackScreen();
}
