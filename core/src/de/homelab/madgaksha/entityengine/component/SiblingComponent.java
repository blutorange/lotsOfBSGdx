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
	private final static SiblingComponent DEFAULT_NEXT_SIBLING_COMPONENT = null;
	private final static SiblingComponent DEFAULT_PREV_SIBLING_COMPONENT = null;
	private final static Entity DEFAULT_ME = null;

	public SiblingComponent nextSiblingComponent = DEFAULT_NEXT_SIBLING_COMPONENT;
	public SiblingComponent prevSiblingComponent = DEFAULT_PREV_SIBLING_COMPONENT;
	public Entity me = DEFAULT_ME;

	public SiblingComponent() {
	}

	public SiblingComponent(SiblingComponent prevChild, SiblingComponent nextChild, Entity me) {
		setup(prevChild, nextChild, me);
	}

	public void setup(SiblingComponent prevSiblingComponent, SiblingComponent nextSiblingComponent, Entity me) {
		this.prevSiblingComponent = prevSiblingComponent;
		this.nextSiblingComponent = nextSiblingComponent;
		this.me = me;
	}

	@Override
	public void reset() {
		prevSiblingComponent = DEFAULT_PREV_SIBLING_COMPONENT;
		nextSiblingComponent = DEFAULT_NEXT_SIBLING_COMPONENT;
		me = DEFAULT_ME;
	}
}