package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Component for entities that are inactive, but might still be visible.
 * 
 * @see InvisibleComponent
 * @author madgaksha
 *
 */
public class InactiveComponent implements Component, Poolable {
	@Override
	public void reset() {
	}
}
