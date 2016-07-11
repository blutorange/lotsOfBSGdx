package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ForceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Updates velocity from the force applied to a component and its mass.
 * 
 * @author madgaksha
 *
 */
public class AccelerationSystem extends DisableIteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AccelerationSystem.class);

	public AccelerationSystem() {
		this(DefaultPriority.accelerationSystem);
	}

	@SuppressWarnings("unchecked")
	public AccelerationSystem(int priority) {
		super(DisableIteratingSystem.all(TemporalComponent.class, VelocityComponent.class, ForceComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final VelocityComponent v = Mapper.velocityComponent.get(entity);
		final ForceComponent f = Mapper.forceComponent.get(entity);
		deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
		v.x += f.x * deltaTime;
		v.y += f.y * deltaTime;
	}

}
