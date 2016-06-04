package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ForceComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates velocity from the force applied to a component and its mass.
 * 
 * @author madgaksha
 *
 */
public class AccelerationSystem extends IteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AccelerationSystem.class);

	public AccelerationSystem() {
		this(DefaultPriority.accelerationSystem);
	}

	@SuppressWarnings("unchecked")
	public AccelerationSystem(int priority) {
		super(Family.all(TemporalComponent.class, VelocityComponent.class, ForceComponent.class)
				.exclude(InactiveComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final VelocityComponent v = Mapper.velocityComponent.get(entity);
		final ForceComponent f = Mapper.forceComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		v.x += f.x * deltaTime;
		v.y += f.y * deltaTime;
		v.y += f.y * deltaTime;
	}

}
