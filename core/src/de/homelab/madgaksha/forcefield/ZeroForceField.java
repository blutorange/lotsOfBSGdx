package de.homelab.madgaksha.forcefield;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class ZeroForceField implements IForceField {
	private final static Vector2 v = new Vector2();	
	public ZeroForceField() {
	}
	@Override
	public Vector2 apply(Entity e) {
		v.set(0f, 0f);
		return v;
	}
}
