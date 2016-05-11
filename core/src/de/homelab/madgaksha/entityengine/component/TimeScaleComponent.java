package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TimeScaleComponent implements Component, Poolable{
	private final static float DEFAULT_TIME_SCALING_FACTOR = 1.0f;
	
	public float timeScalingFactor = DEFAULT_TIME_SCALING_FACTOR;

	@Override
	public void reset() {
		timeScalingFactor = DEFAULT_TIME_SCALING_FACTOR;
	}
}
