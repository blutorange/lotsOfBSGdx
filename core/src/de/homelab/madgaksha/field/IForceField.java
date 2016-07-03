package de.homelab.madgaksha.field;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.component.ForceComponent;

public interface IForceField {
	public void apply(Entity e, ForceComponent fc);
}
