package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the direction an object is looking at. The sprite may
 * change depending on the direction.
 * 
 * Unit: Degree
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
 * @see RotationComponent
 * @author madgaksha
 *
 */
public class DirectionComponent implements Component, Poolable {
	public float degree = 0.0f;

	@Override
	public void reset() {
		degree = 0.0f;		
	}
}
