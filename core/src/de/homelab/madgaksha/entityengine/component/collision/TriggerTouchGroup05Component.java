package de.homelab.madgaksha.entityengine.component.collision;

import de.homelab.madgaksha.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.entityengine.entity.ITrigger;
import de.homelab.madgaksha.entityengine.entitysystem.CollisionSystem;

/**
 * Group for the collision system. 
 * @see TriggerTouchComponent
 * @see CollisionSystem
 * @author mad_gaksha
 */
public class TriggerTouchGroup05Component extends TriggerTouchComponent {
	public TriggerTouchGroup05Component(){}
	public TriggerTouchGroup05Component(ITrigger t) {
		super(t);
	}
}
