package de.homelab.madgaksha.entityengine.entityutils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Utilities for working with an entity's pain points. Usually processed by an appropriate entity system,
 * this should be used sparingly.
 * @author madgaksha
 *
 */
public class ComponentUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ComponentUtils.class);
	
	public static void applyComponentQueue(Entity e, ComponentQueueComponent cqc) {
		for (Class<? extends Component> c : cqc.remove) e.remove(c);
		for (Component c : cqc.add) e.add(c);
		cqc.add.clear();
		cqc.remove.clear();
	}
	
	public static void applyComponentQueue(Entity e) {
		final ComponentQueueComponent cqc = Mapper.componentQueueComponent.get(e);
		if (cqc != null) applyComponentQueue(e, cqc);
	}
}
