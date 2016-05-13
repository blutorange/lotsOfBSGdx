package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;

public class GrantRotationSystem extends IteratingSystem {

	public GrantRotationSystem() {
		this(DefaultPriority.grantPositionSystem);
	}

	@SuppressWarnings("unchecked")
	public GrantRotationSystem(int priority) {
		super(Family.all(RotationComponent.class, ShouldRotationComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final RotationComponent rc = Mapper.rotationComponent.get(entity);
		final ShouldRotationComponent src = Mapper.shouldRotationComponent.get(entity);
		rc.thetaZ = src.grantStrategy.compromise(rc.thetaZ, src.thetaZ, deltaTime);
		rc.centerX = src.grantStrategy.compromise(rc.centerX, src.centerX, deltaTime);
		rc.centerY = src.grantStrategy.compromise(rc.centerY, src.centerY, deltaTime);
	}
}
