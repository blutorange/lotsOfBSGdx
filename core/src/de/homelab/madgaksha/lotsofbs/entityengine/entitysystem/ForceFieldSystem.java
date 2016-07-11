package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ForceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ForceFieldComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Computes the force applied to an object.
 * 
 * @author madgaksha
 *
 */
public class ForceFieldSystem extends IteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ForceFieldSystem.class);

	public ForceFieldSystem() {
		this(DefaultPriority.forceFieldSystem);
	}

	@SuppressWarnings("unchecked")
	public ForceFieldSystem(int priority) {
		super(Family.all(ForceComponent.class, ForceFieldComponent.class).exclude(InactiveComponent.class).get(),
				priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final ForceComponent fc = Mapper.forceComponent.get(entity);
		final ForceFieldComponent ffc = Mapper.forceFieldComponent.get(entity);
		ffc.field.apply(entity, fc);
	}
}
