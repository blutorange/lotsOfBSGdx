package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import com.badlogic.ashley.core.Entity;

public interface ITimedCallback {
	public void run(Entity entity, Object data);
}
