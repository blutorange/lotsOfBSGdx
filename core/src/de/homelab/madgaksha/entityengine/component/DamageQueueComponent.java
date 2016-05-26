package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.CallbackMaker;

/**
 * Contains information for {@link CallbackMaker} entities.
 * @author madgaksha
 *
 */
public class DamageQueueComponent implements Component, Poolable {
	private static final long DEFAULT_QUEUED_DAMAGE = 0L;
	
	public long queuedDamage = DEFAULT_QUEUED_DAMAGE;
	
	public DamageQueueComponent(){
	}
	
	@Override
	public void reset() {
		queuedDamage = DEFAULT_QUEUED_DAMAGE;
	}
}
