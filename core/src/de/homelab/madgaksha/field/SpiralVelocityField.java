package de.homelab.madgaksha.field;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.logging.Logger;

public class SpiralVelocityField implements IVelocityField {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SpiralVelocityField.class);
	private float speedRadial;
	private float speedTangential;
	private float minDistance;
	private float changeDirectionChance;
	private PositionComponent target;

	public SpiralVelocityField(PositionComponent target, float speedRadial, float speedTangential, float minDistance) {
		this(target, speedRadial, speedTangential, minDistance, 0.0f);
	}

	public SpiralVelocityField(PositionComponent target, float speedRadial, float speedTangential, float minDistance,
			float changeDirectionChance) {
		this.speedRadial = speedRadial;
		this.speedTangential = speedTangential;
		this.minDistance = minDistance;
		this.target = target;
		this.changeDirectionChance = changeDirectionChance;
	}

	@Override
	public void apply(Entity e, VelocityComponent vc) {
		PositionComponent pc = Mapper.positionComponent.get(e);
		TemporalComponent tc = Mapper.temporalComponent.get(e);
		// For small probabilities p, 1-(1-p/n)^n ~ p
		// Thus the result is about the same independent of whether
		// we check once per second or twice per second
		// at half the probability etc.
		if (MathUtils.randomBoolean(tc.deltaTime * changeDirectionChance))
			speedTangential = -speedTangential;
		float vx = target.x - pc.x;
		float vy = target.y - pc.y;
		float len = (float) Math.sqrt(vx * vx + vy * vy);
		if (len < minDistance)
			return;
		vx /= len;
		vy /= len;
		vc.x = speedRadial * vx + speedTangential * vy;
		vc.y = speedRadial * vy - speedTangential * vx;
	}
}
