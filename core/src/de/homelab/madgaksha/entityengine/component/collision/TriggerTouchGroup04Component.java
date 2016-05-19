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
public class TriggerTouchGroup04Component extends TriggerTouchComponent {
	public TriggerTouchGroup04Component(){}
	public TriggerTouchGroup04Component(ITrigger t) {
		super(t);
	}
}
