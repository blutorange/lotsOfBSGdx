package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the position of an object in world space.
 * 
 * R = (R_x, R_y, R_z).
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class InputComponent implements Component, Poolable {
	private final static int DEFAULT_LEFT = Keys.LEFT;
	private final static int DEFAULT_RIGHT = Keys.RIGHT;
	private final static int DEFAULT_DOWN = Keys.DOWN;
	private final static int DEFAULT_UP = Keys.UP;
	
	private final static int DEFAULT_DIRECTION_UP = Keys.W;
	private final static int DEFAULT_DIRECTION_DOWN = Keys.S;
	private final static int DEFAULT_DIRECTION_RIGHT = Keys.D;
	private final static int DEFAULT_DIRECTION_LEFT = Keys.A;
	
	private final static float DEFAULT_SPEED = 1.0f;
	private final static float DEFAULT_SCREEN_ORIENTATION = 0.0f;
	private final static boolean DEFAULT_RELATIVE_TO_CAMERA = true;
	private final static boolean DEFAULT_ORIENT_TO_SCREEN = false;
	
	public int left = DEFAULT_LEFT;
	public int right = DEFAULT_RIGHT;
	public int up = DEFAULT_UP;
	public int down = DEFAULT_DOWN;
	
	public int directionUp = DEFAULT_DIRECTION_UP;
	public int directionDown = DEFAULT_DIRECTION_DOWN;
	public int directionLeft = DEFAULT_DIRECTION_LEFT;
	public int directionRight = DEFAULT_DIRECTION_RIGHT;
	
	public float speed = DEFAULT_SPEED;	
	public float screenOrientation = DEFAULT_SCREEN_ORIENTATION;
	public boolean relativeToCamera = DEFAULT_RELATIVE_TO_CAMERA;
	public boolean orientToScreen = DEFAULT_ORIENT_TO_SCREEN;
	
	public InputComponent() {
	}
	public InputComponent(float speed) {
		this.speed = speed;
	}
	public InputComponent(float speed, boolean r, boolean o) {
		this.speed = speed;
		this.orientToScreen = o;
		this.relativeToCamera = r;
	}
	public static InputComponent relativeToCamera(float speed, boolean b) {
		final InputComponent ic = new InputComponent(speed);
		ic.relativeToCamera = b;
		return ic;
	}
	public static InputComponent orientToScreen(float speed, float screenOrientation, boolean b) {
		final InputComponent ic = new InputComponent(speed);
		ic.orientToScreen = b;
		ic.screenOrientation = screenOrientation;
		return ic;
	}
	
	@Override
	public void reset() {
		up = DEFAULT_UP;
		down = DEFAULT_DOWN;
		right = DEFAULT_RIGHT;
		left = DEFAULT_LEFT;
		
		directionUp = DEFAULT_DIRECTION_UP;
		directionDown = DEFAULT_DIRECTION_DOWN;
		directionLeft = DEFAULT_DIRECTION_LEFT;
		directionRight = DEFAULT_DIRECTION_RIGHT;
		
		relativeToCamera = DEFAULT_RELATIVE_TO_CAMERA;
		orientToScreen = DEFAULT_ORIENT_TO_SCREEN;
		screenOrientation = DEFAULT_SCREEN_ORIENTATION;
		speed = DEFAULT_SPEED;
	}

}
