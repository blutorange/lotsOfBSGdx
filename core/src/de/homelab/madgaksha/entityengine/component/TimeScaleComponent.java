package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TimeScaleComponent implements Component, Poolable {
	private final static float DEFAULT_TIME_SCALING_FACTOR = 1.0f;
	private final static boolean DEFAULT_SCALE_DISABLED = false;

	public float timeScalingFactor = DEFAULT_TIME_SCALING_FACTOR;
	public boolean scaleDisabled = DEFAULT_SCALE_DISABLED;

	public TimeScaleComponent() {
	}

	/**
	 * Scales time by the given factor, slowing down or speeding up motion.
	 * 
	 * @param tsf
	 *            Time scaling factor. 1.0 is no change.
	 */
	public TimeScaleComponent(float tsf) {
		timeScalingFactor = tsf;
	}

	@Override
	public void reset() {
		timeScalingFactor = DEFAULT_TIME_SCALING_FACTOR;
		scaleDisabled = DEFAULT_SCALE_DISABLED;
	}
}
