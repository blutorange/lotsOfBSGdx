package de.homelab.madgaksha.entityengine.component;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * A list of components to be removed as a list of components to be added.
 * @author madgaksha
 *
 */
public class ComponentQueueComponent implements Component, Poolable {
	private final static List<Component> DEFAULT_ADD = new ArrayList<Component>(10); 
	private final static List<Class<? extends Component>> DEFAULT_REMOVE = new ArrayList<Class<? extends Component>>(10);

	public List<Component> add = DEFAULT_ADD;
	public List<Class<? extends Component>> remove = DEFAULT_REMOVE;
	
	public ComponentQueueComponent() {
	}
	
	@Override
	public void reset() {
		add.clear();
		remove.clear();
	}
}
