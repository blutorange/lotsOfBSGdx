package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcecache.Resources.EAnimationList;

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
	private static final Animation[] RESET_LIST = new Animation[0];
	public Animation[] animationList = {};
	public ESpriteDirectionStrategy spriteDirectionStrategy = ESpriteDirectionStrategy.STATIC;
	
	public SpriteForDirectionComponent(Animation[] al) {
		animationList = al;
	}
	public SpriteForDirectionComponent(Animation[] al, ESpriteDirectionStrategy estrat) {
		animationList = al;
		spriteDirectionStrategy = estrat;
	}
	public SpriteForDirectionComponent(EAnimationList eal) {
		animationList = ResourceCache.getAnimationList(eal);
	}
	public SpriteForDirectionComponent(EAnimationList eal, ESpriteDirectionStrategy estrat) {
		animationList = ResourceCache.getAnimationList(eal);
		spriteDirectionStrategy = estrat;
	}	
	
	@Override
	public void reset() {
		animationList = RESET_LIST;
	}
}
