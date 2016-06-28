package de.homelab.madgaksha.path;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

import de.homelab.madgaksha.logging.Logger;

public class PathLine extends APath {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PathLine.class);

	private final float x;
	private final float y;

	/**
	 * Sets the point to which we line to.
	 * 
	 * @param x
	 * @param y
	 */
	public PathLine(float tmax, boolean relative, float x, float y) {
		super(tmax, relative);
		this.x = x;
		this.y = y;
	}

	@Override
	public void applyInternal(float t, Vector2 vector) {
		if (relative)
			vector.set(origin.x + t * x, origin.y + t * y);
		else
			vector.set(origin.x + t * (x - origin.x), origin.y + t * (y - origin.y));
	}

	@Override
	public String toString() {
		return new StringBuilder().append("PathLine(").append(tmax).append("s,").append(x).append(",").append(y)
				.append(",relative=").append(relative).append(")").toString();
	}

	@Override
	protected void computeParamters() {
	}
}
