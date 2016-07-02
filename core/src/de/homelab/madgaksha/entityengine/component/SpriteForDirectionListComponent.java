package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

/**
 * List of sprite for direction components for different 
 * 
 * @author madgaksha
 *
 */
public class SpriteForDirectionListComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SpriteForDirectionListComponent.class);
	
	public static class SpriteDirection {
		private final static ESpriteDirectionStrategy DEFAULT_SPRITE_DIRECTION_STRATEGY = ESpriteDirectionStrategy.STATIC;
		public AtlasAnimation[] animationList = new AtlasAnimation[0];
		public ESpriteDirectionStrategy spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
		public void reset() {
			spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
		}	
	}
	
	public static enum SpriteMode {
		NORMAL {
			@Override
			public SpriteDirection getSpriteDirection(SpriteForDirectionListComponent sfdcl) {
				return sfdcl.normal;
			}			
		},
		DAMAGE {
			@Override
			public SpriteDirection getSpriteDirection(SpriteForDirectionListComponent sfdcl) {
				return sfdcl.damage;
			}			
		};
		public abstract SpriteDirection getSpriteDirection(SpriteForDirectionListComponent sfdcl); 
	}
	
	public SpriteMode mode = SpriteMode.NORMAL;
	public final SpriteDirection dfault = new SpriteDirection();
	public SpriteDirection normal = new SpriteDirection();
	public SpriteDirection damage = new SpriteDirection();

	public SpriteForDirectionListComponent() {
	}

	public SpriteForDirectionListComponent(SpriteMode mode) {
		this.mode = mode;
	}
	
	private void setup(SpriteDirection sd, AtlasAnimation[] al, ESpriteDirectionStrategy estrat) {
		sd.animationList = al;
		sd.spriteDirectionStrategy = estrat;
	}
	private void setup(SpriteDirection sd, EAnimationList eal, ESpriteDirectionStrategy estrat, boolean cached) {
		sd.animationList = ResourceCache.getAnimationList(eal, cached);
		sd.spriteDirectionStrategy = estrat;
	}
//	private void setup(SpriteDirection sd, AtlasAnimation[] al) {
//	sd.animationList = al;
//}
//	private void setup(SpriteDirection sd, EAnimationList eal) {
//		setup(sd, eal, SpriteDirection.DEFAULT_SPRITE_DIRECTION_STRATEGY, false);
//	}
//	private void setup(SpriteDirection sd, EAnimationList eal, ESpriteDirectionStrategy estrat) {
//		setup(sd, eal, estrat, true);
//	}
	
	public void setupNormal(AtlasAnimation[] al, ESpriteDirectionStrategy sdf) {
		setup(normal ,al, sdf);
	}
	public void setupNormal(EAnimationList al, ESpriteDirectionStrategy sdf, boolean cached) {
		setup(normal ,al, sdf, cached);
	}
	public void setupDamage(AtlasAnimation[] al, ESpriteDirectionStrategy sdf) {
		setup(damage ,al, sdf);
	}
	public void setupDamage(EAnimationList al, ESpriteDirectionStrategy sdf, boolean cached) {
		setup(damage ,al, sdf, cached);
	}

	@Override
	public void reset() {
		mode = SpriteMode.NORMAL;
		normal.reset();
		damage.reset();
	}
}
