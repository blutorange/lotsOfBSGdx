package de.homelab.madgaksha.lotsofbs.path;

import java.io.IOException;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.StringBuilder;
import com.sun.media.sound.InvalidDataException;

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
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PathParabola.class);

	private final static float TAU_MIN = 0.001f;
	private final static float TAU_MAX = 0.999f;

	private Vector2 p2 = new Vector2();
	private Vector2 p1 = new Vector2();
	private Vector2 p0 = new Vector2();

	private Vector2 r2 = new Vector2();
	private Vector2 r1 = new Vector2();
	private Vector2 r0 = new Vector2();

	private float tau;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(p0.x);
		out.writeFloat(p0.y);
		out.writeFloat(p1.x);
		out.writeFloat(p1.y);
		out.writeFloat(p2.x);
		out.writeFloat(p2.y);
		out.writeFloat(r0.x);
		out.writeFloat(r0.y);
		out.writeFloat(r1.x);
		out.writeFloat(r1.y);
		out.writeFloat(r2.x);
		out.writeFloat(r2.y);
		out.writeFloat(tau);
	}
	@Override
	public void write(final Json json) {
		super.write(json);
		json.writeValue(p0.x);
		json.writeValue(p0.y);
		json.writeValue(p1.x);
		json.writeValue(p1.y);
		json.writeValue(p2.x);
		json.writeValue(p2.y);
		json.writeValue(r0.x);
		json.writeValue(r0.y);
		json.writeValue(r1.x);
		json.writeValue(r1.y);
		json.writeValue(r2.x);
		json.writeValue(r2.y);
		json.writeValue(r0.x);
		json.writeValue(tau);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		p2 = new Vector2();
		p1 = new Vector2();
		p0 = new Vector2();
		r0 = new Vector2();
		r1 = new Vector2();
		r2 = new Vector2();
		final float p0x = in.readFloat();
		final float p0y = in.readFloat();
		final float p1x = in.readFloat();
		final float p1y = in.readFloat();
		final float p2x = in.readFloat();
		final float p2y = in.readFloat();
		final float r0x = in.readFloat();
		final float r0y = in.readFloat();
		final float r1x = in.readFloat();
		final float r1y = in.readFloat();
		final float r2x = in.readFloat();
		final float r2y = in.readFloat();
		final float tau = in.readFloat();

		if (tau < TAU_MIN || tau > TAU_MAX) throw new InvalidDataException("tau must be between " + TAU_MIN + " and " + TAU_MAX);

		this.tau = tau;
		p0.set(p0x, p0y);
		p1.set(p1x, p1y);
		p2.set(p2x, p2y);
		r0.set(r0x, r0y);
		r1.set(r1x, r1y);
		r2.set(r2x, r2y);
	}
	@Override
	public void read(final Json json, final JsonValue jsonData) {
		super.read(json, jsonData);
		final float p0x = json.readValue(Float.class, jsonData);
		final float p0y = json.readValue(Float.class, jsonData);
		final float p1x = json.readValue(Float.class, jsonData);
		final float p1y = json.readValue(Float.class, jsonData);
		final float p2x = json.readValue(Float.class, jsonData);
		final float p2y = json.readValue(Float.class, jsonData);
		final float r0x = json.readValue(Float.class, jsonData);
		final float r0y = json.readValue(Float.class, jsonData);
		final float r1x = json.readValue(Float.class, jsonData);
		final float r1y = json.readValue(Float.class, jsonData);
		final float r2x = json.readValue(Float.class, jsonData);
		final float r2y = json.readValue(Float.class, jsonData);
		final float tau = json.readValue(Float.class, jsonData);
		if (tau < TAU_MIN || tau > TAU_MAX) throw new SerializationException("tau must be between " + TAU_MIN + " and " + TAU_MAX);
		this.tau = tau;
		p0.set(p0x, p0y);
		p1.set(p1x, p1y);
		p2.set(p2x, p2y);
		r0.set(r0x, r0y);
		r1.set(r1x, r1y);
		r2.set(r2x, r2y);
	}

	public PathParabola(final float tmax, final boolean relative, final float tau, final float x1, final float y1, final float x2, final float y2) {
		super(tmax, relative);
		r0.set(origin);
		r1.set(x1, y1);
		r2.set(x2, y2);
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

		final float c = r0.x;
		final float f = r0.y;

		final float m = 1.0f / (tau * (1.0f - tau));
		final float b = m * (r1.x - r0.x - tau * tau * (r2.x - r0.x));
		final float e = m * (r1.y - r0.y - tau * tau * (r2.y - r0.y));

		final float a = r2.x - r0.x - b;
		final float d = r2.y - r0.y - e;

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
	public void applyInternal(final float t, final Vector2 vector) {
		vector.set(p2).scl(t * t).add(p0).add(p1.x * t, p1.y * t);
	}

	@Override
	public String toString() {
		return new StringBuilder().append("PathLine(").append(tmax).append("s,").append(r0).append(",").append(r1)
				.append(",").append(r2).append(",relative=").append(relative).append(")").toString();
	}
}
