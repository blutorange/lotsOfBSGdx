package de.homelab.madgaksha.lotsofbs.path;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.getter.GetVector2;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public abstract class APath implements Serializable {
	/** Initial version.*/
	private final static long serialVersionUID = 1L;
	private final static float MIN_DURATION = 0.01f;
	
	protected float tmax;
	protected boolean relative;

	@Transient protected Vector2 origin = new Vector2();
	@Transient protected float tmaxInverse;	
	@Transient private boolean dirty = false;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(tmax);
		out.writeBoolean(relative);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		origin = new Vector2();
		
		final float tmax = in.readFloat();
		if (tmax < MIN_DURATION) throw new InvalidDataException("tmax must be >= " + MIN_DURATION);
		this.tmax = tmax;
		
		relative = in.readBoolean();
		tmaxInverse = 1.0f / tmax;
		dirty = false;
	}
	
	public APath(float tmax, boolean relative) {
		this.tmaxInverse = 1.0f / tmax;
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
	public void apply(float alpha, Vector2 vector) {
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
	public void applyTotal(float totalTime, Vector2 vector) {
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
	public void applyWithInterpolation(float totalTime, Vector2 vector, Interpolation interpolation) {
		apply(interpolation.apply(totalTime * tmaxInverse), vector);
	}

	/**
	 * Sets the origin. All paths are relative to this origin.
	 * 
	 * @param x
	 * @param y
	 */
	public void setOrigin(float x, float y) {
		origin.set(x, y);
		dirty = true;
	}

	public void setOrigin(Vector2 pos) {
		origin.set(pos);
		dirty = true;
	}

	public void setOrigin(GetVector2 getOrigin) {
		getOrigin.as(this.origin);
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
