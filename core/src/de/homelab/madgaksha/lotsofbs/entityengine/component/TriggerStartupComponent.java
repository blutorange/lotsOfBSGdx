package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.ITrigger;

/**
 * A component for objects that need to call some internal java function when
 * the game is started.
 * 
 * @author mad_gaksha
 */
public class TriggerStartupComponent implements Component, Poolable {
	private final static ITrigger DEFAULT_TRIGGER_ACCEPTING_OBJECT = new ITrigger() {
		@Override
		public void callbackTrigger(Entity e, ETrigger t) {
		}
	};

	public ITrigger triggerAcceptingObject = DEFAULT_TRIGGER_ACCEPTING_OBJECT;

	public TriggerStartupComponent() {
	}

	public TriggerStartupComponent(ITrigger triggerAcceptingObject) {
		this.triggerAcceptingObject = triggerAcceptingObject;
	}

	@Override
	public void reset() {
		triggerAcceptingObject = DEFAULT_TRIGGER_ACCEPTING_OBJECT;
	}

}
