package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Stores the the inverse mass of a component.
 * 
 * Unit: 1/kg
 * 
 * @author madgaksha
 *
 */
public class InverseMassComponent implements Component, Poolable {
	private final static float DEFAULT_IMASS = 1.0f;
	public float imass = DEFAULT_IMASS;

	@Override
	public void reset() {
		imass = DEFAULT_IMASS;
	}

}
