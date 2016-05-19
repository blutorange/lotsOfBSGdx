package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.ITrigger;

/**
 * A component for objects that need to call some internal java function
 * when the object is visible on screen.
 * 
 * @author mad_gaksha
 */
public class TriggerScreenComponent implements Component, Poolable {
	private final static ITrigger DEFAULT_TRIGGER_ACCEPTING_OBJECT = new ITrigger() {
		@Override public void callbackTouch(Entity e) {}		
		@Override public void callbackStartup() {}
		@Override public void callbackScreen() {}
	};
	
	public ITrigger triggerAcceptingObject = DEFAULT_TRIGGER_ACCEPTING_OBJECT;

	public TriggerScreenComponent() {
	}
	
	public TriggerScreenComponent(ITrigger triggerAcceptingObject) {
		this.triggerAcceptingObject = triggerAcceptingObject;
	}
	
	@Override
	public void reset() {
		triggerAcceptingObject = DEFAULT_TRIGGER_ACCEPTING_OBJECT;
	}
}
