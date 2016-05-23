package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the velocity of an object in world coordinates.
 * 
 * V = (V_x, V_y, V_z).
 * 
 * Unit: m/s.
 * 
 * @author mad_gaksha
 */
public class AngularVelocityComponent implements Component, Poolable {
	private final static float DEFAULT_SPEED = 0.0f;

	public float speed = 0.0f;

	public AngularVelocityComponent() {
	}
	public AngularVelocityComponent(float speed) {
		this.speed = speed;
	}
	
	@Override
	public void reset() {
		speed = DEFAULT_SPEED;
	}
}
