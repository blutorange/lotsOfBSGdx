package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Updates an object's direction.
 *
 * @author madgaksha
 */
public class AngularMovementSystem extends DisableIteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AngularMovementSystem.class);

	public AngularMovementSystem() {
		this(DefaultPriority.movementSystem);
	}

	public AngularMovementSystem(final int priority) {
		super(DisableIteratingSystem.all(AngularVelocityComponent.class, TemporalComponent.class)
				.one(DirectionComponent.class, RotationComponent.class).exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(final Entity entity, float deltaTime) {
		final DirectionComponent dc = Mapper.directionComponent.get(entity);
		final RotationComponent rc = Mapper.rotationComponent.get(entity);
		final AngularVelocityComponent avc = Mapper.angularVelocityComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;

		if (dc != null)
			dc.degree += avc.speed * deltaTime;
		if (rc != null)
			rc.thetaZ += avc.speed * deltaTime;
	}

}
