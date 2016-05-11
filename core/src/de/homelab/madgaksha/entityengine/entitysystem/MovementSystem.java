package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;

/**
 * Updates an object's position its velocity over a small time step dt. 
 * @author madgaksha
 */
public class MovementSystem extends IteratingSystem {

	public MovementSystem() {
		this(DefaultPriority.movementSystem);
	}
	
	@SuppressWarnings("unchecked")
	public MovementSystem(int priority) {
		super(Family.all(PositionComponent.class, VelocityComponent.class).get(), priority);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PositionComponent r = Mapper.positionComponent.get(entity);
		final VelocityComponent v = Mapper.velocityComponent.get(entity);
		final TimeScaleComponent tsf = Mapper.timeScaleComponent.get(entity);
		if (tsf != null) deltaTime *= tsf.timeScalingFactor;
		r.x += v.x * deltaTime;
		r.y += v.y * deltaTime;
		r.z += v.z * deltaTime;
	}
	
}
