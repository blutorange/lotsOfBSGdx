package de.homelab.madgaksha.lotsofbs.path;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Parabola with the following equation:
 * 
 * <pre>
 * x(t) = a*t^2+b*t+c
 * y(t) = d*t^2+e*t+f
 * </pre>
 * 
 * The coefficient are determined by the following contraints:
 * 
 * <pre>
 * x(0) = x0
 * y(0) = y0
 * x(tau) = x1
 * y(tau) = y1
 * x(1) = x2
 * y(1) = y2
 * </pre>
 * 
 * Whereas tau must be a value between 0.0 and 1.0 (exclusive).
 * 
 * <br>
 * <br>
 * 
 * It follows that
 * 
 * <pre>
 * c = x0
 * b = 1/(tau*(1-tau)) * (x1-x0-tau^2*(x2-x0))
 * a = x2 - x0 - b
 * 
 * f = y0
 * e = 1/(tau*(1-tau)) * (y1-y0-tau^2*(y2-y0))
 * d = y2 - y0 - e
 * </pre>
 * 
 * @author madgaksha
 *
 */
public class PathParabola extends APath {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PathParabola.class);

	private final static float TAU_MIN = 0.0001f;
	private final static float TAU_MAX = 0.9999f;

	private final Vector2 p2 = new Vector2();
	private final Vector2 p1 = new Vector2();
	private final Vector2 p0 = new Vector2();

	private final Vector2 r2 = new Vector2();
	private final Vector2 r1 = new Vector2();
	private final Vector2 r0 = new Vector2();

	private final float tau;

	public PathParabola(float tmax, boolean relative, float tau, float x1, float y1, float x2, float y2) {
		super(tmax, relative);
		this.r0.set(origin);
		this.r1.set(x1, y1);
		this.r2.set(x2, y2);
		// Sanitize tau, we get infinity when tau is either 0 or 1.
		this.tau = MathUtils.clamp(tau, TAU_MIN, TAU_MAX);
		computeParamters();
	}

	@Override
	protected void computeParamters() {
		r0.set(origin);

		if (relative) {
			r1.add(origin);
			r2.add(origin);
		}

		float c = r0.x;
		float f = r0.y;

		float m = 1.0f / (tau * (1.0f - tau));
		float b = m * (r1.x - r0.x - tau * tau * (r2.x - r0.x));
		float e = m * (r1.y - r0.y - tau * tau * (r2.y - r0.y));

		float a = r2.x - r0.x - b;
		float d = r2.y - r0.y - e;

		// float m = 1.0f / (tau * (1.0f - tau));
		// p0.set(r0);
		// p1.set(r2).sub(r0).scl(-tau*tau).add(r1).sub(r0).scl(m);
		// p2.set(r2).sub(r0).sub(p1);

		if (relative) {
			r1.sub(origin);
			r2.sub(origin);
		}

		p2.set(a, d);
		p1.set(b, e);
		p0.set(c, f);
	}

	@Override
	public void applyInternal(float t, Vector2 vector) {
		vector.set(p2).scl(t * t).add(p0).add(p1.x * t, p1.y * t);
	}

	@Override
	public String toString() {
		return new StringBuilder().append("PathLine(").append(tmax).append("s,").append(r0).append(",").append(r1)
				.append(",").append(r2).append(",relative=").append(relative).append(")").toString();
	}
}
