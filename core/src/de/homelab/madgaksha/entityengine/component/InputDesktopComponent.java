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
	private final static float DEFAULT_ACCELERATION_FACTOR_LOW = 80.0f;
	private final static float DEFAULT_ACCELERATION_FACTOR_HIGH = 160.0f;
	
	private final static float DEFAULT_SCREEN_ORIENTATION = 0.0f;
	private final static boolean DEFAULT_RELATIVE_TO_CAMERA = true;
	private final static boolean DEFAULT_ORIENT_TO_SCREEN = false;
	
	private final static int DEFAULT_ENEMY_SWITCHER_NEXT = Keys.PAGE_DOWN;
	private final static int DEFAULT_ENEMY_SWITCHER_PREV = Keys.FORWARD_DEL;
	
	private final static float DEFAULT_BATTLE_SPEED_LOW = 300.0f;
	private final static float DEFAULT_BATTLE_SPEED_HIGH = 500.0f;
	
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
	public float accelerationFactorLow = DEFAULT_ACCELERATION_FACTOR_LOW;
	public float accelerationFactorHigh = DEFAULT_ACCELERATION_FACTOR_HIGH;
	
	public float screenOrientation = DEFAULT_SCREEN_ORIENTATION;
	public boolean relativeToCamera = DEFAULT_RELATIVE_TO_CAMERA;
	public boolean orientToScreen = DEFAULT_ORIENT_TO_SCREEN;
	
	public float battleSpeedLow = DEFAULT_BATTLE_SPEED_LOW;
	public float battleSpeedHigh = DEFAULT_BATTLE_SPEED_HIGH;
	
	
	public InputDesktopComponent() {
	}
	public InputDesktopComponent(float accelerationFactor, float battleSpeed) {
		this.accelerationFactorHigh= this.accelerationFactorLow = accelerationFactor;
		this.battleSpeedHigh= this.battleSpeedLow = battleSpeed;
	}
	public InputDesktopComponent(float accelerationFactorLow, float accelerationFactorHigh, float frictionFactor, float battleSpeedLow, float battleSpeedHigh, boolean relativeToCamera, boolean orientToScreen) {
		this.accelerationFactorLow = accelerationFactorLow;
		this.accelerationFactorHigh = accelerationFactorHigh;
		this.frictionFactor = frictionFactor;
		this.battleSpeedHigh = battleSpeedHigh;
		this.battleSpeedLow = battleSpeedLow;
		this.orientToScreen = orientToScreen;
		this.relativeToCamera = relativeToCamera;
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
		frictionFactor = DEFAULT_FRICTION_FACTOR;
		accelerationFactorLow = DEFAULT_ACCELERATION_FACTOR_LOW;
		accelerationFactorHigh = DEFAULT_ACCELERATION_FACTOR_HIGH;
		
		enemySwitcherPrev = DEFAULT_ENEMY_SWITCHER_PREV;
		enemySwitcherNext = DEFAULT_ENEMY_SWITCHER_NEXT;

	}

}
