package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Component for linked list structures.
 * 
 * @author madgaksha
 *
 */
public class SiblingComponent implements Component, Poolable {
	private final static Entity DEFAULT_NEXT_CHILD = null;
	private final static Entity DEFAULT_PREV_CHILD = null;

	public Entity nextChild = DEFAULT_NEXT_CHILD;
	public Entity prevChild = DEFAULT_PREV_CHILD;

	public SiblingComponent() {
	}

	public SiblingComponent(Entity prevChild, Entity nextChild) {
		setup(prevChild, nextChild);
	}

	public void setup(Entity prevChild, Entity nextChild) {
		this.prevChild = prevChild;
		this.nextChild = nextChild;
	}

	@Override
	public void reset() {
		prevChild = DEFAULT_PREV_CHILD;
		nextChild = DEFAULT_NEXT_CHILD;
	}
}