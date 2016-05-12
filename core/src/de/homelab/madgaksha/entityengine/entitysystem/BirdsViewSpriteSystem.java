package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.BirdsViewSpriteComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.level.GameViewport;

/**
 * Computes the correct frame for a bird's view sprite.
 * 
 * All different views must be placed on the same texture.
 * 
 * @author madgaksha
 *
 */
public class BirdsViewSpriteSystem extends IteratingSystem {

	private GameViewport viewport = null;

	public BirdsViewSpriteSystem(GameViewport viewport) {
		this(viewport, DefaultPriority.birdsViewSpriteSystem);
	}

	@SuppressWarnings("unchecked")
	public BirdsViewSpriteSystem(GameViewport viewport, int priority) {
		super(Family.all(BirdsViewSpriteComponent.class, SpriteAnimationComponent.class, RotationComponent.class).get(), priority);
		this.viewport = viewport;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final BirdsViewSpriteComponent bvsc = Mapper.birdsViewSpriteComponent.get(entity);
		final SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(entity);
		final RotationComponent rc = Mapper.rotationComponent.get(entity);
		
		//TODO
		// compute mode from viewport#camera and rc
		
		sac.animationFamily.setMode(mode);
	}

}
