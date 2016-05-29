package de.homelab.madgaksha.entityengine.component;

import java.util.Arrays;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.entity.CallbackMaker;
import de.homelab.madgaksha.entityengine.entitysystem.DamageSystem;

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
	public final int[] painPointsDigits;

	public PainPointsComponent(){
		painPointsDigits = new int[DamageSystem.NUMBER_OF_DIGITS];
	}
	
	public PainPointsComponent(long maxPainPoints) {
		this();
		setup(maxPainPoints);
	}
	public PainPointsComponent(long painPoints, long maxPainPoints) {
		this();
		setup(painPoints, maxPainPoints);
	}
	public void setup(long maxPainPoints) {
		setup(DEFAULT_PAIN_POINTS, maxPainPoints);
	}
	public void setup(long painPoints, long maxPainPoints) {
		this.painPoints = painPoints;
		this.maxPainPoints = maxPainPoints;
		this.painPointsRatio = ((float)painPoints)/((float)maxPainPoints);
		updatePainPoints();
	}	
	
	@Override
	public void reset() {
		painPoints = DEFAULT_PAIN_POINTS;
		maxPainPoints = DEFAULT_MAX_PAIN_POINTS;
		painPointsRatio = DEFAULT_PAIN_POINTS_RATIO;
		Arrays.fill(painPointsDigits, 0);
	}
	
	public void updatePainPoints() {
		painPointsRatio = ((float)painPoints)/((float)maxPainPoints);
		if (painPoints < 0) painPoints = 0;
		if (painPoints > maxPainPoints) painPoints = maxPainPoints;

		painPointsRatio = ((float)painPoints) / ((float)maxPainPoints);
		
		long digit = painPoints;
		for (int i = painPointsDigits.length; i --> 0;) {
			painPointsDigits[i] = (int)(digit % 10L);
			digit /= 10;
		}
	}
	
}
