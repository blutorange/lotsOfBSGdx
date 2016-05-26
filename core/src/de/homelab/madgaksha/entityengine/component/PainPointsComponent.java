package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.CallbackMaker;

/**
 * Contains information for {@link CallbackMaker} entities.
 * @author madgaksha
 *
 */
public class PainPointsComponent implements Component, Poolable {
	private static final long DEFAULT_MAX_PAIN_POINTS = 10L;
	private static final long DEFAULT_PAIN_POINTS = 0L;
	private static final float DEFAULT_PAIN_POINTS_RATIO = ((float)DEFAULT_PAIN_POINTS) / ((float)DEFAULT_MAX_PAIN_POINTS);
	
	public long maxPainPoints = DEFAULT_MAX_PAIN_POINTS;
	public long painPoints = DEFAULT_PAIN_POINTS;
	public float painPointsRatio = DEFAULT_PAIN_POINTS_RATIO;
	
	public PainPointsComponent(){
	}
	
	public PainPointsComponent(long maxPainPoints) {
		setup(maxPainPoints);
	}
	public PainPointsComponent(long painPoints, long maxPainPoints) {
		setup(painPoints, maxPainPoints);
	}
	public void setup(long maxPainPoints) {
		setup(DEFAULT_PAIN_POINTS, maxPainPoints);
	}
	public void setup(long painPoints, long maxPainPoints) {
		this.painPoints = painPoints;
		this.maxPainPoints = maxPainPoints;
		this.painPointsRatio = ((float)painPoints)/((float)maxPainPoints);
	}	
	
	@Override
	public void reset() {
		painPoints = DEFAULT_PAIN_POINTS;
		maxPainPoints = DEFAULT_MAX_PAIN_POINTS;
		painPointsRatio = DEFAULT_PAIN_POINTS_RATIO;
	}
}
