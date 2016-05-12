package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the orientation of an object relative to the world coordinate
 * system.
 * 
 * The rotation is specified by three angles:
 * 
 * Theta = (Theta_x, Theta_y, Theta_z).
 * 
 * Theta_z : Rotation around the z-axis. In our 2D world, this the rotation
 * angle. Theta_x : Rotation around the x-axis. Theta_y : Rotation around the
 * y-axis.
 * 
 * Unit: Radian.
 * 
 * The center of rotation is given relative to the objects dimensions. A value
 * of 0.5f corresponds to the center of the object. By default, the center of
 * rotation is the center of the object.
 * 
 * Unit: Newton = kg*m/s^2.
 * 
 * @author mad_gaksha
 */
public class RotationComponent implements Component, Poolable {

	private final static float DEFAULT_THETA_X = 0.0f;
	private final static float DEFAULT_THETA_Y = 0.0f;
	private final static float DEFAULT_THETA_Z = 0.0f;
	private final static float DEFAULT_CENTER_X = 0.5f;
	private final static float DEFAULT_CENTER_Y = 0.5f;
	private final static float DEFAULT_CENTER_Z = 0.5f;

	public float thetaX = DEFAULT_THETA_X;
	public float thetaY = DEFAULT_THETA_Y;
	public float thetaZ = DEFAULT_THETA_Z;
	public float centerX = DEFAULT_CENTER_X;
	public float centerY = DEFAULT_CENTER_Y;
	public float centerZ = DEFAULT_CENTER_Z;

	@Override
	public void reset() {
		thetaX = DEFAULT_THETA_X;
		thetaY = DEFAULT_THETA_Y;
		thetaZ = DEFAULT_THETA_Z;
		centerX = DEFAULT_CENTER_X;
		centerY = DEFAULT_CENTER_Y;
		centerZ = DEFAULT_CENTER_Z;

	}

}
