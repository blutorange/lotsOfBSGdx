package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;

public class GrantPositionSystem extends IteratingSystem {

	public GrantPositionSystem() {
		this(DefaultPriority.grantPositionSystem);
	}

	@SuppressWarnings("unchecked")
	public GrantPositionSystem(int priority) {
		super(Family.all(TemporalComponent.class, PositionComponent.class, ShouldPositionComponent.class)
				.exclude(InactiveComponent.class).get(), priority);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		final ShouldPositionComponent spc = Mapper.shouldPositionComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		final Vector2 v = spc.grantStrategy.compromise2D(pc.x, pc.y, spc.x, spc.y, deltaTime);
		pc.x = v.x;
		pc.y = v.y;
		pc.z = spc.grantStrategy.compromise(pc.z, spc.z, deltaTime);
		if (spc.grantOffset) {
			pc.offsetX = spc.grantStrategy.compromise(pc.offsetX, spc.offsetX, deltaTime);
			pc.offsetY = spc.grantStrategy.compromise(pc.offsetY, spc.offsetY, deltaTime);
			pc.offsetZ = spc.grantStrategy.compromise(pc.offsetZ, spc.offsetZ, deltaTime);
		}
	}

}
