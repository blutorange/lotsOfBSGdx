package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the rotation of a sprite or object. This is similar to
 * {@link DirectionComponent}, but this is a rotation of the sprite and rendered
 * by rotating the sprite, the direction corresponds to a different sprite
 * altogether.
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
	private final static float DEFAULT_CENTER_Z = 0.5f;
	private final static float DEFAULT_AXIS_X = 0.0f;
	private final static float DEFAULT_AXIS_Y = 0.0f;
	private final static float DEFAULT_AXIS_Z = 1.0f;
	private final static boolean DEFAULT_INVERSE_TO_CAMERA = true;

	public float centerX = DEFAULT_CENTER_X;
	public float centerY = DEFAULT_CENTER_Y;
	public float centerZ = DEFAULT_CENTER_Z;
	public float axisX = DEFAULT_AXIS_X;
	public float axisY = DEFAULT_AXIS_Y;
	public float axisZ = DEFAULT_AXIS_Z;
	public float thetaZ = DEFAULT_THETA_Z;
	public boolean inverseToCamera = DEFAULT_INVERSE_TO_CAMERA;

	public RotationComponent() {
	}

	public RotationComponent(float tz) {
		thetaZ = tz;
	}

	public RotationComponent(float tz, float cx, float cy) {
		thetaZ = tz;
		centerX = cx;
		centerY = cy;
	}

	public RotationComponent(boolean ic) {
		setup(ic);
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

	public RotationComponent(float degrees, float axisX, float axisY, float axisZ) {
		setup(degrees, axisX, axisY, axisZ);
	}

	public RotationComponent(float degrees, Vector3 axis) {
		setup(degrees, axis);
	}

	public RotationComponent(Vector3 axis) {
		setup(axis);
	}

	public void setup(boolean ic) {
		inverseToCamera = ic;
	}

	public void setup(float degrees, float axisX, float axisY, float axisZ) {
		this.axisX = axisX;
		this.axisY = axisY;
		this.axisZ = axisZ;
		this.thetaZ = degrees;
	}

	public void setup(Vector3 axis) {
		this.axisX = axis.x;
		this.axisY = axis.y;
		this.axisZ = axis.z;
	}

	public void setup(float degrees, Vector3 axis) {
		this.axisX = axis.x;
		this.axisY = axis.y;
		this.axisZ = axis.z;
		this.thetaZ = degrees;
	}

	@Override
	public void reset() {
		centerX = DEFAULT_CENTER_X;
		centerY = DEFAULT_CENTER_Y;
		centerZ = DEFAULT_CENTER_Z;
		thetaZ = DEFAULT_THETA_Z;
		inverseToCamera = DEFAULT_INVERSE_TO_CAMERA;
		axisX = DEFAULT_AXIS_X;
		axisY = DEFAULT_AXIS_Y;
		axisZ = DEFAULT_AXIS_Z;
	}
}
