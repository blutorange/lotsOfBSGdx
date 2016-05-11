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
public class PositionComponent implements Component, Poolable {
	private final static float DEFAULT_X = 0.0f;
	private final static float DEFAULT_Y = 0.0f;
	private final static float DEFAULT_Z = 0.0f;
	
	public float x = DEFAULT_X;
	public float y = DEFAULT_Y;
	public float z = DEFAULT_Z;
	
	public PositionComponent(){}
	public PositionComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public PositionComponent(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void reset() {
		x = DEFAULT_X;
		y = DEFAULT_Y;
		z = DEFAULT_Z;
	}
	
}
