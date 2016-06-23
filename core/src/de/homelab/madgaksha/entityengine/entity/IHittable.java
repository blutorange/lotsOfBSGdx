package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;

/**
 * For entites that can get hit by stuff and react in some way.
 * 
 * @author madgaksha
 *
 */
public interface IHittable {
	/**
	 * 
	 * @param me
	 *            Entity that is being touched (ie. the entity with the
	 *            {@link ReceiveTouchComponent}.
	 * @param you
	 *            Entity that touched me.
	 */
	public void hitByBullet(Entity me, Entity you);
}
