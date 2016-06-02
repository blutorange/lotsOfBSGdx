package de.homelab.madgaksha.grantstrategy;

import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.logging.Logger;

/**
 * 
 * 
 * @author madgaksha
 */
public class SpeedIncreaseGrantStrategy implements IGrantStrategy {
	private final static float DEFAULT_INCREASE = 1.0f;
	private final static float DEFAULT_MAX_SPEED = 500.0f;
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SpeedIncreaseGrantStrategy.class);
	
	private float increase = DEFAULT_INCREASE;
	
	private float maxSpeed = DEFAULT_MAX_SPEED;
	
	private float currentSpeed = 0.0f;
	
	private Vector2 v = new Vector2();
	
	public SpeedIncreaseGrantStrategy() {
	}

	public SpeedIncreaseGrantStrategy(float increase) {
		this.increase = increase;
	}
	public SpeedIncreaseGrantStrategy(float increase, float maxSpeed) {
		this.increase = increase;
		this.maxSpeed = maxSpeed;
	}
	
	@Override
	public float compromise(float is, float should, float deltaTime) {
		currentSpeed += increase*deltaTime;
		return is < should ? Math.min(is+currentSpeed,should) : Math.max(is-currentSpeed,should);
	}
	
	@Override
	public Vector2 compromise2D(float isX, float isY, float shouldX, float shouldY, float deltaTime) {
		currentSpeed = Math.min(currentSpeed + increase*deltaTime, maxSpeed);
		v.set(shouldX-isX,shouldY-isY).nor().scl(currentSpeed*deltaTime).add(isX, isY);
		v.x = isX < shouldX ? Math.min(v.x,shouldX) : Math.max(v.x,shouldX);
		v.y = isY < shouldY ? Math.min(v.y,shouldY) : Math.max(v.y,shouldY);
		return v;
	}
}