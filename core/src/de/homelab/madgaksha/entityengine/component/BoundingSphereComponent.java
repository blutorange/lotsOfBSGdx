package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the dimensions of an object in world space.
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class BoundingSphereComponent implements Component, Poolable {
	private final static float DEFAULT_RADIUS = 1.0f;

	/**
	 * The offset (difference) between the center of the bounding sphere and the
	 * center of the image or position component.
	 */
	private final static float DEFAULT_CENTER_X = 0.0f;
	/**
	 * The offset (difference) between the center of the bounding sphere and the
	 * center of the image or position component.
	 */
	private final static float DEFAULT_CENTER_Y = 0.0f;
	/**
	 * The offset (difference) between the center of the bounding sphere and the
	 * center of the image or position component.
	 */
	private final static float DEFAULT_CENTER_Z = 0.0f;

	public float radius = DEFAULT_RADIUS;
	public float centerX = DEFAULT_CENTER_X;
	public float centerY = DEFAULT_CENTER_Y;
	public float centerZ = DEFAULT_CENTER_Z;

	public BoundingSphereComponent() {
	}

	public BoundingSphereComponent(Circle c) {
		setup(c);
	}

	public BoundingSphereComponent(float r) {
		setup(r);
	}

	public void setup(Circle c) {
		this.radius = c.radius;
		this.centerX = c.x;
		this.centerY = c.y;
	}

	public void setup(float r) {
		this.radius = r;
	}

	@Override
	public void reset() {
		radius = DEFAULT_RADIUS;
		centerX = DEFAULT_CENTER_X;
		centerX = DEFAULT_CENTER_Y;
		centerX = DEFAULT_CENTER_Z;
	}

}
