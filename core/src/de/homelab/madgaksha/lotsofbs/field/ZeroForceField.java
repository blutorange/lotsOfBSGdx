package de.homelab.madgaksha.lotsofbs.field;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ForceComponent;

public class ZeroForceField implements IForceField {
	public ZeroForceField() {
	}

	@Override
	public void apply(Entity e, ForceComponent vc) {
		vc.x = 0.0f;
		vc.y = 0.0f;
	}
}
