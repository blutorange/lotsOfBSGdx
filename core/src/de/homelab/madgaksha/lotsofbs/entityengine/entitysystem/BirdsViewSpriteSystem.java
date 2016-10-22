package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationForDirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent.AnimationForDirection;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Computes the correct frame for a bird's view sprite.
 *
 * All different views must be placed on the same texture.
 *
 * @author madgaksha
 *
 */
public class BirdsViewSpriteSystem extends DisableIteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BirdsViewSpriteSystem.class);

	public BirdsViewSpriteSystem() {
		this(DefaultPriority.birdsViewSpriteSystem);
	}

	public BirdsViewSpriteSystem(final int priority) {
		super(DisableIteratingSystem.all(AnimationForDirectionComponent.class, SpriteAnimationComponent.class,
				DirectionComponent.class), priority);
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final AnimationForDirectionComponent sfdc = Mapper.animationForDirectionComponent.get(entity);
		final SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(entity);
		final DirectionComponent dc = Mapper.directionComponent.get(entity);

		switch (sfdc.spriteDirectionStrategy) {
		case ZENITH:
			sac.animation = sfdc.animationList[((int) (((dc.degree - viewportGame.getRotationUpXY() - 90 + 360)
					+ 360.0f / (2.0f * sfdc.animationList.length)) * sfdc.animationList.length / 360.0f))
			                                   % sfdc.animationList.length];
			break;
		case SIDEWAYS:
			// TODO
			// Project direction vector of the camera to the x-y-plane.
			// Compute angle of this vector to the vector (0,1,0).
			// Use this angle as above.
			sac.animation = sfdc.animationList[((int) (((dc.degree + viewportGame.getRotationDirXY() - 90 + 360)
					+ 360.0f / (2.0f * sfdc.animationList.length)) * sfdc.animationList.length / 360.0f))
			                                   % sfdc.animationList.length];
			break;
		case STATIC:
			// eg. ((358+45)/360 % 4) = 0 => first sprite corresponding to
			// looking left
			// LOG.debug((int)((dc.degree + 360.0f / (2.0f *
			// sfdc.animationList.length)) * sfdc.animationList.length /
			// 360.0f));
			sac.animation = sfdc.animationList[((int) (((dc.degree - 90 + 360)
					+ 360.0f / (2.0f * sfdc.animationList.length)) * sfdc.animationList.length / 360.0f))
			                                   % sfdc.animationList.length];
			break;
		}
	}

	/**
	 * @param degree
	 *            Direction in which the sprite should look to. This is not
	 *            relative to the looking direction of the camera. 0 is looking
	 *            to the right, 90 is looking down etc.
	 * @param sfdc
	 *            The component from which the direction should be extracted.
	 * @return The animation for the given direction.
	 */
	public static AtlasAnimation getForDirection(final float degree, final AnimationForDirectionComponent afdc) {
		AtlasAnimation animation = null;
		animation = afdc.animationList[((int) (((degree + 180.0f) + 360.0f / (2.0f * afdc.animationList.length))
				* afdc.animationList.length / 360.0f)) % afdc.animationList.length];
		return animation;
	}

	public static AtlasAnimation getForDirection(final float degree, final AnimationForDirection afd) {
		AtlasAnimation animation = null;
		animation = afd.animationList[((int) (((degree + 180.0f) + 360.0f / (2.0f * afd.animationList.length))
				* afd.animationList.length / 360.0f)) % afd.animationList.length];
		return animation;
	}
}