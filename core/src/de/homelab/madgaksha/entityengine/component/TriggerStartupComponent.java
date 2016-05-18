package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the force that is applied to objects.
 * 
 * F = (F_x, F_y, F_z).
 * 
 * Unit: Newton = kg*m/s^2.
 * 
 * @author mad_gaksha
 */
public class TriggerStartupComponent implements Component, Poolable {

	@Override
	public void reset() {
	}

}
