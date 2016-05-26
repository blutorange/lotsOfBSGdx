package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;

public interface IReceive {
	/**
	 * 
	 * @param me Entity that is being touched (ie. the entity with the {@link ReceiveTouchComponent}.
	 * @param you Entity that touched me.
	 */
	public void callbackTouched(Entity me, Entity you);
}
