package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the rotation of a sprite or object. This is similar to {@link DirectionComponent},
 * but this is a rotation of the sprite and rendered by rotating the sprite, the direction
 * corresponds to a different sprite altogether.
 * 
 * The rotation is specified by three angles:
 * 
 * Theta = (Theta_x, Theta_y, Theta_z).
 * 
 * Theta_z : Rotation around the z-axis. In our 2D world, this the rotation
 * angle. Theta_x : Rotation around the x-axis. Theta_y : Rotation around the
 * y-axis.
 * 
 * Unit: Degree.
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

	private final static float DEFAULT_THETA_Z = 0.0f;
	private final static float DEFAULT_CENTER_X = 0.5f;
	private final static float DEFAULT_CENTER_Y = 0.5f;
	private final static boolean DEFAULT_INVERSE_TO_CAMERA = true;
	
	public float centerX = DEFAULT_CENTER_X;
	public float centerY = DEFAULT_CENTER_Y;
	public float thetaZ = DEFAULT_THETA_Z;
	public boolean inverseToCamera = DEFAULT_INVERSE_TO_CAMERA;

	public RotationComponent() {}
	public RotationComponent(float tz) {
		thetaZ = tz;
	}
	public RotationComponent(float tz, float cx, float cy) {
		thetaZ = tz;
		centerX = cx;
		centerY = cy;
	}
	public RotationComponent(float tz, boolean ic) {
		thetaZ = tz;
		inverseToCamera = ic;
	}
	public RotationComponent(float tz, float cx, float cy, boolean ic) {
		thetaZ = tz;
		centerX = cx;
		centerY = cy;
		inverseToCamera = ic;
	}
	
	@Override
	public void reset() {
		centerX = DEFAULT_CENTER_X;
		centerY = DEFAULT_CENTER_Y;
		thetaZ = DEFAULT_THETA_Z;
	}

}
