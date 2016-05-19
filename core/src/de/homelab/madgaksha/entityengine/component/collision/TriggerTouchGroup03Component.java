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
public class TriggerTouchGroup03Component extends TriggerTouchComponent {
	public TriggerTouchGroup03Component(){}
	public TriggerTouchGroup03Component(ITrigger t) {
		super(t);
	}
}
