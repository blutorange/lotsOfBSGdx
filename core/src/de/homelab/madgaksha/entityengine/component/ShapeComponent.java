package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.enums.EShapeType;

/**
 * Represents the dimensions of an object in world space.
 * 
 * Unit: Meter.
 * 
 * @author mad_gaksha
 */
public class ShapeComponent implements Component, Poolable {
	private final static EShapeType DEFAULT_SHAPE_TYPE = EShapeType.RECTANGLE;
	private final static Shape2D DEFAULT_SHAPE = new Rectangle(-0.5f, -0.5f, 1.0f, 1.0f);

	public Shape2D shape = DEFAULT_SHAPE;
	public EShapeType shapeType = DEFAULT_SHAPE_TYPE;

	public ShapeComponent() {
	}

	public ShapeComponent(Shape2D shape) {
		setup(shape);
	}

	public ShapeComponent(Shape2D shape, EShapeType shapeType) {
		setup(shape, shapeType);
	}

	public void setup(Shape2D shape) {
		this.shape = shape;
		this.shapeType = EShapeType.valueOf(shape);
	}

	public void setup(Shape2D shape, EShapeType shapeType) {
		this.shape = shape;
		this.shapeType = shapeType;
	}

	@Override
	public void reset() {
		this.shape = DEFAULT_SHAPE;
		this.shapeType = DEFAULT_SHAPE_TYPE;
	}

}
