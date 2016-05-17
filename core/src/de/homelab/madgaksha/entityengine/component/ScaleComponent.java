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
public class ScaleComponent implements Component, Poolable {
	private final static float DEFAULT_SCALE_X = 1.0f;
	private final static float DEFAULT_SCALE_Y = 1.0f;
	
	public float scaleX = DEFAULT_SCALE_X;
	public float scaleY = DEFAULT_SCALE_Y;
	
	
	public ScaleComponent() {
	}

	public ScaleComponent(float scale) {
		this(scale,scale);
	}
	public ScaleComponent(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	@Override
	public void reset() {
		scaleX = DEFAULT_SCALE_X;
		scaleY = DEFAULT_SCALE_Y;
	}

}
