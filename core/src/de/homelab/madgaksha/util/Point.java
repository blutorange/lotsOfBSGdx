package de.homelab.madgaksha.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.logging.Logger;

public class Point implements Shape2D {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Point.class);

	public float x = 0.0f;
	public float y = 0.0f;

	public Point(){
		
	}
	
	/**
	 * Constructs a new point at the center of the rectangle.
	 * 
	 * @param r
	 *            Rectangle at whose center this point will lie.
	 */
	public Point(Rectangle r) {
		x = r.x + r.width * 0.5f;
		y = r.y + r.height * 0.5f;
	}

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	@Override
	public boolean contains(Vector2 point) {
		return x == point.x && y == point.y;
	}

	@Override
	public boolean contains(float x, float y) {
		return this.x == x && this.y == y;
	}

	public boolean inside(Shape2D shape) {
		return shape.contains(x, y);
	}
}
