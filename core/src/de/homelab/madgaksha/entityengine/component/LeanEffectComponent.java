package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Let v be the velocity of the object. The rotation angle and scale is
 * determined as follows:
 * 
 * <pre>
 * angle = 0.0 +- targetAngle * (1-exp(-leanFactor*v^2))
 * scale = 1.0 +- targetScale * (1-exp(-leanFactor*v^2))
 * </pre>
 * 
 * The sign is determined by the direction of the velocity vector.
 * 
 * @author mad_gaksha
 */
public class LeanEffectComponent implements Component, Poolable {
	private final static float DEFAULT_LEAN_FACTOR = 1.0f;
	private final static float DEFAULT_TARGET_ANGLE = 20.0f;
	private final static float DEFAULT_TARGET_SCALE = 0.2f;

	public float leanFactor = DEFAULT_LEAN_FACTOR;
	public float targetAngle = DEFAULT_TARGET_ANGLE;
	public float targetScale = DEFAULT_TARGET_SCALE;
	
	public LeanEffectComponent() {
	}

	public LeanEffectComponent(float targetAngle, float targetScale) {
		this.targetAngle = targetAngle;
		this.targetScale = targetScale;
	}

	public LeanEffectComponent(float targetAngle, float targetScale, float leanFactor) {
		this.targetAngle = targetAngle;
		this.targetScale = targetScale;
		this.leanFactor = leanFactor;
	}

	@Override
	public void reset() {
		leanFactor = DEFAULT_LEAN_FACTOR;
		targetAngle = DEFAULT_TARGET_ANGLE;
		targetScale = DEFAULT_TARGET_SCALE;
	}

}
