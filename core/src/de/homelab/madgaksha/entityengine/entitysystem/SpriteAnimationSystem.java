package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;

public class SpriteAnimationSystem extends IteratingSystem {

	public SpriteAnimationSystem() {
		this(DefaultPriority.spriteAnimationSystem);
	}

	@SuppressWarnings("unchecked")
	public SpriteAnimationSystem(int priority) {
		super(Family.all(SpriteAnimationComponent.class, SpriteComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final SpriteComponent sc = Mapper.spriteComponent.get(entity);
		final SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(entity);
		sc.sprite.setRegion(sac.animation.getKeyFrame(stateTime));
	}
}
