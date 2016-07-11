package de.homelab.madgaksha.lotsofbs.util;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.visibleWorldBoundingBox;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.lotsofbs.enums.EShapeType;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class GeoUtil {
	private GeoUtil() {
	}

	private final static Logger LOG = Logger.getLogger(GeoUtil.class);

	private final static Polygon p1 = new Polygon();
	private final static Polygon p2 = new Polygon();
	private final static Polygon p3 = new Polygon();

	private final static Vector3 v1 = new Vector3();
	private final static Vector3 v2 = new Vector3();
	private final static Vector3 v3 = new Vector3();
	private final static Vector3 v4 = new Vector3();

	private final static Vector2 w1 = new Vector2();
	private final static Vector2 w2 = new Vector2();
	private final static Vector2 w3 = new Vector2();

	private final static Circle c1 = new Circle();
	private final static Circle c2 = new Circle();

	private final static Rectangle t1 = new Rectangle();

	private final static Ray r1 = new Ray();
	private final static Ray r2 = new Ray();
	private final static Ray r3 = new Ray();
	private final static Ray r4 = new Ray();

	private static float f1 = 0.0f;
	private static float f2 = 0.0f;
	private static float f3 = 0.0f;

	private final static float[] fa8 = new float[8];
	private final static float[] fa82 = new float[8];

	private static Circle c;
	private static Ellipse e, ee;
	private static Polyline l;
	private static Rectangle t;
	private static Polygon p, pp;

	static {
		p1.setOrigin(0.0f, 0.0f);
		p2.setOrigin(0.0f, 0.0f);
		p3.setOrigin(0.0f, 0.0f);
	}

	public static Rectangle getBoundingBox(Shape2D shape) {
		final Rectangle r;
		if (shape instanceof Rectangle) {
			r = (Rectangle) shape;
		} else if (shape instanceof Circle) {
			Circle c = (Circle) shape;
			r = new Rectangle(c.x - c.radius, c.y - c.radius, 2.0f * c.radius, 2.0f * c.radius);
		} else if (shape instanceof Point) {
			Point p = (Point) shape;
			r = new Rectangle(p.x, p.y, p.x, p.y);
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
			throw new UnknownError(
					"you are using a newer version of libgdx, as of writing there was no other subclass");
		}
		return r;
	}

	/**
	 * @param f
	 *            Frustum definining the cone (without the clipping planes).
	 * @param p
	 *            Plane to intersect
	 * @param output
	 *            4 points for storing the intersecting quadrangle.
	 *            (x,y,z),(x,y,z),(x,y,z),(x,y,z).
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

	public static boolean isCollision(Shape2D a, Shape2D b, EShapeType sa, EShapeType sb, PositionComponent pa,
			PositionComponent pb) {
		switch (sa) {
		case POINT:
			return b.contains(((Point) a).x + pa.x - pb.x, ((Point) a).y + pa.y - pb.y);
		case CIRCLE:
			switch (sb) {
			case POINT:
				return a.contains(((Point) b).x + pb.x - pa.x, ((Point) b).y + pb.y - pa.x);
			case CIRCLE:
				c1.set((Circle) a);
				c2.set((Circle) b);
				f1 = f1 - c2.x - pb.x;
				f2 = f2 - c2.y - pb.y;
				return f1 * f1 + f2 * f2 <= (c1.radius + c2.radius) * (c1.radius + c2.radius);
			case ELLIPSE:
				// convert ellipse to polygon
				e = (Ellipse) b;
				for (int i = 0; i != 8; i += 2) {
					fa8[i] = e.x + e.width * MathUtils.sinDeg(45.0f * i);
					fa8[i + 1] = e.y + e.height * MathUtils.cosDeg(45.0f * i);
				}
				p2.setVertices(fa8);
				p2.setPosition(pb.x, pb.y);

				// convert circle to polygon
				c = (Circle) a;
				for (int i = 0; i != 8; i += 2) {
					fa82[i] = e.x + c.radius * MathUtils.sinDeg(45.0f * i);
					fa82[i + 1] = e.y + c.radius * MathUtils.cosDeg(45.0f * i);
				}
				p1.setVertices(fa82);
				p1.setPosition(pa.x, pa.y);

				return Intersector.intersectPolygons(p1, p2, p3);
			case POLYGON:
				c = (Circle) a;
				p = (Polygon) b;
				for (int i = 0; i != 8; i += 2) {
					fa8[i] = c.x + c.radius * MathUtils.sinDeg(45.0f * i);
					fa8[i + 1] = c.y + c.radius * MathUtils.cosDeg(45.0f * i);
				}
				p1.setVertices(fa8);
				p2.setVertices(p.getVertices());
				p1.setPosition(pa.x, pa.y);
				p2.setPosition(p.getX() + pb.x, p.getY() + pb.y);
				return Intersector.intersectPolygons(p1, p2, p3);
			case POLYLINE:
				// check each line segment separately
				c = (Circle) a;
				l = (Polyline) b;
				final float[] f = l.getVertices();
				f1 = c.radius * c.radius;
				f2 = pb.x + l.getOriginX();
				f3 = pb.y + l.getOriginY();
				w1.x = c.x + pa.x;
				w1.y = c.y + pa.y;
				// Degenerate point
				if (f.length == 2)
					return c.contains(f[0] + f2, f[1] + f3);
				w3.x = f[f.length - 2] + f2;
				w3.y = f[f.length - 1] + f3;
				for (int i = f.length - 4; i >= 0; i -= 2) {
					w2.x = f[i] + f2;
					w2.y = f[i + 1] + f3;
					if (Intersector.intersectSegmentCircle(w2, w3, w1, f1))
						return true;
					w3.x = w2.x;
					w3.y = w2.y;
				}
				return false;
			case RECTANGLE:
				// overlaps does not check for the case when the circle
				// is completely contained in the rectangle
				c1.set((Circle) a);
				t1.set((Rectangle) b);
				c1.x += pa.x;
				c1.y += pa.y;
				t1.x += pb.x;
				t1.y += pb.y;
				return t1.contains(c1.x, c1.y) || Intersector.overlaps(c1, t1);
			default:
				LOG.debug("unknown shape:" + b);
				return false;
			}
		case ELLIPSE:
			switch (sb) {
			case POINT:
				return a.contains(((Point) b).x + pb.x - pa.x, ((Point) b).y + pb.y - pa.x);
			case CIRCLE:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case ELLIPSE:
				// convert ellipse to circle
				e = (Ellipse) a;
				ee = (Ellipse) b;
				f1 = e.x + pa.x - ee.x - pb.x;
				f2 = e.y + pa.y - ee.y - pb.y;
				f3 = Math.max(e.width, e.height) + Math.max(ee.width, ee.height);
				return f1 * f1 + f2 * f2 <= f3 * f3;
			case POLYGON:
				// convert ellipse to polygon
				e = (Ellipse) a;
				p = (Polygon) b;
				for (int i = 0; i != 8; i += 2) {
					fa8[i] = e.x + e.width * MathUtils.sinDeg(45.0f * i);
					fa8[i + 1] = e.y + e.height * MathUtils.cosDeg(45.0f * i);
				}
				p1.setVertices(fa8);
				p2.setVertices(p.getVertices());
				p1.setPosition(pa.x, pa.y);
				p2.setPosition(p.getX() + pb.x, p.getY() + pb.y);
				return Intersector.intersectPolygons(p1, p2, p3);
			case POLYLINE:
				LOG.debug("collision ellipse-polyline unsupported");
				break;
			case RECTANGLE:
				// convert ellipse to polygon
				e = (Ellipse) a;
				for (int i = 0; i != 8; i += 2) {
					fa8[i] = e.x + e.width * MathUtils.sinDeg(45.0f * i);
					fa8[i + 1] = e.y + e.height * MathUtils.cosDeg(45.0f * i);
				}
				p1.setVertices(fa8);
				p1.setPosition(pa.x, pa.y);

				// convert rectangle to polygon
				t = (Rectangle) b;
				fa8[0] = t.x;
				fa8[1] = t.y;
				fa8[2] = t.x + t.width;
				fa8[3] = t.y;
				fa8[4] = t.x + t.width;
				fa8[5] = t.y + t.height;
				fa8[6] = t.x;
				fa8[7] = t.y + t.height;
				p2.setVertices(fa8);
				p2.setPosition(pb.x, pb.y);

				return Intersector.intersectPolygons(p1, p2, p3);
			default:
				LOG.debug("unknown shape:" + b);
				return false;
			}
		case POLYGON:
			switch (sb) {
			case POINT:
				return a.contains(((Point) b).x + pb.x - pa.x, ((Point) b).y + pb.y - pa.x);
			case CIRCLE:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case ELLIPSE:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case POLYGON:
				p = (Polygon) a;
				pp = (Polygon) b;
				p1.setVertices(p.getVertices());
				p2.setVertices(pp.getVertices());
				p1.setPosition(p.getX() + pa.x, p.getY() + pa.y);
				p2.setPosition(pp.getX() + pb.x, pp.getY() + pb.y);
				return Intersector.intersectPolygons(p1, p2, p3);
			case POLYLINE:
				p = (Polygon) a;
				l = (Polyline) b;

				p1.setVertices(p.getVertices());
				p1.setPosition(p.getX() + pa.x, p.getY() + pa.y);

				f2 = pb.x + l.getOriginX();
				f3 = pb.y + l.getOriginY();
				final float[] f = l.getVertices();
				if (f.length == 2)
					return c.contains(f[0] + f2, f[1] + f3); // degenerate point

				// check each line segment separately
				w3.x = f[f.length - 2] + f2;
				w3.y = f[f.length - 1] + f3;
				for (int i = f.length - 4; i >= 0; i -= 2) {
					w2.x = f[i] + f2;
					w2.y = f[i + 1] + f3;
					if (Intersector.intersectSegmentPolygon(w2, w3, p1))
						return true;
					w3.x = w2.x;
					w3.y = w2.y;
				}
				return false;
			case RECTANGLE:
				p = (Polygon) a;
				t = (Rectangle) b;

				p1.setVertices(p.getVertices());
				p1.setPosition(p.getX() + pa.x, p.getY() + pa.y);

				fa8[0] = t.x;
				fa8[1] = t.y;
				fa8[2] = t.x + t.width;
				fa8[3] = t.y;
				fa8[4] = t.x + t.width;
				fa8[5] = t.y + t.height;
				fa8[6] = t.x;
				fa8[7] = t.y + t.height;
				p2.setVertices(fa8);
				p2.setPosition(pb.x, pb.y);

				return Intersector.overlapConvexPolygons(p1, p2);
			default:
				LOG.debug("unknown shape:" + b);
				return false;
			}
		case POLYLINE:
			switch (sb) {
			case POINT:
				return a.contains(((Point) b).x + pb.x - pa.x, ((Point) b).y + pb.y - pa.x);
			case CIRCLE:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case ELLIPSE:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case POLYGON:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case POLYLINE:
				LOG.debug("collision polyline-polyline unsupported");
				return false;
			case RECTANGLE:
				l = (Polyline) a;
				f2 = pa.x + l.getOriginX();
				f3 = pa.y + l.getOriginY();
				final float[] f = l.getVertices();
				if (f.length == 2)
					return t.contains(f[0] + f2, f[1] + f3); // degenerate point

				// convert rectangle to polygon
				t = (Rectangle) b;
				fa8[0] = t.x;
				fa8[1] = t.y;
				fa8[2] = t.x + t.width;
				fa8[3] = t.y;
				fa8[4] = t.x + t.width;
				fa8[5] = t.y + t.height;
				fa8[6] = t.x;
				fa8[7] = t.y + t.height;
				p2.setVertices(fa8);
				p2.setPosition(pb.x, pb.y);

				// intersect polygon-polyline
				w3.x = f[f.length - 2] + f2;
				w3.y = f[f.length - 1] + f3;
				for (int i = f.length - 4; i >= 0; i -= 2) {
					w2.x = f[i] + f2;
					w2.y = f[i + 1] + f3;
					if (Intersector.intersectSegmentPolygon(w2, w3, p))
						return true;
					w3.x = w2.x;
					w3.y = w2.y;
				}

				return false;
			default:
				LOG.debug("unknown shape:" + b);
				return false;
			}
		case RECTANGLE:
			switch (sb) {
			case POINT:
				return a.contains(((Point) b).x + pb.x - pa.x, ((Point) b).y + pb.y - pa.x);
			case CIRCLE:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case ELLIPSE:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case POLYGON:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case POLYLINE:
				return GeoUtil.isCollision(b, a, sb, sa, pb, pa);
			case RECTANGLE:
				Rectangle r1 = (Rectangle) a;
				Rectangle r2 = (Rectangle) b;
				if (r1.x + r1.width + pa.x > r2.x + pb.x) {
					if (r1.x + pa.x < r2.x + r2.width + pb.x) {
						if (r1.y + r1.height + pa.y > r2.y + pb.y) {
							if (r1.y + pa.y < r2.y + r2.height + pb.y) {
								return true;
							}
						}
					}
				}
				return false;
			default:
				LOG.debug("unknown shape:" + b);
			}
		default:
			LOG.debug("unknown shape:" + a);
			return false;
		}
	}

	public static boolean boundingBoxVisible(ABoundingBoxComponent bbc, PositionComponent pc) {
		if (visibleWorldBoundingBox.x + visibleWorldBoundingBox.width >= bbc.minX + pc.x) {
			if (visibleWorldBoundingBox.x <= bbc.maxX + pc.x) {
				if (visibleWorldBoundingBox.y + visibleWorldBoundingBox.height >= bbc.minY + pc.y) {
					if (visibleWorldBoundingBox.y <= bbc.maxY + pc.y) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void rotatePoint(Vector2 point, Vector2 center, float degrees) {
		point.sub(center).rotate(degrees).add(center);
	}

	/**
	 * Rotates the rectangle and returns a polygon representing the rotated
	 * rectangle. The center of the rotation is the center of the shape's
	 * bounding box.
	 * 
	 * @param rect
	 *            The rectangle to be rotated.
	 * @param degrees
	 *            Angle of rotation.
	 * @return The polygon representing the rotated rectangle. Vertices are
	 *         counter-clockwise.
	 */
	public static Polygon getRotatedRectangle(Rectangle rect, float degrees) {
		float vertices[] = new float[8];
		w1.set(rect.x + 0.5f * rect.width, rect.y + 0.5f * rect.height);

		w2.set(rect.x, rect.y);
		rotatePoint(w2, w1, degrees);
		vertices[0] = w2.x;
		vertices[1] = w2.y;

		w2.set(rect.x + rect.width, rect.y);
		rotatePoint(w2, w1, degrees);
		vertices[2] = w2.x;
		vertices[3] = w2.y;

		w2.set(rect.x + rect.width, rect.y + rect.height);
		rotatePoint(w2, w1, degrees);
		vertices[4] = w2.x;
		vertices[5] = w2.y;

		w2.set(rect.x, rect.y + rect.height);
		rotatePoint(w2, w1, degrees);
		vertices[6] = w2.x;
		vertices[7] = w2.y;

		return new Polygon(vertices);
	}

	/**
	 * Rotates the polygon's vertices by the given amount. Modifies provided
	 * polygon. Unlike the rotate method of {@link Polygon}, this modifies the
	 * polygon's local vertices themselves.
	 * 
	 * @param p
	 *            Polygon to rotate.
	 * @param degrees
	 *            Angle of rotation.
	 */
	public static void rotatePolygon(Polygon p, Vector2 center, float degrees) {
		float v[] = p.getVertices();
		for (int i = 0; i < v.length; i += 2) {
			w1.set(v[i], v[i + 1]);
			rotatePoint(w1, center, degrees);
			v[i] = w1.x;
			v[i + 1] = w1.y;
		}
	}

	public static void boundingBoxCenter(Shape2D shape, Vector2 center) {
		if (shape instanceof Rectangle) {
			Rectangle r = (Rectangle) shape;
			center.x = r.x + 0.5f * r.width;
			center.y = r.y + 0.5f * r.height;
		} else if (shape instanceof Circle) {
			center.x = ((Circle) shape).x;
			center.y = ((Circle) shape).y;
		} else if (shape instanceof Ellipse) {
			Ellipse e = (Ellipse) shape;
			center.x = e.x + 0.5f * e.width;
			center.y = e.y + 0.5f * e.height;
		} else if (shape instanceof Polygon) {
			Polygon p = (Polygon) shape;
			Rectangle r = p.getBoundingRectangle();
			center.x = r.x + 0.5f * r.width;
			center.y = r.y + 0.5f * r.height;
		} else if (shape instanceof Polyline) {
			Polyline pl = (Polyline) shape;
			Polygon pp = new Polygon(pl.getVertices());
			Rectangle r = pp.getBoundingRectangle();
			center.x = r.x + 0.5f * r.width;
			center.y = r.y + 0.5f * r.height;
		}
	}

	public static void scaleShapeBy(ShapeComponent sc, float scaleX, float scaleY) {
		switch (sc.shapeType) {
		case CIRCLE:
			Circle c = (Circle) sc.shape;
			if (scaleX != scaleY) {
				sc.shape = new Ellipse(c);
				e.width *= scaleX;
				e.height *= scaleY;
			} else {
				c.radius *= scaleX;
			}
			break;
		case ELLIPSE:
			Ellipse e = (Ellipse) sc.shape;
			e.width *= scaleX;
			e.height *= scaleY;
			break;
		case POINT:
			// nothing to be done;
			break;
		case POLYGON:
			Polygon p = (Polygon) sc.shape;
			p.setScale(scaleX, scaleY);
			break;
		case POLYLINE:
			Polyline l = (Polyline) sc.shape;
			l.setScale(scaleX, scaleY);
			break;
		case RECTANGLE:
			Rectangle r = (Rectangle) sc.shape;
			r.width *= scaleX;
			r.height *= scaleY;
			break;
		default:
			break;
		}
	}

	public static Shape2D cloneShape(Shape2D shape) {
		switch (EShapeType.valueOf(shape)) {
		case CIRCLE:
			return new Circle((Circle) shape);
		case ELLIPSE:
			return new Ellipse((Ellipse) shape);
		case POINT:
			return new Point((Point) shape);
		case POLYGON:
			return new Polygon(((Polygon) shape).getTransformedVertices());
		case POLYLINE:
			return new Polyline(((Polyline) shape).getTransformedVertices());
		case RECTANGLE:
			return new Rectangle((Rectangle) shape);
		default:
			LOG.error("unkown shape type: " + shape);
			return shape;
		}
	}
}
