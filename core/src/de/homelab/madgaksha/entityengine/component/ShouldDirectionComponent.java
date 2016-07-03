package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.grantstrategy.IGrantStrategy;

/**
 * The direction an object should take in the immediate future.
 * 
 * @see DirectionComponent
 * @author madgaksha
 *
 */
public class ShouldDirectionComponent extends DirectionComponent implements Component, Poolable {
	private final static IGrantStrategy DEFAULT_GRANT_STRATEGY = new ExponentialGrantStrategy();
	public IGrantStrategy grantStrategy = DEFAULT_GRANT_STRATEGY;

	public ShouldDirectionComponent() {
	}

	public ShouldDirectionComponent(IGrantStrategy gs) {
		setup(gs);
	}

	public void setup(IGrantStrategy gs) {
		grantStrategy = gs;
	}

	public void reset() {
		super.reset();
		grantStrategy = DEFAULT_GRANT_STRATEGY;
	}
}
