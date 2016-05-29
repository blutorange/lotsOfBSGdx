package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.LifeComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.logging.Logger;

public class LifeSystem extends IteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LifeSystem.class);
	
	public LifeSystem() {
		this(DefaultPriority.lifeSystem);
	}

	@SuppressWarnings("unchecked")
	public LifeSystem(int priority) {
		super(Family.all(LifeComponent.class, TemporalComponent.class).exclude(InactiveComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final LifeComponent lc = Mapper.lifeComponent.get(entity);
		final TemporalComponent tc = Mapper.temporalComponent.get(entity);
		lc.remainingLife -= tc.deltaTime;
		if (lc.remainingLife <= 0.0f) {
			if (lc.onDeath != null) lc.onDeath.kill(entity);
			else gameEntityEngine.removeEntity(entity);
		}
	}
}
