package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents the dimensions of an object in world space.
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class ShapeComponent implements Component, Poolable {
	private final static Shape2D DEFAULT_SHAPE = new Shape2D() {
		@Override
		public boolean contains(float x, float y) {
			return false;
		}		
		@Override
		public boolean contains(Vector2 point) {
			return false;
		}
	};

	public Shape2D shape = DEFAULT_SHAPE;
	
	public ShapeComponent() {
	}

	public ShapeComponent(Shape2D shape) {
		this.shape = shape;
	}
	
	@Override
	public void reset() {
		this.shape = DEFAULT_SHAPE;
	}

}
