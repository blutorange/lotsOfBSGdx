package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the dimensions of an object in world space.
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class BoundingBoxComponent implements Component, Poolable {
	private final static float DEFAULT_MIN_X = -0.5f;
	private final static float DEFAULT_MIN_Y = -0.5f;
	
	private final static float DEFAULT_MAX_X = 0.5f;
	private final static float DEFAULT_MAX_Y = 0.5f;
	
	public float minX = DEFAULT_MIN_X;
	public float minY = DEFAULT_MIN_Y;
	public float maxX = DEFAULT_MAX_X;
	public float maxY = DEFAULT_MAX_Y;
	
	public BoundingBoxComponent() {
	}

	public BoundingBoxComponent(Rectangle r) {
		setup(r);
	}
	public BoundingBoxComponent(float minX, float minY, float maxX, float maxY) {
		setup(minX,minY,maxX,maxY);
	}

	public void setup(Rectangle r) {
		this.minX = r.x;
		this.minY = r.y;
		this.maxX = r.x + r.width;
		this.maxY = r.y + r.height;
	}
	public void setup(float minX, float minY, float maxX, float maxY) {
		this.minX= minX;
		this.minY= minY;
		this.maxX = maxX;
		this.minY = minY;
	}

	
	@Override
	public void reset() {
		minX = DEFAULT_MIN_X;
		minY = DEFAULT_MIN_Y;
		maxX = DEFAULT_MAX_X;
		maxY = DEFAULT_MAX_Y;
	}

	@Override
	public String toString() {
		return "(" + minX + ", " + minY + ")-(" + maxX + ", " + maxY + ")";
	}
	
}
