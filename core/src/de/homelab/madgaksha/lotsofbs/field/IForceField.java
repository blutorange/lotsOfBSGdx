package de.homelab.madgaksha.lotsofbs.field;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ForceComponent;

public interface IForceField {
	public void apply(Entity e, ForceComponent fc);
}
