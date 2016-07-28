package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;

public class GrantPositionSystem extends DisableIteratingSystem {

	public GrantPositionSystem() {
		this(DefaultPriority.grantPositionSystem);
	}

	@SuppressWarnings("unchecked")
	public GrantPositionSystem(int priority) {
		super(DisableIteratingSystem
				.all(TemporalComponent.class, PositionComponent.class, ShouldPositionComponent.class)
				.exclude(InactiveComponent.class), priority);
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
		if (spc.removeOnFulfill)
			if (spc.x == pc.x && spc.y == pc.y && spc.z == pc.z)
				entity.remove(ShouldPositionComponent.class);
	}
}
