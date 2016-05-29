package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.CallbackMaker;

/**
 * Contains information for {@link CallbackMaker} entities.
 * @author madgaksha
 *
 */
public class BulletStatusComponent implements Component, Poolable {
	private final static long DEFAULT_POWER = 1L;
	
	public long power = DEFAULT_POWER;
	
	public BulletStatusComponent(){
	}
	
	public BulletStatusComponent(long power) {
		setup(power);
	}
	
	public void setup(long power) {
		this.power = power;
	}
	
	@Override
	public void reset() {
		power = DEFAULT_POWER;
	}}
