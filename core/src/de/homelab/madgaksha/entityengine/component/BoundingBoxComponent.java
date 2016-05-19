package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
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
	
	private final static float DEFAULT_CENTER_X = 0.0f;
	private final static float DEFAULT_CENTER_Y = 0.0f;
	
	public float halfwidth = DEFAULT_HALFWIDTH;
	public float halfheight = DEFAULT_HALFHEIGHT;
	public float halfdepth = DEFAULT_HALFDEPTH;
	public float centerX = DEFAULT_CENTER_X;
	public float centerY = DEFAULT_CENTER_Y;
	
	public BoundingBoxComponent() {
	}

	public BoundingBoxComponent(Rectangle r) {
		this.halfwidth = r.width * 0.5f;
		this.halfheight = r.height * 0.5f;
		this.centerX = r.x + this.halfwidth;
		this.centerY = r.y + this.halfheight;
	}
	public BoundingBoxComponent(float hw, float hh) {
		this.halfwidth = hw;
		this.halfheight = hh;
	}
	public BoundingBoxComponent(float hw, float hh, float x, float y) {
		this.halfwidth = hw;
		this.halfheight = hh;
		this.centerX = x;
		this.centerY = y;
	}
	public BoundingBoxComponent(float hw, float hh, float hd) {
		this.halfwidth = hw;
		this.halfheight = hh;
		this.halfdepth = hd;
	}
	public BoundingBoxComponent(float hw, float hh, float hd, float x, float y) {
		this.halfwidth = hw;
		this.halfheight = hh;
		this.halfdepth = hd;
		this.centerX = x;
		this.centerY = y;
	}
	
	public void set(Rectangle r) {
		this.halfwidth = 0.5f*r.width;
		this.halfheight = 0.5f*r.height;
		this.centerX = r.x + this.halfwidth;
		this.centerY = r.y + this.halfheight;
	}

	@Override
	public void reset() {
		halfwidth = DEFAULT_HALFWIDTH;
		halfheight = DEFAULT_HALFHEIGHT;
		halfdepth = DEFAULT_HALFDEPTH;
		centerX = DEFAULT_CENTER_X;
		centerY = DEFAULT_CENTER_Y;
	}

}
