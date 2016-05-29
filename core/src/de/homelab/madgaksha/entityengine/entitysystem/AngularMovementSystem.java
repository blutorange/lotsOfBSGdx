package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates an object's direction.
 * 
 * @author madgaksha
 */
public class AngularMovementSystem extends IteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AngularMovementSystem.class);

	public AngularMovementSystem() {
		this(DefaultPriority.movementSystem);
	}

	@SuppressWarnings("unchecked")
	public AngularMovementSystem(int priority) {
		super(Family.all(AngularVelocityComponent.class, TemporalComponent.class).one(DirectionComponent.class, RotationComponent.class).exclude(InactiveComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final DirectionComponent dc = Mapper.directionComponent.get(entity);
		final RotationComponent rc = Mapper.rotationComponent.get(entity);
		final AngularVelocityComponent avc = Mapper.angularVelocityComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		
		if (dc != null) dc.degree += avc.speed*deltaTime;
		if (rc != null) rc.thetaZ += avc.speed*deltaTime;
	}

}
