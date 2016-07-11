package de.homelab.madgaksha.lotsofbs.field;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;

public interface IVelocityField {
	public void apply(Entity e, VelocityComponent vc);
}
