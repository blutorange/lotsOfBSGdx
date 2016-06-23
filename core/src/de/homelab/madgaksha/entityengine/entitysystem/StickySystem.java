package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.StickyComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class StickySystem extends IteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(StickySystem.class);
	private final Vector3 upVector;

	public StickySystem() {
		this(DefaultPriority.stickySystem);
	}

	@SuppressWarnings("unchecked")
	public StickySystem(int priority) {
		super(Family.all(ShouldPositionComponent.class, StickyComponent.class, TemporalComponent.class)
				.exclude(InactiveComponent.class, VelocityComponent.class).get(), priority);
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
