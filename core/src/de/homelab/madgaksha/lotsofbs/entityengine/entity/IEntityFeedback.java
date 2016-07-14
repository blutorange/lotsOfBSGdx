package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import com.badlogic.ashley.core.Entity;

public interface IEntityFeedback {
	public static final IEntityFeedback TRUE = new IEntityFeedback() {
		/** Does nothing. */
		@Override
		public boolean check(Entity entity, Object data) {
			return true;
		}		
	};
	public static final IEntityFeedback FALSE = new IEntityFeedback() {
		/** Does nothing. */
		@Override
		public boolean check(Entity entity, Object data) {
			return false;
		}		
	};
	public boolean check(Entity entity, Object data);
}
