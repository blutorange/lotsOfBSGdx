package de.homelab.madgaksha.lotsofbs.path;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.getter.GetVector2;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public abstract class APath implements Serializable, com.badlogic.gdx.utils.Json.Serializable {
	/** Initial version.*/
	private final static long serialVersionUID = 1L;
	private final static float MIN_DURATION = 0.01f;

	protected float tmax;
	protected boolean relative;

	@Transient protected Vector2 origin = new Vector2();
	@Transient protected float tmaxInverse;
	@Transient private boolean dirty = false;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(tmax);
		out.writeBoolean(relative);
	}
	@Override
	public void write(final Json json) {
		json.writeValue(tmax);
		json.writeValue(relative);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		origin = new Vector2();

		final float tmax = in.readFloat();
		if (tmax < MIN_DURATION) throw new InvalidDataException("tmax must be >= " + MIN_DURATION);
		this.tmax = tmax;

		relative = in.readBoolean();
		tmaxInverse = 1.0f / tmax;
		dirty = false;
	}
	@Override
	public void read(final Json json, final JsonValue jsonData) {
		final float tmax = json.readValue(Float.class, jsonData);
		if (tmax < MIN_DURATION) throw new SerializationException("tmax must be >= " + MIN_DURATION);
		this.tmax = tmax;
		relative = json.readValue(Boolean.class, jsonData);
		tmaxInverse = 1.0f / tmax;
	}

	public APath() {
		tmax = tmaxInverse = 1f;
		relative = false;
	}

	public APath(final float tmax, final boolean relative) {
		tmaxInverse = 1.0f / tmax;
		this.tmax = tmax;
		this.relative = relative;
	}

	/**
	 * Returns the point at the given interval. Creates a new vector.
	 *
	 * @param alpha
	 *            Value between 0 and 1.
	 */
	protected abstract void applyInternal(float alpha, Vector2 vector);

	/**
	 * When origin or anything else changes, this method gets called so that the
	 * implementing path can update its parameters.
	 */
	protected abstract void computeParamters();

	/**
	 * Returns the point at the given interval.
	 *
	 * @param t
	 *            Value between 0 and 1.
	 * @param vector
	 *            Vector which will store the result.
	 */
	public void apply(final float alpha, final Vector2 vector) {
		if (dirty)
			computeParamters();
		applyInternal(alpha, vector);
	}

	/**
	 * Returns the point at the given interval.
	 *
	 * @param totalTime
	 *            Value between 0 and tmax.
	 * @param vector
	 *            Vector which will store the result.
	 */
	public void applyTotal(final float totalTime, final Vector2 vector) {
		apply(Math.min(totalTime, tmax) * tmaxInverse, vector);
	}

	/**
	 * @param totalTime
	 *            Value between 0 and tmax.
	 * @param vector
	 *            Vector which will store the result.
	 * @param interpolation
	 *            Interpolation scheme to use.
	 */
	public void applyWithInterpolation(final float totalTime, final Vector2 vector, final Interpolation interpolation) {
		apply(interpolation.apply(totalTime * tmaxInverse), vector);
	}

	/**
	 * Sets the origin. All paths are relative to this origin.
	 *
	 * @param x
	 * @param y
	 */
	public void setOrigin(final float x, final float y) {
		origin.set(x, y);
		dirty = true;
	}

	public void setOrigin(final Vector2 pos) {
		origin.set(pos);
		dirty = true;
	}

	public void setOrigin(final GetVector2 getOrigin) {
		getOrigin.as(origin);
		dirty = true;
	}

	public void setTMax(final float tmax) throws IllegalArgumentException {
		if (tmax < MIN_DURATION) throw new IllegalArgumentException("tmax must be >= " + MIN_DURATION);
		this.tmax = tmax;
		tmaxInverse = 1.0f / tmax;
		dirty = true;
	}

	public float getTMax() {
		return tmax;
	}

	public float getOriginX() {
		return origin.x;
	}

	public float getOriginY() {
		return origin.y;
	}
}
