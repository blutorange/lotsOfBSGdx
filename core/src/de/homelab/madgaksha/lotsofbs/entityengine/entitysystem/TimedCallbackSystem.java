package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Calls a callback after a given time, and keeps calling it a certain number of
 * times again.
 *
 * @author madgaksha
 */
public class TimedCallbackSystem extends IteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TimedCallbackSystem.class);

	public TimedCallbackSystem() {
		this(DefaultPriority.timedCallbackSystem);
	}

	public TimedCallbackSystem(final int priority) {
		super(Family.all(TimedCallbackComponent.class, TemporalComponent.class).exclude(InactiveComponent.class).get(),
				priority);
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final TimedCallbackComponent tcc = Mapper.timedCallbackComponent.get(entity);
		final TemporalComponent tc = Mapper.temporalComponent.get(entity);
		tcc.totalTime += tc.deltaTime;
		if (tcc.totalTime >= tcc.duration) {
			tcc.timedCallback.run(entity, tcc.callbackData);
			--tcc.callbacksLeft;
			if (tcc.callbacksLeft >= 0 && --tcc.callbacksLeft <= 0) {
				// everything done
				if (tcc.removeEntity) gameEntityEngine.removeEntity(entity);
				else entity.remove(TimedCallbackComponent.class);
			}
			tcc.totalTime = 0.0f;
		}
	}
}
