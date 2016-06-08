package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Make an entity shake as if there were an earthquake. Applying this to the camera entity results in a nifty screen quake.
 * 
 * Modifies the {@link PositionComponent#offsetX} etc. of an entity's {@link PositionComponent}.
 * @author mad_gaksha
 */
public class QuakeEffectComponent implements Component, Poolable {
	private final static float DEFAULT_AMPLITUDE = 1.0f;
	private final static float DEFAULT_FREQUENCY = 1.0f;
	private final static float DEFAULT_MINUS_T = 1.0f;
	private final static float DEFAULT_MIN_ADVANCE_ANGLE = 45.0f;
	private final static float DEFAULT_MAX_ADVANCE_ANGLE = 315.0f;
	private final static float DEFAULT_MIN_AMPLITUDE_RATIO = 0.5f;
	
	/** Maximum radial translation of the quake. */
	public float amplitude = DEFAULT_AMPLITUDE;
	/** Frequency in Hz at which quakes occur. */
	public float frequency = DEFAULT_FREQUENCY;
	
	public float minAdvanceAngle = DEFAULT_MIN_ADVANCE_ANGLE;
	public float maxAdvanceAngle = DEFAULT_MAX_ADVANCE_ANGLE;
	public float minAmplitudeRatio = DEFAULT_MIN_AMPLITUDE_RATIO;
	
	// Internal use.
	public float minusT = DEFAULT_MINUS_T;
	public float angle = 0.0f;
	
	public QuakeEffectComponent() {
	}

	public QuakeEffectComponent(float amplitude, float frequency) {
		setup(amplitude, frequency);
	}
	public void setup(float amplitude, float frequency)  {
		this.amplitude = amplitude;
		this.frequency = frequency;
	}
	
	@Override
	public void reset() {
		minusT = DEFAULT_MINUS_T;
		amplitude = DEFAULT_AMPLITUDE;
		frequency = DEFAULT_FREQUENCY;
		minAdvanceAngle = DEFAULT_MIN_ADVANCE_ANGLE;
		maxAdvanceAngle = DEFAULT_MAX_ADVANCE_ANGLE;
		minAmplitudeRatio = DEFAULT_MIN_AMPLITUDE_RATIO;
	}

}
