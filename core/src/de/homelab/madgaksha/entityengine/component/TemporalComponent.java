package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Component every entity needs to have for it to be processed by an entity system
 * requiring access to deltaTime.
 * <br>
 * The deltaTime passed in by ashley to the update function must not be used.
 * <br>
 * deltaTime must be accessed via <code>Mapper.temporalComponent.get(entity).deltaTime</code>.
 * 
 * @author madgaksha
 *
 */
public class TemporalComponent implements Component, Poolable {
	private final static float DEFAULT_DELTA_TIME= 0.033f;
	private final static float DEFAULT_TOTAL_TIME= 0.0f;

	public float deltaTime = DEFAULT_DELTA_TIME;
	public float totalTime = DEFAULT_TOTAL_TIME;

	/**
	 * Scales time by the given factor, slowing down or speeding up motion.
	 * @param tsf Time scaling factor. 1.0 is no change.
	 */
	public TemporalComponent() {
	}
	
	@Override
	public void reset() {
		deltaTime = DEFAULT_DELTA_TIME;
		totalTime = DEFAULT_TOTAL_TIME;
	}
}
