package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ForceComponent;
import de.homelab.madgaksha.entityengine.component.InverseMassComponent;
import de.homelab.madgaksha.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;

/**
 * Updates velocity from the force applied to a component and its mass.
 * 
 * @author madgaksha
 *
 */
public class NewtonianForceSystem extends IteratingSystem {

	public NewtonianForceSystem() {
		this(DefaultPriority.newtonianForceSystem);
	}

	@SuppressWarnings("unchecked")
	public NewtonianForceSystem(int priority) {
		super(Family.all(VelocityComponent.class, ForceComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final VelocityComponent v = Mapper.velocityComponent.get(entity);
		final ForceComponent f = Mapper.forceComponent.get(entity);
		final TimeScaleComponent tsf = Mapper.timeScaleComponent.get(entity);
		if (tsf != null)
			deltaTime *= tsf.timeScalingFactor;
		v.x += f.x * deltaTime;
		v.y += f.y * deltaTime;
		v.y += f.y * deltaTime;
	}

}
