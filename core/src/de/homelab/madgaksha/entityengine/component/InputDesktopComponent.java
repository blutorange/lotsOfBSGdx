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
public class InputDesktopComponent implements Component, Poolable {
	private final static int DEFAULT_LEFT = Keys.LEFT;
	private final static int DEFAULT_RIGHT = Keys.RIGHT;
	private final static int DEFAULT_DOWN = Keys.DOWN;
	private final static int DEFAULT_UP = Keys.UP;
	
	private final static int DEFAULT_DIRECTION_UP = Keys.W;
	private final static int DEFAULT_DIRECTION_DOWN = Keys.S;
	private final static int DEFAULT_DIRECTION_RIGHT = Keys.D;
	private final static int DEFAULT_DIRECTION_LEFT = Keys.A;
	
	private final static int DEFAULT_SPEED_TRIGGER = Keys.SHIFT_LEFT; 
	
	private final static float DEFAULT_FRICTION_FACTOR = 0.8f;
	private final static float DEFAULT_ACCELERATION_FACTOR = 80.0f;
	
	private final static float DEFAULT_LOW_SPEED = 200.0f;
	private final static float DEFAULT_HIGH_SPEED = 800.0f;
	private final static float DEFAULT_SCREEN_ORIENTATION = 0.0f;
	private final static boolean DEFAULT_RELATIVE_TO_CAMERA = true;
	private final static boolean DEFAULT_ORIENT_TO_SCREEN = false;
	
	private final static int DEFAULT_ENEMY_SWITCHER_NEXT = Keys.PAGE_DOWN;
	private final static int DEFAULT_ENEMY_SWITCHER_PREV = Keys.FORWARD_DEL;
	
	public int left = DEFAULT_LEFT;
	public int right = DEFAULT_RIGHT;
	public int up = DEFAULT_UP;
	public int down = DEFAULT_DOWN;
	
	public int directionUp = DEFAULT_DIRECTION_UP;
	public int directionDown = DEFAULT_DIRECTION_DOWN;
	public int directionLeft = DEFAULT_DIRECTION_LEFT;
	public int directionRight = DEFAULT_DIRECTION_RIGHT;
	
	public int speedTrigger = DEFAULT_SPEED_TRIGGER;
	public int enemySwitcherPrev = DEFAULT_ENEMY_SWITCHER_PREV;
	public int enemySwitcherNext = DEFAULT_ENEMY_SWITCHER_NEXT;
		
	public float frictionFactor = DEFAULT_FRICTION_FACTOR;
	public float accelerationFactor = DEFAULT_ACCELERATION_FACTOR;
	
	public float lowSpeed = DEFAULT_LOW_SPEED;	
	public float highSpeed = DEFAULT_HIGH_SPEED;
	public float screenOrientation = DEFAULT_SCREEN_ORIENTATION;
	public boolean relativeToCamera = DEFAULT_RELATIVE_TO_CAMERA;
	public boolean orientToScreen = DEFAULT_ORIENT_TO_SCREEN;
	
	
	public InputDesktopComponent() {
	}
	public InputDesktopComponent(float speed) {
		this.lowSpeed = this.highSpeed = speed;
	}
	public InputDesktopComponent(float lowSpeed, float highSpeed, float frictionFactor, float accelerationFactor, boolean r, boolean o) {
		this.lowSpeed = lowSpeed;
		this.highSpeed = highSpeed;
		this.frictionFactor = frictionFactor;
		this.accelerationFactor = accelerationFactor;
		this.orientToScreen = o;
		this.relativeToCamera = r;
	}

	public static InputDesktopComponent relativeToCamera(float speed, boolean b) {
		final InputDesktopComponent ic = new InputDesktopComponent(speed);
		ic.relativeToCamera = b;
		return ic;
	}
	public static InputDesktopComponent orientToScreen(float speed, float screenOrientation, boolean b) {
		final InputDesktopComponent ic = new InputDesktopComponent(speed);
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
		
		speedTrigger = DEFAULT_SPEED_TRIGGER;
		
		relativeToCamera = DEFAULT_RELATIVE_TO_CAMERA;
		orientToScreen = DEFAULT_ORIENT_TO_SCREEN;
		screenOrientation = DEFAULT_SCREEN_ORIENTATION;
		lowSpeed = DEFAULT_LOW_SPEED;
		highSpeed = DEFAULT_HIGH_SPEED;
		frictionFactor = DEFAULT_FRICTION_FACTOR;
		accelerationFactor = DEFAULT_ACCELERATION_FACTOR;
		
		enemySwitcherPrev = DEFAULT_ENEMY_SWITCHER_PREV;
		enemySwitcherNext = DEFAULT_ENEMY_SWITCHER_NEXT;

	}

}
