package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.AnimationForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.AnimationModeListComponent;
import de.homelab.madgaksha.entityengine.component.AnimationModeListComponent.AnimationForDirection;
import de.homelab.madgaksha.entityengine.component.AnimationModeQueueComponent;
import de.homelab.madgaksha.entityengine.component.AnimationModeQueueComponent.AnimationModeTransition;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.ResourcePool;

/**
 * Sets the spriteForDirectionComponent for the current mode.
 * 
 * @author madgaksha
 * 
 */
public class AnimationModeSystem extends DisableIteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AnimationModeSystem.class);

	public AnimationModeSystem() {
		this(DefaultPriority.animationModeSystem);
	}

	@SuppressWarnings("unchecked")
	public AnimationModeSystem(int priority) {
		super(DisableIteratingSystem.all(AnimationForDirectionComponent.class, AnimationModeListComponent.class,
				AnimationModeQueueComponent.class, TemporalComponent.class).exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// This will happen almost always, as transitions are usually rare and
		// quick.
		final AnimationModeQueueComponent smqc = Mapper.animationModeQueueComponent.get(entity);
		if (smqc.queue.isEmpty())
			return;

		// This happens only rarely.
		final AnimationForDirectionComponent sfdc = Mapper.animationForDirectionComponent.get(entity);
		final AnimationModeListComponent sfdlc = Mapper.animationModeListComponent.get(entity);
		final SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(entity);
		final TemporalComponent tc = Mapper.temporalComponent.get(entity);

		// Wait for completion of current animation if requested.
		if (smqc.queue.peekFirst().waitForCompletion
				&& !sac.animation.isAnimationFinished(tc.totalTime - sac.startTime))
			return;

		// Transition to requested animation.
		AnimationModeTransition amt = smqc.queue.pop();
		smqc.currentMode = amt.targetMode;
		AnimationForDirection sd = smqc.currentMode.getAnimationForDirection(sfdlc);
		if (sd == null)
			sd = AnimationModeListComponent.getDefault();

		// Reset animation if requested.
		if (amt.reset)
			sac.startTime = tc.totalTime;

		ResourcePool.freeAnimationModeTransition(amt);

		sfdc.setup(sd);
	}
}