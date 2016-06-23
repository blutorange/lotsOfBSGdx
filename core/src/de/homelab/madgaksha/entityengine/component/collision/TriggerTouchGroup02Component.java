package de.homelab.madgaksha.entityengine.component.collision;

import de.homelab.madgaksha.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.entityengine.entity.ITrigger;
import de.homelab.madgaksha.entityengine.entitysystem.CollisionSystem;

/**
 * Group for the collision system.
 * 
 * @see TriggerTouchComponent
 * @see CollisionSystem
 * @author mad_gaksha
 */
public class TriggerTouchGroup02Component extends TriggerTouchComponent {
	public TriggerTouchGroup02Component() {
	}

	public TriggerTouchGroup02Component(ITrigger t) {
		super(t);
	}
}
