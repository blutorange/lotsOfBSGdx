package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

/** 
 * For entities that can alter their position, speed, sprite etc.
 * @author madgaksha
 *
 */
public interface IBehaving {
	public boolean behave(Entity e);
}
