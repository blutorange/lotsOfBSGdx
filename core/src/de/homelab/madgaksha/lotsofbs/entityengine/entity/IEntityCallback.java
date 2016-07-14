package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import com.badlogic.ashley.core.Entity;

public interface IEntityCallback {
	public static final IEntityCallback NOOP = new IEntityCallback() {
		/** Does nothing. */
		@Override
		public void run(Entity entity, Object data) {
		}		
	};
	public void run(Entity entity, Object data);
}
