package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.VelocityFieldComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Computes the force applied to an object.
 * 
 * @author madgaksha
 *
 */
public class VelocityFieldSystem extends DisableIteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(VelocityFieldSystem.class);

	public VelocityFieldSystem() {
		this(DefaultPriority.velocityFieldSystem);
	}

	@SuppressWarnings("unchecked")
	public VelocityFieldSystem(int priority) {
		super(DisableIteratingSystem.all(VelocityComponent.class, VelocityFieldComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final VelocityComponent vc = Mapper.velocityComponent.get(entity);
		final VelocityFieldComponent vfc = Mapper.velocityFieldComponent.get(entity);
		vfc.field.apply(entity, vc);
	}
}