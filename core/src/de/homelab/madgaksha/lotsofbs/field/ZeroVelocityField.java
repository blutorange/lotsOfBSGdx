package de.homelab.madgaksha.lotsofbs.field;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;

public class ZeroVelocityField implements IVelocityField {
	public ZeroVelocityField() {
	}

	@Override
	public void apply(Entity e, VelocityComponent vc) {
		vc.x = 0.0f;
		vc.y = 0.0f;
	}
}
