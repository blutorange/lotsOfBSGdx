package de.homelab.madgaksha.grantstrategy;

/**
 * An interface for smoothly setting value to a target value.
 * 
 * @author madgaksha
 *
 */
public interface IGrantStrategy {
	/**
	 * Tries and sets the current value as close as possible to the target
	 * value.
	 * 
	 * @param is
	 *            The current value.
	 * @param should
	 *            The target value.
	 * @return The value as close to the target value.
	 */
	public float compromise(float is, float should, float deltaTime);
}
