package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.IReceive;

/**
 * @author mad_gaksha
 */
public abstract class ReceiveTouchComponent implements Component, Poolable {
	private final static IReceive DEFAULT_TRIGGER_RECEIVING_OBJECT = new IReceive() {		
		@Override public void callbackTouched(Entity me, Entity you) {}
	};
	
	public IReceive triggerReceivingObject = DEFAULT_TRIGGER_RECEIVING_OBJECT;
	public ReceiveTouchComponent() {
	}
	public ReceiveTouchComponent(IReceive t) {
		this.triggerReceivingObject = t;
	}

	public void setup(IReceive t) {
		this.triggerReceivingObject = t;
	}
	
	@Override
	public void reset() {
		this.triggerReceivingObject = DEFAULT_TRIGGER_RECEIVING_OBJECT;
	}
}
