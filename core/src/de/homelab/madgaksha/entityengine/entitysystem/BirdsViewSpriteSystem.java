package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.level.GameViewport;
import de.homelab.madgaksha.logging.Logger;

/**
 * Computes the correct frame for a bird's view sprite.
 * 
 * All different views must be placed on the same texture.
 * 
 * @author madgaksha
 *
 */
public class BirdsViewSpriteSystem extends IteratingSystem {

	private final static Logger LOG = Logger.getLogger(BirdsViewSpriteSystem.class);
	private final static Vector2 u = new Vector2();
	private final static Vector2 v = new Vector2();
	
	private GameViewport viewport = null;

	public BirdsViewSpriteSystem(GameViewport viewport) {
		this(viewport, DefaultPriority.birdsViewSpriteSystem);
	}

	@Override
	public void update(float deltaTime) {
		// Iterate.
		super.update(deltaTime);		
	}
	
	@SuppressWarnings("unchecked")
	public BirdsViewSpriteSystem(GameViewport viewport, int priority) {
		super(Family.all(SpriteForDirectionComponent.class, SpriteAnimationComponent.class, DirectionComponent.class)
				.get(), priority);
		this.viewport = viewport;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final SpriteForDirectionComponent sfdc = Mapper.spriteForDirectionComponent.get(entity);
		final SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(entity);
		final DirectionComponent dc = Mapper.directionComponent.get(entity);
		
		switch (sfdc.spriteDirectionStrategy) {
		case ZENITH:
			sac.animation = sfdc.animationList[((int)(((dc.degree+viewport.getRotationUpXY() - 90 + 360) + 360.0f / (2.0f * sfdc.animationList.length)) * sfdc.animationList.length / 360.0f))
			               					% sfdc.animationList.length];			
			break;
		case SIDEWAYS:
			//TODO
			// Project direction vector of the camera to the x-y-plane.
			// Compute angle of this vector to the vector (0,1,0).
			// Use this angle as above.
			sac.animation = sfdc.animationList[((int)(((dc.degree+viewport.getRotationDirXY() - 90 + 360) + 360.0f / (2.0f * sfdc.animationList.length)) * sfdc.animationList.length / 360.0f))
				               					% sfdc.animationList.length];
			break;
		case STATIC:
			// eg. ((358+45)/360 % 4) = 0 => first sprite corresponding to
			// looking left
			//LOG.debug((int)((dc.degree + 360.0f / (2.0f * sfdc.animationList.length)) * sfdc.animationList.length / 360.0f));
			sac.animation = sfdc.animationList[((int)((dc.degree + 360.0f / (2.0f * sfdc.animationList.length)) * sfdc.animationList.length / 360.0f))
					% sfdc.animationList.length];
			break;
		}
	}
}