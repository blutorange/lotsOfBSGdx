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
	private final static float DEFAULT_ORIGINAL_SCALE = 1.0f;
	
	public float scaleX = DEFAULT_SCALE_X;
	public float scaleY = DEFAULT_SCALE_Y;
	public float originalScale = DEFAULT_ORIGINAL_SCALE;
	
	public ScaleComponent() {
	}

	public ScaleComponent(float scale) {
		setup(scale);
	}
	public ScaleComponent(float scaleX, float scaleY) {
		setup(scaleX, scaleY);
	}
	public ScaleComponent(float scaleX, float scaleY, float originalScale) {
		setup(scaleX, scaleY, originalScale);
	}
	
	public void setup(float scale) {
		setup(scale, scale, DEFAULT_ORIGINAL_SCALE);
	}
	public void setup(float scaleX, float scaleY) {
		setup(scaleX, scaleY, DEFAULT_ORIGINAL_SCALE);
	}
	public void setup(float scaleX, float scaleY, float originalScale) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.originalScale = originalScale;
	}
	
	@Override
	public void reset() {
		scaleX = DEFAULT_SCALE_X;
		scaleY = DEFAULT_SCALE_Y;
	}

}
