package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.IBehaving;

/**
 * Contains the callback {@link Entity} implementing {@link IBehaving}.
 * 
 * @author madgaksha
 *
 */
public class ColorFlashEffectComponent implements Component, Poolable {

	private final static float DEFAULT_DURATION = 1.0f;
	private final static Color DEFAULT_COLOR_START = Color.WHITE;
	private final static Color DEFAULT_COLOR_END = Color.WHITE;
	private final static Interpolation DEFAULT_INTERPOLATOR = Interpolation.linear;
	private final static int DEFAULT_REPETITIONS = 1;

	private final static float DEFAULT_TOTAL_TIME = 0.0f;
	private final static int DEFAULT_TOTAL_LOOPS = 0;
	private final static boolean DEFAULT_DIRECTION_FORWARD = true;

	public Color colorStart = DEFAULT_COLOR_START;
	public Color colorEnd = DEFAULT_COLOR_END;
	public Interpolation interpolator = DEFAULT_INTERPOLATOR;
	public float duration = DEFAULT_DURATION;
	public float repetitions = DEFAULT_REPETITIONS;

	// Used internally.
	public float totalTime = DEFAULT_TOTAL_TIME;
	public boolean directionForward = DEFAULT_DIRECTION_FORWARD;
	public int totalLoops = DEFAULT_TOTAL_LOOPS;

	public ColorFlashEffectComponent() {
	}

	public ColorFlashEffectComponent(Color start, Color end, Interpolation interpolator) {
		setup(start, end, interpolator);
	}

	public ColorFlashEffectComponent(Color start, Color end, Interpolation interpolator, float duration) {
		setup(start, end, interpolator, duration);
	}

	public void setup(Color start, Color end, Interpolation interpolator) {
		setup(start, end, interpolator, DEFAULT_DURATION);
	}

	public void setup(Color start, Color end, Interpolation interpolator, float duration) {
		setup(start, end, interpolator, duration, DEFAULT_REPETITIONS);
	}

	public void setup(Color start, Color end, Interpolation interpolator, float duration, int repetitions) {
		this.colorStart = start;
		this.colorEnd = end;
		this.interpolator = interpolator;
		this.duration = duration;
		this.repetitions = repetitions;
	}

	@Override
	public void reset() {
		colorStart = DEFAULT_COLOR_START;
		colorEnd = DEFAULT_COLOR_END;
		interpolator = DEFAULT_INTERPOLATOR;
		duration = DEFAULT_DURATION;
		repetitions = DEFAULT_REPETITIONS;

		totalTime = DEFAULT_TOTAL_TIME;
		totalLoops = DEFAULT_TOTAL_LOOPS;
		directionForward = DEFAULT_DIRECTION_FORWARD;
	}
}
