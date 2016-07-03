package de.homelab.madgaksha.field;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.component.VelocityComponent;

public interface IVelocityField {
	public void apply(Entity e, VelocityComponent vc);
}
