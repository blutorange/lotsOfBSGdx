package de.homelab.madgaksha.entitysystem.component;

import com.badlogic.ashley.core.Component;

/**
 * Represents the force that is applied to objects.
 * 
 * F = (F_x, F_y, F_z).
 * 
 * Unit: Newton = kg*m/s^2.
 * 
 * @author mad_gaksha
 */
public class ForceComponent implements Component {
	public float x;
	public float y;
	public float z = 0.0f;
}
