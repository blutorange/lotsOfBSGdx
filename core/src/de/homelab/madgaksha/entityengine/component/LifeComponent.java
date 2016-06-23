package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.IBehaving;
import de.homelab.madgaksha.entityengine.entity.IMortal;

/**
 * Contains the callback {@link Entity} implementing {@link IBehaving}.
 * 
 * @author madgaksha
 *
 */
public class LifeComponent implements Component, Poolable {

	private final static float DEFAULT_REMAINING_LIFE = 1.0f;

	public float remainingLife = DEFAULT_REMAINING_LIFE;
	public IMortal onDeath = null;

	public LifeComponent() {
	}

	public LifeComponent(float remainingLife) {
		setup(remainingLife);
	}

	public LifeComponent(float remainingLife, IMortal onDeath) {
		setup(remainingLife, onDeath);
	}

	public void setup(float remainingLife) {
		this.remainingLife = remainingLife;
	}

	public void setup(float remainingLife, IMortal onDeath) {
		this.remainingLife = remainingLife;
		this.onDeath = onDeath;
	}

	@Override
	public void reset() {
		remainingLife = DEFAULT_REMAINING_LIFE;
		onDeath = null;
	}
}
