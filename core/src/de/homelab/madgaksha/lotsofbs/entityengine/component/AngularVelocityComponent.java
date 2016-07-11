package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents angular velocity. Speed in degrees/second.
 * 
 * @author mad_gaksha
 */
public class AngularVelocityComponent implements Component, Poolable {
	private final static float DEFAULT_SPEED = 0.0f;

	public float speed = 0.0f;

	public AngularVelocityComponent() {
	}

	public AngularVelocityComponent(float speed) {
		setup(speed);
	}

	public void setup(float speed) {
		this.speed = speed;
	}

	@Override
	public void reset() {
		speed = DEFAULT_SPEED;
	}
}
