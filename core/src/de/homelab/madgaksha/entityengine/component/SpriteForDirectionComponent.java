package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * Component for sprites that are pseudo 3D, ie. with a different sprite for
 * each of the eight directions.
 *  
 * Contains the mapping as an array of 8 animations.
 * 
 * The layout is as follows:
 * 
 * 1      2     3
 *    __  ^ __
 *    |\  |  /|
 *      \ | /
 *       \|/
 * 0 <----+----> 4
 *       /|\
 *      / | \
 *    |/  |  \|
 *     -- v  -- 
 * 7      6      5
 * 
 * @author madgaksha
 *
 */
public class SpriteForDirectionComponent implements Component, Poolable {
	private final static Logger LOG = Logger.getLogger(SpriteForDirectionComponent.class);
	private final static Animation[] DEFAULT_ANIMATION_LIST = new Animation[0];
	private final static ESpriteDirectionStrategy DEFAULT_SPRITE_DIRECTION_STRATEGY = ESpriteDirectionStrategy.STATIC;
	
	
	public Animation[] animationList = DEFAULT_ANIMATION_LIST;
	public ESpriteDirectionStrategy spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
	
	
	public SpriteForDirectionComponent(Animation[] al) {
		animationList = al;
	}
	public SpriteForDirectionComponent(Animation[] al, ESpriteDirectionStrategy estrat) {
		animationList = al;
		spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
	}
	public SpriteForDirectionComponent(EAnimationList eal) {
		this(eal, DEFAULT_SPRITE_DIRECTION_STRATEGY, false);
	}
	public SpriteForDirectionComponent(EAnimationList eal, ESpriteDirectionStrategy estrat) {
		this(eal, estrat, true);
	}
	public SpriteForDirectionComponent(EAnimationList eal, ESpriteDirectionStrategy estrat, boolean cached) {
		animationList = ResourceCache.getAnimationList(eal, cached);
		LOG.debug(animationList);
		spriteDirectionStrategy = estrat;
	}
	
	
	@Override
	public void reset() {
		animationList = DEFAULT_ANIMATION_LIST;
		spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
	}
}
