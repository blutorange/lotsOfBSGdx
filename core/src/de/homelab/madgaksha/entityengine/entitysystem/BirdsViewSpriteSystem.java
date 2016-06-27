package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

/**
 * Computes the correct frame for a bird's view sprite.
 * 
 * All different views must be placed on the same texture.
 * 
 * @author madgaksha
 * 
 */
public class BirdsViewSpriteSystem extends IteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger
			.getLogger(BirdsViewSpriteSystem.class);

	public BirdsViewSpriteSystem() {
		this(DefaultPriority.birdsViewSpriteSystem);
	}

	@SuppressWarnings("unchecked")
	public BirdsViewSpriteSystem(int priority) {
		super(
				Family.all(SpriteForDirectionComponent.class,
						SpriteAnimationComponent.class,
						DirectionComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final SpriteForDirectionComponent sfdc = Mapper.spriteForDirectionComponent
				.get(entity);
		final SpriteAnimationComponent sac = Mapper.spriteAnimationComponent
				.get(entity);
		final DirectionComponent dc = Mapper.directionComponent.get(entity);

		switch (sfdc.spriteDirectionStrategy) {
		case ZENITH:
			sac.animation = sfdc.animationList[((int) (((dc.degree
					- viewportGame.getRotationUpXY() - 90 + 360) + 360.0f / (2.0f * sfdc.animationList.length))
					* sfdc.animationList.length / 360.0f))
					% sfdc.animationList.length];
			break;
		case SIDEWAYS:
			// TODO
			// Project direction vector of the camera to the x-y-plane.
			// Compute angle of this vector to the vector (0,1,0).
			// Use this angle as above.
			sac.animation = sfdc.animationList[((int) (((dc.degree
					+ viewportGame.getRotationDirXY() - 90 + 360) + 360.0f / (2.0f * sfdc.animationList.length))
					* sfdc.animationList.length / 360.0f))
					% sfdc.animationList.length];
			break;
		case STATIC:
			// eg. ((358+45)/360 % 4) = 0 => first sprite corresponding to
			// looking left
			// LOG.debug((int)((dc.degree + 360.0f / (2.0f *
			// sfdc.animationList.length)) * sfdc.animationList.length /
			// 360.0f));
			sac.animation = sfdc.animationList[((int) ((dc.degree + 360.0f / (2.0f * sfdc.animationList.length))
					* sfdc.animationList.length / 360.0f))
					% sfdc.animationList.length];
			break;
		}
	}

	/**
	 * @param degree
	 *            Direction in which the sprite should look to. This is not
	 *            relative to the looking direction of the camera.
	 *            0 is looking to the right, 90 is looking down etc.
	 * @param sfdc
	 *            The component from which the direction should be extracted.
	 * @return The animation for the given direction.
	 */
	public static AtlasAnimation getForDirection(float degree,
			SpriteForDirectionComponent sfdc) {
		AtlasAnimation animation = null;
		animation = sfdc.animationList[((int) (((degree + 180.0f) + 360.0f / (2.0f * sfdc.animationList.length))
				* sfdc.animationList.length / 360.0f))
				% sfdc.animationList.length];
		return animation;
	}
}