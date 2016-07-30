package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.lotsofbs.grantstrategy.IGrantStrategy;

/**
 * The position an object should take in the immediate future.
 * 
 * @see PositionComponent
 * @author madgaksha
 *
 */
public class ShouldPositionComponent extends PositionComponent implements Component, Poolable {
	private final static IGrantStrategy DEFAULT_GRANT_STRATEGY = new ExponentialGrantStrategy();
	private final static boolean DEFAULT_GRANT_OFFSET = false;
	private final static boolean DEFAULT_REMOVE_ON_FULLFILL = false;

	public IGrantStrategy grantStrategy = DEFAULT_GRANT_STRATEGY;
	public boolean grantOffset = DEFAULT_GRANT_OFFSET;
	public boolean removeOnFulfill = DEFAULT_REMOVE_ON_FULLFILL;
	
	public ShouldPositionComponent() {

	}

	public ShouldPositionComponent(IGrantStrategy grantStrategy) {
		setup(grantStrategy);
	}

	public ShouldPositionComponent(IGrantStrategy grantStrategy, boolean grantOffset) {
		setup(grantStrategy, grantOffset);
	}

	public void setup(IGrantStrategy gs) {
		grantStrategy = gs;
	}

	public void setup(IGrantStrategy grantStrategy, boolean grantOffset) {
		this.grantStrategy = grantStrategy;
		this.grantOffset = grantOffset;
	}
	
	public void setup(IGrantStrategy grantStrategy, boolean grantOffset, boolean removeOnFullfill) {
		this.grantStrategy = grantStrategy;
		this.grantOffset = grantOffset;
		this.removeOnFulfill = removeOnFullfill;
	}

	@Override
	public void reset() {
		super.reset();
		grantStrategy = DEFAULT_GRANT_STRATEGY;
		grantOffset = DEFAULT_GRANT_OFFSET;
		removeOnFulfill = DEFAULT_REMOVE_ON_FULLFILL;
	}
}
