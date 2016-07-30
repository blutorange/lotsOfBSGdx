package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.BehaviourComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class AiSystem extends DisableIteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AiSystem.class);

	public AiSystem() {
		this(DefaultPriority.aiSystem);
	}

	@SuppressWarnings("unchecked")
	public AiSystem(int priority) {
		super(DisableIteratingSystem.all(BehaviourComponent.class, TemporalComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final BehaviourComponent bc = Mapper.behaviourComponent.get(entity);
		if (bc.brain.behave(entity) && bc.cortex != null)
			bc.cortex.behave(entity);
	}

}
