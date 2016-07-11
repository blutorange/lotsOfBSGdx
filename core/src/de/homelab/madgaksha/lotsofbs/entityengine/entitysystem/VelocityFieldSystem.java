package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityFieldComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

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