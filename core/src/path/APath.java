package path;

import com.badlogic.gdx.math.Vector2;

public abstract class APath {

	protected final Vector2 origin = new Vector2();
	public final float tmaxInverse;
	public final float tmax;
	public final boolean relative;

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
	public final Vector2 applyInternal(float alpha) {
		Vector2 v = new Vector2();
		apply(alpha, v);
		return v;
	}

	/**
	 * Returns the point at the given interval.
	 * 
	 * @param alpha
	 *            Value between 0 and 1.
	 * @param vector
	 *            Vector which will store the result.
	 */
	public abstract void apply(float t, Vector2 vector);

	/**
	 * Sets the origin. All paths are relative to this origin.
	 * 
	 * @param x
	 * @param y
	 */
	public void setOrigin(float x, float y) {
		origin.set(x, y);
	}
}
