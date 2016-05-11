package de.homelab.madgaksha.grantstrategy;

/**
 * Sets current value to the should-value exponentially.
 * Let x_is be the current value.
 * Let x_should be the target value.
 * <br><br>
 * This strategy sets the current value as follows:
 * <br><br>
 *   <code>x_is += (x_should-x_is)*reductionFactor</code>
 * <br><br> 
 * The reductionFactor should be bigger than 0 and not
 * larger than 1.
 * <br><br>
 * A reductionFactor of 1 sets the current value to the
 * target value immediately.
 * 
 * @author madgaksha
 */
public class ExponentialGrantStrategy implements IGrantStrategy {
	private final static float DEFAULT_REDUCTION_FACTOR = 1.0f;
	
	private float reductionFactor = DEFAULT_REDUCTION_FACTOR;

	public ExponentialGrantStrategy() {
		reductionFactor = DEFAULT_REDUCTION_FACTOR;
	}
	public ExponentialGrantStrategy(float reductionFactor) {
		this.reductionFactor = reductionFactor;
	}

	@Override
	public float compromise(float is, float should) {
		return is + (should-is)*reductionFactor;
	}
}
