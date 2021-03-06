package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;

public class GrantRotationSystem extends DisableIteratingSystem {

	public GrantRotationSystem() {
		this(DefaultPriority.grantPositionSystem);
	}

	public GrantRotationSystem(final int priority) {
		super(DisableIteratingSystem
				.all(TemporalComponent.class, RotationComponent.class, ShouldRotationComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(final Entity entity, float deltaTime) {
		final RotationComponent rc = Mapper.rotationComponent.get(entity);
		final ShouldRotationComponent src = Mapper.shouldRotationComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		// Make sure we rotate in the direction of the lesser angle...
		if (Math.abs(src.thetaZ - rc.thetaZ) > 180.0f) {
			if (src.thetaZ < rc.thetaZ)
				src.thetaZ += 360.0;
			else
				rc.thetaZ += 360.0f;
		}
		rc.thetaZ = src.grantStrategy.compromise(rc.thetaZ, src.thetaZ, deltaTime) % 360.0f;
		rc.centerX = src.grantStrategy.compromise(rc.centerX, src.centerX, deltaTime);
		rc.centerY = src.grantStrategy.compromise(rc.centerY, src.centerY, deltaTime);
	}
}
