package de.homelab.madgaksha.lotsofbs.path;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class PathLine extends APath {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PathLine.class);

	private float x;
	private float y;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(x);
		out.writeFloat(y);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		x = in.readFloat();
		y = in.readFloat();
	}

	
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
