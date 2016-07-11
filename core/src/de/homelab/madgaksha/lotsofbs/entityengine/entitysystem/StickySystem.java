package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.StickyComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class StickySystem extends DisableIteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(StickySystem.class);
	private final Vector3 upVector;

	public StickySystem() {
		this(DefaultPriority.stickySystem);
	}

	@SuppressWarnings("unchecked")
	public StickySystem(int priority) {
		super(DisableIteratingSystem.all(ShouldPositionComponent.class, StickyComponent.class, TemporalComponent.class)
				.exclude(InactiveComponent.class, VelocityComponent.class), priority);
		upVector = viewportGame.getPerspectiveCamera().up;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final ShouldPositionComponent spc = Mapper.shouldPositionComponent.get(entity);
		final StickyComponent sec = Mapper.stickyComponent.get(entity);
		if (sec.offsetRelativeToCamera) {
			spc.x = sec.stickToPositionComponent.x + sec.offsetY * upVector.x + sec.offsetX * upVector.y;
			spc.y = sec.stickToPositionComponent.y + sec.offsetY * upVector.y - sec.offsetX * upVector.x;
		} else {
			spc.x = sec.stickToPositionComponent.x + sec.offsetX;
			spc.y = sec.stickToPositionComponent.y + sec.offsetY;
		}
		if (!sec.ignoreTrackOffset) {
			spc.offsetX = sec.stickToPositionComponent.offsetX;
			spc.offsetY = sec.stickToPositionComponent.offsetY;
		}
	}

}
