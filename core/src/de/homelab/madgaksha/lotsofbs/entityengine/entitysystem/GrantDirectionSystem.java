package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldDirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;

public class GrantDirectionSystem extends DisableIteratingSystem {

	public GrantDirectionSystem() {
		this(DefaultPriority.grantDirectionSystem);
	}

	@SuppressWarnings("unchecked")
	public GrantDirectionSystem(int priority) {
		super(DisableIteratingSystem
				.all(TemporalComponent.class, DirectionComponent.class, ShouldDirectionComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		final DirectionComponent dc = Mapper.directionComponent.get(entity);
		final ShouldDirectionComponent sdc = Mapper.shouldDirectionComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		dc.degree = sdc.grantStrategy.compromise(dc.degree, sdc.degree, deltaTime);
	}
}
