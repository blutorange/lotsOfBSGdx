package de.homelab.madgaksha.entitysystem.component;


/**
 * Let the component should-value be x_should.
 * Let the component is-value be x_is.
 * Let the time step be dt.
 * 
 * Set x = x_is - x_should
 *
 * Compute the velocity of the component
 * value as follows:
 * 
 * v_is = (x_is_current - x_is_last ) / dt
 * v_should = (x_should_current - x_should_last) / dt
 * 
 * The Newtonian force on the body is a combination of
 * a harmonic force and a friction force:
 *
 *   F = F_friction + F_harmonic
 *   
 * Compute the force as follows:
 *   F_friction ~ - µ * sign(v_is*v_should) * (v_is-v_should)
 *   F_harmonic ~ - k * x
 *   
 * Whereas 
 *  - µ is the friction coefficient
 *  - k is the harmonic coefficient
 *  
 * These constants control how fast the is-values
 * approach the should-values. In particular, µ
 * should be set high enough to prevent rebouncing.
 */
public class NewtonianGrantComponent {
	public float harmonic;
	public float friction;
}
