package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the direction an object is looking at. The sprite may change
 * depending on the direction.
 * 
 * Unit: Degree
 * 
 * <pre>
 * 
 *          90°
 *           |
 *           |
 *           |
 *   0°------+------180°
 *           |
 *           |
 *           |
 *          270°
 * 
 * </pre>
 * 
 * @see RotationComponent
 * @author madgaksha
 *
 */
public class DirectionComponent implements Component, Poolable {
	private final static float DEFAULT_DEGREE = 0.0f;
	public float degree = DEFAULT_DEGREE;

	public DirectionComponent() {
	}

	public DirectionComponent(float d) {
		setup(d);
	}

	public void setup(float d) {
		degree = d;
	}

	@Override
	public void reset() {
		degree = DEFAULT_DEGREE;
	}
}
