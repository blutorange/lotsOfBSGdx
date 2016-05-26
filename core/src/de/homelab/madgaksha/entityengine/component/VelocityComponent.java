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
public class VelocityComponent implements Component, Poolable {
	private final static float DEFAULT_X = 0.0f;
	private final static float DEFAULT_Y = 0.0f;
	private final static float DEFAULT_Z = 0.0f;

	public float x = 0.0f;
	public float y = 0.0f;
	public float z = 0.0f;

	public VelocityComponent() {
	}
	public VelocityComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public VelocityComponent(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setup(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void setup(float x, float y, float z) {
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
	
	@Override
	public String toString(){
		return "VelocityComponent(" + x + "," + y + "," + z + ")";
	}
}
