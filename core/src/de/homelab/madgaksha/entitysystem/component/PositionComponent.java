package de.homelab.madgaksha.entitysystem.component;

import com.badlogic.ashley.core.Component;

/**
 * Represents the position of an object in world space.
 * 
 * R = (R_x, R_y, R_z).
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class PositionComponent implements Component {
	public float x;
	public float y;
	public float z = 0.0f;
}
