package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Frequency pre-multiplied by 2*pi. The variable {@link #frequency} corresponds
 * to an actual frequency of 2*pi/{@link #frequency}.
 * 
 * @author mad_gaksha
 */
public class HoverEffectComponent implements Component, Poolable {
	private final static float DEFAULT_FREQUENCY = 2.0f * MathUtils.PI / 1.0f;
	private final static float DEFAULT_AMPLITUDE = 8.0f;

	public float frequency = DEFAULT_FREQUENCY;
	public float amplitude = DEFAULT_AMPLITUDE;

	public HoverEffectComponent() {
	}

	public HoverEffectComponent(float amplitude, float frequency) {
		setup(amplitude, frequency);
	}

	public void setup(float amplitude, float frequency) {
		this.frequency = 2.0f * MathUtils.PI / frequency;
		this.amplitude = amplitude;
	}

	@Override
	public void reset() {
		frequency = DEFAULT_FREQUENCY;
		amplitude = DEFAULT_AMPLITUDE;
	}

}
