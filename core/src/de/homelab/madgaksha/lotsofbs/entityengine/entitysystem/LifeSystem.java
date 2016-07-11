package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.LifeComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class LifeSystem extends DisableIteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LifeSystem.class);

	public LifeSystem() {
		this(DefaultPriority.lifeSystem);
	}

	@SuppressWarnings("unchecked")
	public LifeSystem(int priority) {
		super(DisableIteratingSystem.all(LifeComponent.class, TemporalComponent.class).exclude(InactiveComponent.class),
				priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final LifeComponent lc = Mapper.lifeComponent.get(entity);
		final TemporalComponent tc = Mapper.temporalComponent.get(entity);
		lc.remainingLife -= tc.deltaTime;
		if (lc.remainingLife <= 0.0f) {
			if (lc.onDeath != null)
				lc.onDeath.kill(entity);
			else
				gameEntityEngine.removeEntity(entity);
		}
	}
}
