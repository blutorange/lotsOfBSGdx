package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;

public class GrantRotationSystem extends IteratingSystem {

	public GrantRotationSystem() {
		this(DefaultPriority.grantPositionSystem);
	}

	@SuppressWarnings("unchecked")
	public GrantRotationSystem(int priority) {
		super(Family.all(TemporalComponent.class, RotationComponent.class, ShouldRotationComponent.class).exclude(InactiveComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final RotationComponent rc = Mapper.rotationComponent.get(entity);
		final ShouldRotationComponent src = Mapper.shouldRotationComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		// Make sure we rotate in the direction of the lesser angle...
		if (Math.abs(src.thetaZ-rc.thetaZ) > 180.0f) {
			if (src.thetaZ < rc.thetaZ) src.thetaZ += 360.0;
			else rc.thetaZ += 360.0f;
		}
		rc.thetaZ = src.grantStrategy.compromise(rc.thetaZ, src.thetaZ, deltaTime)%360.0f;
		rc.centerX = src.grantStrategy.compromise(rc.centerX, src.centerX, deltaTime);
		rc.centerY = src.grantStrategy.compromise(rc.centerY, src.centerY, deltaTime);
	}
}
