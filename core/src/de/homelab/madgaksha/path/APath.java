package de.homelab.madgaksha.path;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.getter.GetVector2;

public abstract class APath {

	protected final Vector2 origin = new Vector2();
	protected final float tmaxInverse;
	protected final float tmax;
	protected final boolean relative;
	private boolean dirty = false;

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
	 * When origin or anything else changes, this method gets called
	 * so that the implementing path can update its parameters.
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
		if (dirty) computeParamters();
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
		apply(Math.min(totalTime,tmax)*tmaxInverse, vector);
	}
	
	/**
	 * @param totalTime Value between 0 and tmax.
	 * @param vector Vector which will store the result.
	 * @param interpolation Interpolation scheme to use.
	 */
	public void applyWithInterpolation(float totalTime, Vector2 vector, Interpolation interpolation) {
		apply(interpolation.apply(totalTime*tmaxInverse), vector);
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
