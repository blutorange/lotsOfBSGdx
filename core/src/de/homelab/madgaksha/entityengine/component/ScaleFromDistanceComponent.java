package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.Mapper;

/**
 * Makes an entity smaller/larger the closer it is to another entity. Scale will
 * be {@link #minScale} at distances smaller than {@link #minDistance}, and
 * {@link #maxScale} at distances greater than {@link #maxDistance}. Linearly
 * interpolated inbetween.
 * 
 * @author mad_gaksha
 */
public class ScaleFromDistanceComponent implements Component, Poolable {
	private final static float DEFAULT_MIN_SCALE = 0.0f;
	private final static float DEFAULT_MAX_SCALE = 0.0f;
	private final static float DEFAULT_MIN_DISTANCE = 0.0f;
	private final static float DEFAULT_MAX_DISTANCE = 1.0f;

	public PositionComponent positionComponent = null;
	public float minScale = DEFAULT_MIN_SCALE;
	public float maxScale = DEFAULT_MAX_SCALE;
	public float minDistance = DEFAULT_MIN_DISTANCE;
	public float maxDistance = DEFAULT_MAX_DISTANCE;

	public ScaleFromDistanceComponent() {
	}

	public ScaleFromDistanceComponent(PositionComponent pc, float minScale, float maxScale, float minDistance,
			float maxDistance) {
		setup(pc, minScale, maxScale, minDistance, maxDistance);
	}

	public ScaleFromDistanceComponent(Entity e, float minScale, float maxScale, float minDistance, float maxDistance) {
		setup(e, minScale, maxScale, minDistance, maxDistance);
	}

	public void setup(Entity e, float minScale, float maxScale, float minDistance, float maxDistance) {
		setup(Mapper.positionComponent.get(e), minScale, maxScale, minDistance, maxDistance);
	}

	public void setup(PositionComponent pc, float minScale, float maxScale, float minDistance, float maxDistance) {
		this.positionComponent = pc;
		this.minScale = minScale;
		this.maxScale = maxScale;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
	}

	@Override
	public void reset() {
		positionComponent = null;
		minScale = DEFAULT_MIN_SCALE;
		maxScale = DEFAULT_MAX_SCALE;
		minDistance = DEFAULT_MIN_DISTANCE;
		maxDistance = DEFAULT_MAX_DISTANCE;
	}

}
