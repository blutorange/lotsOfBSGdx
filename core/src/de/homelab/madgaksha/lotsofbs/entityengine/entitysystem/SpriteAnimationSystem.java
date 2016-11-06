package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;

public class SpriteAnimationSystem extends DisableIteratingSystem {

	public SpriteAnimationSystem() {
		this(DefaultPriority.spriteAnimationSystem);
	}

	public SpriteAnimationSystem(final int priority) {
		super(DisableIteratingSystem.all(TemporalComponent.class, SpriteAnimationComponent.class, SpriteComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final SpriteComponent sc = Mapper.spriteComponent.get(entity);
		final SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(entity);
		final AtlasRegion ar = sac.animation.getKeyFrame(Mapper.temporalComponent.get(entity).totalTime - sac.startTime);
		if (!sc.sprite.isAtlasRegionEqualTo(ar)) sc.sprite.setAtlasRegion(ar);
	}
}
