package de.homelab.madgaksha.entitysystem.component;

import com.badlogic.ashley.core.Component;

/**
 * Represents the orientation of an object relative to
 * the world coordinate system.
 * 
 * The rotation is specified by three angles:
 * 
 * Theta = (Theta_x, Theta_y, Theta_z).
 * 
 * Theta_z : Rotation around the z-axis. In our 2D world, this
 * the rotation angle.
 * Theta_x : Rotation around the x-axis.
 * Theta_y : Rotation around the y-axis.
 * 
 * The center of rotation is given relative to the 
 * objects dimensions. A value of 0.5f corresponds to
 * the center of the object. By default, the center of
 * rotation is the center of the object.
 * 
 * Unit: Newton = kg*m/s^2.
 * 
 * @author mad_gaksha
 */
public class RotationComponent implements Component {
	public float thetaX;
	public float thetaY;
	public float thetaZ = 0.0f;
	public float centerX = 0.5f;
	public float centerY = 0.5f;
	public float centerZ = 0.5f;
}
