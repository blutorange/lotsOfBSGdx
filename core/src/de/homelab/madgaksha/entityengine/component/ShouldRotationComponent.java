package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.grantstrategy.IGrantStrategy;

/**
 * The rotation an object should take in the immediate future.
 * 
 * @see RotationComponent
 * @author madgaksha
 *
 */
public class ShouldRotationComponent extends RotationComponent implements Component, Poolable {
	private final static IGrantStrategy DEFAULT_GRANT_STRATEGY = new ExponentialGrantStrategy();
	public IGrantStrategy grantStrategy = DEFAULT_GRANT_STRATEGY;

	public void reset() {
		super.reset();
		grantStrategy = DEFAULT_GRANT_STRATEGY;
	}
}
