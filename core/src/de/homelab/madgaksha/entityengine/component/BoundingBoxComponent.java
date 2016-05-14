package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the dimensions of an object in world space.
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class BoundingBoxComponent implements Component, Poolable {
	private final static float DEFAULT_HALFWIDTH = 0.5f;
	private final static float DEFAULT_HALFHEIGHT = 0.5f;
	private final static float DEFAULT_HALFDEPTH = 0.0f;
	
	public float halfwidth = DEFAULT_HALFWIDTH;
	public float halfheight = DEFAULT_HALFHEIGHT;
	public float halfdepth = DEFAULT_HALFDEPTH;
	
	
	public BoundingBoxComponent() {
	}

	public BoundingBoxComponent(float hw, float hh) {
		this.halfwidth = hw;
		this.halfheight = hh;
	}
	public BoundingBoxComponent(float hw, float hh, float hd) {
		this.halfwidth = hw;
		this.halfheight = hh;
		this.halfdepth = hd;
	}

	@Override
	public void reset() {
		halfwidth = DEFAULT_HALFWIDTH;
		halfheight = DEFAULT_HALFHEIGHT;
		halfdepth = DEFAULT_HALFDEPTH;
	}

}
