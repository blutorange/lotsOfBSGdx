package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the position of an object in world space.
 * 
 * R = (R_x, R_y, R_z).
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class TrajectoryComponent implements Component, Poolable {
	private final static float DEFAULT_ANGLE_TO_VELOCITY = 80.0f;
	private final static float DEFAULT_FORCE_COEFFICIENT = 1.0f;
	
	public float angleToVelocity = DEFAULT_ANGLE_TO_VELOCITY;
	public float forceCoefficient = DEFAULT_FORCE_COEFFICIENT;
	
	public TrajectoryComponent() {
	}

	public TrajectoryComponent(float a) {
		this.angleToVelocity = a;
	}
	public TrajectoryComponent(float a, float c) {
		this.forceCoefficient = c;
		this.angleToVelocity = a;
	}
	

	@Override
	public void reset() {
		angleToVelocity = DEFAULT_ANGLE_TO_VELOCITY;
		forceCoefficient = DEFAULT_FORCE_COEFFICIENT;
	}

}
