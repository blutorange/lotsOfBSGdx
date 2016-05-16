package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
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
public class PositionComponent implements Component, Poolable {
	private final static float DEFAULT_X = 0.0f;
	private final static float DEFAULT_Y = 0.0f;
	private final static float DEFAULT_Z = 0.0f;
	private final static float DEFAULT_OFFSET_X = 0.0f;
	private final static float DEFAULT_OFFSET_Y = 0.0f;
	private final static float DEFAULT_OFFSET_Z = 0.0f;
	private final static boolean DEFAULT_LIMIT_TO_MAP = false;
	
	public float x = DEFAULT_X;
	public float y = DEFAULT_Y;
	public float z = DEFAULT_Z;
	public float offsetX = DEFAULT_OFFSET_X;
	public float offsetY = DEFAULT_OFFSET_Y;
	public float offsetZ = DEFAULT_OFFSET_Z;
	
	public boolean limitToMap = DEFAULT_LIMIT_TO_MAP;
	
	public PositionComponent() {
	}

	public PositionComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public PositionComponent(float x, float y, boolean l) {
		this.x = x;
		this.y = y;
		limitToMap = l;
	}
	public PositionComponent(Vector2 v) {
		this(v.x, v.y, DEFAULT_LIMIT_TO_MAP);
	}
	public PositionComponent(Vector2 v, boolean l) {
		this(v.x, v.y, l);
	}
	
	public PositionComponent(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public PositionComponent(float x, float y, float z, boolean l) {
		this.x = x;
		this.y = y;
		this.z = z;
		limitToMap = l;
	}

	@Override
	public void reset() {
		x = DEFAULT_X;
		y = DEFAULT_Y;
		z = DEFAULT_Z;
		offsetX = DEFAULT_OFFSET_X;
		offsetY = DEFAULT_OFFSET_Y;
		offsetZ = DEFAULT_OFFSET_Z;

		limitToMap = DEFAULT_LIMIT_TO_MAP;
	}

}
