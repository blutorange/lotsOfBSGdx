package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.component.AnimationModeListComponent.AnimationForDirection;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

/**
 * Component for sprites that are pseudo 3D, ie. with a different sprite for
 * each of the eight directions.
 * 
 * Contains the mapping as an array of 8 animations.
 * 
 * The layout is as follows:
 * 
 * 1 2 3 __ ^ __ |\ | /| \ | / \|/ 0 <----+----> 4 /|\ / | \ |/ | \| -- v -- 7 6
 * 5
 * 
 * @author madgaksha
 *
 */
public class AnimationForDirectionComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AnimationForDirectionComponent.class);
	private final static ESpriteDirectionStrategy DEFAULT_SPRITE_DIRECTION_STRATEGY = ESpriteDirectionStrategy.STATIC;

	public AtlasAnimation[] animationList = new AtlasAnimation[0];
	public ESpriteDirectionStrategy spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
	
	public AnimationForDirectionComponent() {

	}

	public AnimationForDirectionComponent(ESpriteDirectionStrategy estrat) {
		spriteDirectionStrategy = estrat;
	}

	public AnimationForDirectionComponent(AtlasAnimation[] al) {
		setup(al, DEFAULT_SPRITE_DIRECTION_STRATEGY);
	}

	public AnimationForDirectionComponent(AtlasAnimation[] al, ESpriteDirectionStrategy estrat) {
		setup(al, estrat);
	}

	public AnimationForDirectionComponent(EAnimationList eal) {
		this(eal, DEFAULT_SPRITE_DIRECTION_STRATEGY, false);
	}

	public AnimationForDirectionComponent(EAnimationList eal, ESpriteDirectionStrategy estrat) {
		setup(eal, estrat, true);
	}

	public AnimationForDirectionComponent(EAnimationList eal, ESpriteDirectionStrategy estrat, boolean cached) {
		setup(eal, estrat, cached);
	}

	public void setup(AtlasAnimation[] al, ESpriteDirectionStrategy estrat) {
		animationList = al;
		spriteDirectionStrategy = estrat;
	}

	public void setup(EAnimationList eal, ESpriteDirectionStrategy estrat) {
		setup(eal, estrat, true);
	}

	public void setup(EAnimationList eal, ESpriteDirectionStrategy estrat, boolean cached) {
		animationList = ResourceCache.getAnimationList(eal, cached);
		spriteDirectionStrategy = estrat;
	}

	public void setup(AnimationForDirection sd) {
		animationList = sd.animationList;
		spriteDirectionStrategy = sd.spriteDirectionStrategy;
	}
	
	@Override
	public void reset() {
		animationList = new AtlasAnimation[0];
		spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
	}


}
