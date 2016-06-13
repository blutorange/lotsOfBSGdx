package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Calls a callback after a given time, and keeps calling it a certain number of times again.
 * 
 * @author madgaksha
 */
public class TimedCallbackSystem extends IteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TimedCallbackSystem.class);
	public TimedCallbackSystem() {
		this(DefaultPriority.timedCallbackSystem);
	}

	@SuppressWarnings("unchecked")
	public TimedCallbackSystem(int priority) {
		super(Family.all(TimedCallbackComponent.class, TemporalComponent.class).exclude(InactiveComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final TimedCallbackComponent tcc = Mapper.timedCallbackComponent.get(entity);
		final TemporalComponent tc = Mapper.temporalComponent.get(entity);
		tcc.totalTime += tc.deltaTime;
		if (tcc.totalTime >= tcc.duration) {
			tcc.timedCallback.run(entity, tcc.callbackData);
			--tcc.callbacksLeft;
			if (tcc.callbacksLeft >= 0 && --tcc.callbacksLeft <= 0) {
				// everything done
				entity.remove(TimedCallbackComponent.class);
			}
			tcc.totalTime = 0.0f;
		}
	}

}
