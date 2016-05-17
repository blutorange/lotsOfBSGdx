package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TimeScaleComponent;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class TemporalSystem extends IteratingSystem {
	
	public TemporalSystem() {
		this(DefaultPriority.temporalSystem);
	}

	@SuppressWarnings("unchecked")
	public TemporalSystem(int priority) {
		super(Family.all(TemporalComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final TemporalComponent tc = Mapper.temporalComponent.get(entity);
		final TimeScaleComponent tsfc = Mapper.timeScaleComponent.get(entity);
		tc.deltaTime = deltaTime;
		if (tsfc != null) tc.deltaTime *= tsfc.timeScalingFactor;
		tc.totalTime += tc.deltaTime;
	}
}
