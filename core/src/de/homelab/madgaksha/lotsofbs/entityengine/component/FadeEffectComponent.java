package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.IEntityCallback;

/**
 * Frequency pre-multiplied by 2*pi. The variable {@link #frequency} corresponds
 * to an actual frequency of 2*pi/{@link #frequency}.
 * 
 * @author mad_gaksha
 */
public class FadeEffectComponent implements Component, Poolable {
	private final static float DEFAULT_START = 0.0f;
	private final static float DEFAULT_END = 1.0f;
	private final static float DEFAULT_DURATION = 1.0f;
	private final static float DEFAULT_TOTAL_TIME = 0.0f;
	private final static Object DEFAULT_CUSTOM_DATA = null;
	private final static Interpolation DEFAULT_INTERPOLATION = Interpolation.linear;

	public float totalTime = DEFAULT_TOTAL_TIME;
	public float start = DEFAULT_START;
	public float end = DEFAULT_END;
	public float duration = DEFAULT_DURATION;
	public Interpolation interpolation = DEFAULT_INTERPOLATION;
	public IEntityCallback callback = null;
	public Object customData = DEFAULT_CUSTOM_DATA;

	public FadeEffectComponent() {
	}

	public FadeEffectComponent(float duration, float start, float end, Interpolation interpolation) {
		setup(duration, start, end, interpolation);
	}

	public void setup(float duration, float start, float end, Interpolation interpolation) {
		this.duration = duration;
		this.start = start;
		this.end = end;
		this.interpolation = interpolation;
	}

	public void setup(float duration, float start, float end, Interpolation interpolation, IEntityCallback callback) {
		this.duration = duration;
		this.start = start;
		this.end = end;
		this.interpolation = interpolation;
		this.callback = callback;

	}

	@Override
	public void reset() {
		start = DEFAULT_START;
		end = DEFAULT_END;
		duration = DEFAULT_DURATION;
		interpolation = DEFAULT_INTERPOLATION;
		callback = null;
		customData = null;
		totalTime = DEFAULT_TOTAL_TIME;
	}

}
