package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the dimensions of an object in world space.
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class BoundingSphereComponent implements Component, Poolable {
	private final static float DEFAULT_RADIUS = 0.5f;
	
	public float radius = DEFAULT_RADIUS;	
	
	public BoundingSphereComponent() {
	}

	public BoundingSphereComponent(float r) {
		this.radius = r;
	}

	@Override
	public void reset() {
		radius = DEFAULT_RADIUS;
	}

}
