package de.homelab.madgaksha.grantstrategy;

import com.badlogic.gdx.math.Vector2;

/**
 * Sets the current value to the target value.
 * 
 * @author madgaksha
 */
public class ImmediateGrantStrategy implements IGrantStrategy {
	private Vector2 v = new Vector2();

	public ImmediateGrantStrategy() {
	}

	@Override
	public float compromise(float is, float should, float deltaTime) {
		return should;
	}

	@Override
	public Vector2 compromise2D(float isX, float isY, float shouldX, float shouldY, float deltaTime) {
		return v.set(shouldX, shouldY);
	}
}
