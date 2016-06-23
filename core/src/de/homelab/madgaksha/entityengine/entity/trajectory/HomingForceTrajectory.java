package de.homelab.madgaksha.entityengine.entity.trajectory;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ForceComponent;
import de.homelab.madgaksha.entityengine.component.ForceFieldComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.entity.BulletTrajectoryMaker;
import de.homelab.madgaksha.entityengine.entity.IBehaving;
import de.homelab.madgaksha.forcefield.HomingForceField;
import de.homelab.madgaksha.logging.Logger;

public class HomingForceTrajectory extends BulletTrajectoryMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(HomingForceTrajectory.class);

	private final static Vector2 v = new Vector2();
	private final static Vector2 w = new Vector2();

	private PositionComponent targetPositionComponent = null;
	private boolean ignoreTrackOffset = false;
	private float springConstant = 1.0f;
	private float friction = 0.05f;
	private float absorptionRadiusSquared = 64.0f * 64.0f;

	public HomingForceTrajectory() {
		super();
	}

	public void target(PositionComponent pc) {
		this.targetPositionComponent = pc;
	}

	public void ignoreTrackOffset(boolean ignoreTrackOffset) {
		this.ignoreTrackOffset = ignoreTrackOffset;
	}

	public void attraction(float attraction) {
		this.springConstant = attraction;
	}

	public void friction(float friction) {
		this.friction = friction;
	}

	public void absorptionRadius(float absorptionRadius) {
		this.absorptionRadiusSquared = absorptionRadius * absorptionRadius;
	}

	@Override
	protected void setup(Entity e) {
		super.setup(e);

		final ForceComponent fc = gameEntityEngine.createComponent(ForceComponent.class);
		final ForceFieldComponent ffc = gameEntityEngine.createComponent(ForceFieldComponent.class);

		ffc.field = new HomingForceField(targetPositionComponent, springConstant);
		ffc.ignoreOffset = ignoreTrackOffset;

		e.add(fc).add(ffc);
	}

	@Override
	public IBehaving getBehaviour() {
		return new IBehaving() {
			@Override
			public boolean behave(Entity bullet) {
				final PositionComponent pcBullet = Mapper.positionComponent.get(bullet);
				final VelocityComponent vc = Mapper.velocityComponent.get(bullet);
				final ForceFieldComponent ffc = Mapper.forceFieldComponent.get(bullet);
				final HomingForceField hfc = (HomingForceField) ffc.field;
				// Get velocity component orthogonal to the bullet-target
				// vector.
				v.set(pcBullet.x - hfc.target.x, pcBullet.y - hfc.target.y);
				final float len2 = v.len2();
				if (v.len2() < absorptionRadiusSquared) {
					bullet.remove(ForceComponent.class);
					vc.x = -v.x;
					vc.y = -v.y;
				} else {
					v.scl(1.0f / (float) Math.sqrt(len2));
					w.set(vc.x, vc.y);
					v.scl(w.dot(v));
					w.sub(v);
					// Apply friction.
					vc.x -= friction * w.x;
					vc.y -= friction * w.y;
				}
				return true;
			}
		};
	}
}