package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.CallbackMaker;
import de.homelab.madgaksha.enums.ElementalType;

/**
 * Contains information for {@link CallbackMaker} entities.
 * 
 * @author madgaksha
 *
 */
public class BulletStatusComponent implements Component, Poolable {
	private final static long DEFAULT_POWER = 1L;
	private final static long DEFAULT_SCORE = 0L;
	private final static ElementalType DEFAULT_ELEMENTAL_TYPE = ElementalType.NORMAL;

	public long power = DEFAULT_POWER;
	public long score = DEFAULT_SCORE;
	public ElementalType elementalType;

	public BulletStatusComponent() {
	}

	public BulletStatusComponent(long power) {
		setup(power);
	}

	public BulletStatusComponent(long power, long score) {
		setup(power);
	}

	public void setup(long power) {
		this.power = power;
	}

	public void setup(long power, long score) {
		this.power = power;
		this.score = score;
	}

	@Override
	public void reset() {
		power = DEFAULT_POWER;
	}
}
