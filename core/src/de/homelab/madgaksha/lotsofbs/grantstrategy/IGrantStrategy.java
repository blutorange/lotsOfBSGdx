package de.homelab.madgaksha.lotsofbs.grantstrategy;

import com.badlogic.gdx.math.Vector2;

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

	public Vector2 compromise2D(float isX, float isY, float shouldX, float shouldY, float deltaTime);
}
