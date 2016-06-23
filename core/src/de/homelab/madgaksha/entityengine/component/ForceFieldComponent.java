package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.forcefield.IForceField;

/**
 * Represents the force that is applied to objects.
 * 
 * F = (F_x, F_y, F_z).
 * 
 * Unit: Newton = kg*m/s^2.
 * 
 * @author mad_gaksha
 */
public class ForceFieldComponent implements Component, Poolable {
	private final static boolean DEFAULT_IGNORE_OFFSET = false;
	private final static IForceField DEFAULT_FIELD = new IForceField() {
		@Override
		public Vector2 apply(Entity e) {
			return new Vector2();
		}
	};

	public IForceField field;
	public boolean ignoreOffset = DEFAULT_IGNORE_OFFSET;

	public ForceFieldComponent() {
	}

	@Override
	public void reset() {
		field = DEFAULT_FIELD;
		ignoreOffset = DEFAULT_IGNORE_OFFSET;
	}

}
