package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

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
public class SpriteForDirectionComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SpriteForDirectionComponent.class);
	private final static AtlasAnimation[] DEFAULT_ANIMATION_LIST = new AtlasAnimation[0];
	private final static ESpriteDirectionStrategy DEFAULT_SPRITE_DIRECTION_STRATEGY = ESpriteDirectionStrategy.STATIC;

	public AtlasAnimation[] animationList = DEFAULT_ANIMATION_LIST;
	public ESpriteDirectionStrategy spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;

	public SpriteForDirectionComponent() {

	}

	public SpriteForDirectionComponent(AtlasAnimation[] al) {
		animationList = al;
	}

	public SpriteForDirectionComponent(AtlasAnimation[] al, ESpriteDirectionStrategy estrat) {
		animationList = al;
		spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
	}

	public SpriteForDirectionComponent(EAnimationList eal) {
		this(eal, DEFAULT_SPRITE_DIRECTION_STRATEGY, false);
	}

	public SpriteForDirectionComponent(EAnimationList eal, ESpriteDirectionStrategy estrat) {
		setup(eal, estrat, true);
	}

	public SpriteForDirectionComponent(EAnimationList eal, ESpriteDirectionStrategy estrat, boolean cached) {
		setup(eal, estrat, cached);
	}

	public void setup(EAnimationList eal, ESpriteDirectionStrategy estrat) {
		setup(eal, estrat, true);
	}

	public void setup(EAnimationList eal, ESpriteDirectionStrategy estrat, boolean cached) {
		animationList = ResourceCache.getAnimationList(eal, cached);
		spriteDirectionStrategy = estrat;
	}

	@Override
	public void reset() {
		animationList = DEFAULT_ANIMATION_LIST;
		spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
	}
}
