package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.CallbackOnReenterComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Calls a callback after a given time, and keeps calling it a certain number of
 * times again.
 *
 * @author madgaksha
 */
public class CallbackOnReenterSystem extends IteratingSystem implements EntityListener {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CallbackOnReenterSystem.class);

	public CallbackOnReenterSystem() {
		this(DefaultPriority.callbackOnReenterSystem);
	}

	public CallbackOnReenterSystem(final int priority) {
		super(Family.all(CallbackOnReenterComponent.class, PositionComponent.class).exclude(InactiveComponent.class)
				.get(), priority);
		final Family family = getFamily();
		gameEntityEngine.addEntityListener(family, this);
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		final CallbackOnReenterComponent corc = Mapper.callbackOnReenterComponent.get(entity);
		if (!corc.shape.contains(pc.x, pc.y)) {
			corc.hasLeftShape = true;
		} else if (corc.hasLeftShape) {
			// Call callback
			corc.onReenter.run(entity, corc.callbackData);
			if (corc.remainingNumberOfRepetitions > 0) {
				--corc.remainingNumberOfRepetitions;
				corc.hasLeftShape = false;
			} else if (corc.remainingNumberOfRepetitions == 0) {
				entity.remove(CallbackOnReenterComponent.class);
			}
		}
	}

	@Override
	public void entityRemoved(final Entity entity) {
	}

	@Override
	public void entityAdded(final Entity entity) {
		// check whether entity is inside or outside the shape initially
		final CallbackOnReenterComponent corc = Mapper.callbackOnReenterComponent.get(entity);
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		corc.hasLeftShape = !corc.shape.contains(pc.x, pc.y);
	}
}
