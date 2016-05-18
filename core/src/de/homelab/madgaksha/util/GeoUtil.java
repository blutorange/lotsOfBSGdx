package de.homelab.madgaksha.util;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

public class GeoUtil {
	private GeoUtil() {
	}

	public static Rectangle getBoundingBox(Shape2D shape) {
		final Rectangle r;
		if (shape instanceof Rectangle) {
			r = (Rectangle) shape;
		} else if (shape instanceof Circle) {
			Circle c = (Circle) shape;
			r = new Rectangle(c.x - c.radius, c.y - c.radius, 2.0f * c.radius, 2.0f * c.radius);
		} else if (shape instanceof Ellipse) {
			Ellipse e = (Ellipse) shape;
			r = new Rectangle(e.x, e.y, e.width, e.height);
		} else if (shape instanceof Polygon) {
			Polygon p = (Polygon) shape;
			r = p.getBoundingRectangle();
		} else if (shape instanceof Polyline) {
			// Will not happen usually, as this
			// object should only be instantiated
			// by MapData, which performs type-
			// checking already.
			// Add slow implementation anyway.
			Polyline pl = (Polyline) shape;
			Polygon pp = new Polygon(pl.getVertices());
			r = pp.getBoundingRectangle();
		} else {			
			throw new UnknownError("you are using a newer version of libgdx, as of writing there was no other subclass");
		}
		return r;
	}
}
