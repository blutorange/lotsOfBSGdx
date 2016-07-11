package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;

/**
 * Component for hierarchical (tree) structure.
 * 
 * @author madgaksha
 *
 */
public class AnyChildComponent implements Component, Poolable {
	private final static SiblingComponent DEFAULT_CHILD_COMPONENT = null;

	public SiblingComponent childComponent = DEFAULT_CHILD_COMPONENT;

	public AnyChildComponent() {
	}

	public AnyChildComponent(Entity e) {
		setup(e);
	}

	public AnyChildComponent(SiblingComponent sc) {
		setup(sc);
	}

	public void setup(Entity e) {
		this.childComponent = Mapper.siblingComponent.get(e);
	}

	public void setup(SiblingComponent sc) {
		this.childComponent = sc;
	}

	@Override
	public void reset() {
		childComponent = DEFAULT_CHILD_COMPONENT;
	}
}
