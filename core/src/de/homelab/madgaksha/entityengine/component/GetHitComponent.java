package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.CallbackMaker;
import de.homelab.madgaksha.entityengine.entity.IHittable;

/**
 * Contains information for {@link CallbackMaker} entities.
 * 
 * @author madgaksha
 *
 */
public class GetHitComponent implements Component, Poolable {
	private final static IHittable DEFAULT_HITTABLE = new IHittable() {
		@Override
		public void hitByBullet(Entity me, Entity you) {
		}
	};

	public IHittable hittable = DEFAULT_HITTABLE;

	public GetHitComponent() {
	}

	public GetHitComponent(IHittable hittable) {
		setup(hittable);
	}

	public void setup(IHittable hittable) {
		this.hittable = hittable;
	}

	@Override
	public void reset() {
		hittable = DEFAULT_HITTABLE;
	}
}
