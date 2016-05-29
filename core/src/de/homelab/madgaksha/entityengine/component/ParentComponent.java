package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Component for hierarchical (tree) structure.
 * @author madgaksha
 *
 */
public class ParentComponent implements Component, Poolable {
	private final static Entity DEFAULT_PARENT = null;
	
	public Entity parent = DEFAULT_PARENT;
	
	public ParentComponent(){
	}
	
	public ParentComponent(Entity parent) {
		setup(parent);
	}
	
	public void setup(Entity parent) {
		this.parent = parent;
	}
	
	@Override
	public void reset() {
		parent = DEFAULT_PARENT;
	}}
