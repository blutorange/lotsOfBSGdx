package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

/**
 * For entities that can die.
 * 
 * @author madgaksha
 *
 */
public interface IMortal {
	public void kill(Entity e);
}
