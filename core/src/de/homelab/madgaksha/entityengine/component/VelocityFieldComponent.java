package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.field.IVelocityField;
import de.homelab.madgaksha.field.ZeroVelocityField;

/**
 * Represents the velocity that is applied to objects.
 * 
 * V = (V_x, V_y, V_z).
 * 
 * Unit: Newton = worldUnits/s^2.
 * 
 * @author mad_gaksha
 */
public class VelocityFieldComponent implements Component, Poolable {
	private final static boolean DEFAULT_IGNORE_OFFSET = false;
	private final static IVelocityField DEFAULT_FIELD = new ZeroVelocityField();

	public IVelocityField field;
	public boolean ignoreOffset = DEFAULT_IGNORE_OFFSET;

	public VelocityFieldComponent() {
	}

	@Override
	public void reset() {
		field = DEFAULT_FIELD;
		ignoreOffset = DEFAULT_IGNORE_OFFSET;
	}

}
