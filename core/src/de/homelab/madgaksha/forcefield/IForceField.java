package de.homelab.madgaksha.forcefield;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public interface IForceField {
	public Vector2 apply(Entity e);
}
