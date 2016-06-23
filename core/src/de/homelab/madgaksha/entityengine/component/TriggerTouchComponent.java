package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup01Component;
import de.homelab.madgaksha.entityengine.entity.ITrigger;
import de.homelab.madgaksha.entityengine.entitysystem.CollisionSystem;

/**
 * A component for objects that need to call some internal java function when
 * touched by some object.
 * 
 * I might do collision detection this way or similarly.
 * 
 * This is the base class. The {@link CollisionSystem} only checks for
 * collisions between entities with the same collision group. Use
 * {@link TriggerTouchGroup01Component} etc.
 * 
 * @author mad_gaksha
 */
public abstract class TriggerTouchComponent implements Component, Poolable {

	private final static ITrigger DEFAULT_TRIGGER_ACCEPTING_OBJECT = new ITrigger() {
		@Override
		public void callbackTrigger(Entity e, ETrigger t) {
		}
	};

	public ITrigger triggerAcceptingObject = DEFAULT_TRIGGER_ACCEPTING_OBJECT;

	public TriggerTouchComponent() {
	}

	public TriggerTouchComponent(ITrigger t) {
		this.triggerAcceptingObject = t;
	}

	@Override
	public void reset() {
		triggerAcceptingObject = DEFAULT_TRIGGER_ACCEPTING_OBJECT;
	}
}
