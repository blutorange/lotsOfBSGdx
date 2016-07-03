package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Component for entities that are invisible, but might still be active.
 * 
 * @see InactiveComponent
 * @author madgaksha
 *
 */
public class CameraFocusComponent implements Component, Poolable {
	@Override
	public void reset() {
	}
}