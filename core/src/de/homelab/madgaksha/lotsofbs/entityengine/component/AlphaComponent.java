package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 
 * @author mad_gaksha
 */
public class AlphaComponent implements Component, Poolable {
	private final static float DEFAULT_ALPHA = 1.0f;

	public float alpha = DEFAULT_ALPHA;

	public AlphaComponent() {
	}

	public AlphaComponent(float alpha) {
		setup(alpha);
	}

	public void setup(float alpha) {
		this.alpha = alpha;
	}

	@Override
	public void reset() {
		alpha = DEFAULT_ALPHA;
	}

}
