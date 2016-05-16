package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;

public class GrantPositionSystem extends IteratingSystem {

	public GrantPositionSystem() {
		this(DefaultPriority.grantPositionSystem);
	}

	@SuppressWarnings("unchecked")
	public GrantPositionSystem(int priority) {
		super(Family.all(TemporalComponent.class, PositionComponent.class, ShouldPositionComponent.class).get(), priority);
	}
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		final ShouldPositionComponent spc = Mapper.shouldPositionComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		pc.x = spc.grantStrategy.compromise(pc.x, spc.x, deltaTime);
		pc.y = spc.grantStrategy.compromise(pc.y, spc.y, deltaTime);
		pc.z = spc.grantStrategy.compromise(pc.z, spc.z, deltaTime);
	}

}
