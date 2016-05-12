package de.homelab.madgaksha.grantstrategy;

/**
 * Let the component should-value be x_should. Let the component is-value be
 * x_is. Let the time step be dt.
 * 
 * Set x = x_is - x_should
 *
 * Compute the velocity of the component value as follows:
 * 
 * v_is = (x_is_current - x_is_last ) / dt v_should = (x_should_current -
 * x_should_last) / dt
 * 
 * The Newtonian force on the body is a combination of a harmonic force and a
 * friction force:
 *
 * F = F_friction + F_harmonic
 * 
 * Compute the force as follows: F_friction ~ - µ * sign(v_is*v_should) *
 * (v_is-v_should) F_harmonic ~ - k * x
 * 
 * Whereas - µ is the friction coefficient - k is the harmonic coefficient
 * 
 * These constants control how fast the is-values approach the should-values. In
 * particular, µ should be set high enough to prevent rebouncing.
 * 
 * @author madgaksha
 */
public class NewtonianGrantStrategy implements IGrantStrategy {
	private final static float DEFAULT_HARMONIC = 1.0f;
	private final static float DEFAULT_FRICTION = 1.0f;

	public float harmonic = DEFAULT_HARMONIC;
	public float friction = DEFAULT_FRICTION;

	public NewtonianGrantStrategy() {
		harmonic = DEFAULT_HARMONIC;
		friction = DEFAULT_FRICTION;
	}

	public NewtonianGrantStrategy(float harmonic, float friction) {
		this.harmonic = harmonic;
		this.friction = friction;
	}

	@Override
	public float compromise(float is, float should, float deltaTime) {
		// TODO
		return should;
	}
}
