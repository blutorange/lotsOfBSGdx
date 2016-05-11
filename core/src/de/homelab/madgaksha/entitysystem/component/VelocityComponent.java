package de.homelab.madgaksha.entitysystem.component;

import com.badlogic.ashley.core.Component;

/**
 * Represents the velocity of an object in world coordinates.
 * 
 * V = (V_x, V_y, V_z).
 * 
 * Unit: m/s.
 * 
 * @author mad_gaksha
 */
public class VelocityComponent implements Component {
	public float x;
	public float y;
	public float z = 0.0f;
}
