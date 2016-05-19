package de.homelab.madgaksha.util;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class GeoUtil {
	private GeoUtil() {
	}
	
	private final static Vector3 v1 = new Vector3();
	private final static Vector3 v2 = new Vector3();
	private final static Vector3 v3 = new Vector3();
	private final static Vector3 v4 = new Vector3();
	
	private final static Ray r1 = new Ray();
	private final static Ray r2 = new Ray();
	private final static Ray r3 = new Ray();
	private final static Ray r4 = new Ray();
	
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
	
	/**
	 * @param f Frustum definining the cone (without the clipping planes).
	 * @param p Plane to intersect
	 * @param output 4 points for storing the intersecting quadrangle. (x,y,z),(x,y,z),(x,y,z),(x,y,z).
	 */
	public static void getConePlaneIntersection(Frustum f, Plane p, float[] output) {
		Vector3[] pts = f.planePoints;
		r1.set(pts[0].x, pts[0].y, pts[0].z, pts[4].x - pts[0].x, pts[4].y - pts[0].y, pts[4].z - pts[0].z);
		r2.set(pts[1].x, pts[1].y, pts[1].z, pts[5].x - pts[1].x, pts[5].y - pts[1].y, pts[5].z - pts[1].z);
		r3.set(pts[2].x, pts[2].y, pts[2].z, pts[6].x - pts[2].x, pts[6].y - pts[2].y, pts[6].z - pts[2].z);
		r4.set(pts[3].x, pts[3].y, pts[3].z, pts[7].x - pts[3].x, pts[7].y - pts[3].y, pts[7].z - pts[3].z);
		Intersector.intersectRayPlane(r1, p, v1);
		Intersector.intersectRayPlane(r2, p, v2);
		Intersector.intersectRayPlane(r3, p, v3);
		Intersector.intersectRayPlane(r4, p, v4);
		output[0] = v1.x;
		output[1] = v1.y;
		output[2] = v1.z;
		output[3] = v2.x;
		output[4] = v2.y;
		output[5] = v2.z;
		output[6] = v3.x;
		output[7] = v3.y;
		output[8] = v3.z;
		output[9] = v4.x;
		output[10] = v4.y;
		output[11] = v4.z;
	}
}
