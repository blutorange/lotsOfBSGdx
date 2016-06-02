package de.homelab.madgaksha.grantstrategy;

import com.badlogic.gdx.math.Vector2;

/**
 * Sets current value to the should-value exponentially. Let x_is be the current
 * value. Let x_should be the target value. <br>
 * <br>
 * This strategy sets the current value as follows: <br>
 * <br>
 * <code>x_is += (x_should-x_is)*reductionFactor*(deltaTime/targetTime)</code>
 * <br>
 * <br>
 * The reductionFactor should be bigger than 0 and not larger than 1. <br>
 * <br>
 * A reductionFactor of 1 sets the current value to the target value within one
 * target time interval.
 * 
 * @author madgaksha
 */
public class ExponentialGrantStrategy implements IGrantStrategy {
	private final static float DEFAULT_TARGET_TIME = 0.033f;
	private final static float DEFAULT_COMBINED_FACTOR = 1.0f / DEFAULT_TARGET_TIME;
		
	private float combinedFactor = DEFAULT_COMBINED_FACTOR;
	private final Vector2 v = new Vector2();
	
	public ExponentialGrantStrategy() {
		combinedFactor = DEFAULT_COMBINED_FACTOR;
	}

	public ExponentialGrantStrategy(float reductionFactor) {
		this.combinedFactor = reductionFactor / DEFAULT_TARGET_TIME;
	}	
	
	public ExponentialGrantStrategy(float reductionFactor, float targetTime) {
		this.combinedFactor = reductionFactor / targetTime;
	}

	@Override
	public float compromise(float is, float should, float deltaTime) {
		final float reductionFactor = combinedFactor * deltaTime;
		return is + (should - is) * (reductionFactor > 1.0f ? 1.0f : reductionFactor);
	}

	@Override
	public Vector2 compromise2D(float isX, float isY, float shouldX, float shouldY, float deltaTime) {
		final float reductionFactor = combinedFactor * deltaTime;
		if (reductionFactor >= 1.0f) return v.set(shouldX,shouldY);
		v.set(shouldX-isX,shouldY-isY);
		v.x = v.x * reductionFactor + isX;
		v.y = v.y * reductionFactor + isY;
		return v;
	}
}
