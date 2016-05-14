package de.homelab.madgaksha.grantstrategy;

/**
 * Sets the current value to the target value.
 * 
 * @author madgaksha
 */
public class ImmediateGrantStrategy implements IGrantStrategy {
	public ImmediateGrantStrategy() {
	}
	@Override
	public float compromise(float is, float should, float deltaTime) {
		return should;
	}
}
