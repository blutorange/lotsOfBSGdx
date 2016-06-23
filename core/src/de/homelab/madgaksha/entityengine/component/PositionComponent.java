package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.Mapper;

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
		setup(x, y);
	}

	public PositionComponent(PositionComponent pc) {
		setup(pc);
	}

	public PositionComponent(Entity e) {
		setup(e);
	}

	public PositionComponent(float x, float y, boolean l) {
		setup(x, y, l);
	}

	public PositionComponent(Vector2 v) {
		setup(v);
	}

	public PositionComponent(Vector2 v, boolean l) {
		setup(v, l);
	}

	public PositionComponent(float x, float y, float z) {
		setup(x, y, z);
	}

	public PositionComponent(float x, float y, float z, boolean l) {
		setup(x, y, z, l);
	}

	/**
	 * Sets this position component to the same values as the given position
	 * component.
	 * 
	 * @param pc
	 *            Position component to clone.
	 */
	public void setup(PositionComponent pc) {
		this.x = pc.x;
		this.y = pc.y;
		this.z = pc.z;
		this.offsetX = pc.offsetX;
		this.offsetY = pc.offsetY;
		this.offsetZ = pc.offsetZ;
		this.limitToMap = pc.limitToMap;
	}

	public void setup(Entity e) {
		final PositionComponent pc = Mapper.positionComponent.get(e);
		if (pc != null)
			setup(pc);
	}

	/**
	 * Sets this component to the given position on the xy-plane.
	 * 
	 * @param x
	 *            The x-position-
	 * @param y
	 *            The y-position.
	 */
	public void setup(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setup(float x, float y, boolean l) {
		this.x = x;
		this.y = y;
		limitToMap = l;
	}

	public void setup(Vector2 v) {
		setup(v.x, v.y, DEFAULT_LIMIT_TO_MAP);
	}

	public void setup(Vector2 v, boolean l) {
		setup(v.x, v.y, l);
	}

	public void setup(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setup(float x, float y, float z, boolean l) {
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

	@Override
	public String toString() {
		return "PositionComponent(" + x + "," + y + "," + z + ")+(" + offsetX + "," + offsetY + "," + offsetZ
				+ ");limitToMap=" + limitToMap;
	}

}
