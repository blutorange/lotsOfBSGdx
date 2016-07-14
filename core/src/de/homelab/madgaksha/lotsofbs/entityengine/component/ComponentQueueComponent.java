package de.homelab.madgaksha.lotsofbs.entityengine.component;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.IEntityFeedback;

/**
 * A list of components to be removed as a list of components to be added.
 * 
 * @author madgaksha
 *
 */
public class ComponentQueueComponent implements Component, Poolable {

	public final List<Component> add;
	public final List<Class<? extends Component>> remove;
	public IEntityFeedback applicable = IEntityFeedback.TRUE; 
	public Object callbackData = null;
	
	public ComponentQueueComponent() {
		add = new ArrayList<Component>(10);
		remove = new ArrayList<Class<? extends Component>>(10);
	}

	@Override
	public void reset() {
		add.clear();
		remove.clear();
		applicable = IEntityFeedback.TRUE;
		callbackData = null;
	}
}

