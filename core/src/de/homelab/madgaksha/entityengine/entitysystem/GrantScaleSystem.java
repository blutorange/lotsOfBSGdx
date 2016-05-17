package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;

public class GrantScaleSystem extends IteratingSystem {

	public GrantScaleSystem() {
		this(DefaultPriority.grantScaleSystem);
	}

	@SuppressWarnings("unchecked")
	public GrantScaleSystem(int priority) {
		super(Family.all(ScaleComponent.class, ShouldScaleComponent.class, TemporalComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final ScaleComponent sc = Mapper.scaleComponent.get(entity);
		final ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		sc.scaleX = sc.scaleY = ssc.grantStrategy.compromise(sc.scaleX, ssc.scaleX, deltaTime);
		if (ssc.scaleX != ssc.scaleY) sc.scaleY = ssc.grantStrategy.compromise(sc.scaleY, ssc.scaleY, deltaTime);
	}
}
