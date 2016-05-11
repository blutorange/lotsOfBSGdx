package de.homelab.madgaksha.entitysystem.component;

/**
 * Sets current value to the should-value exponentially.
 * Let x_is be the current value.
 * Let x_should be the target value.
 * 
 * This strategy sets the current value as follow:
 *   x_is += (x_should-x_is)*reductionFactor
 *   
 * The reductionFactor should be bigger than 0 and not
 * larger than 1.
 * 
 * A reductionFactor of 1 sets the current value to the
 * target value immediately.
 */
public class ExponentialGrantComponent {
	public float reductionFactor = 1.0f;
}
