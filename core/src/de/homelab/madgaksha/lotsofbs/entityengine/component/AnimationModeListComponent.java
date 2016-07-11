package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.lotsofbs.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimationList;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

/**
 * List of sprite for direction components for different
 * 
 * @author madgaksha
 *
 */
public class AnimationModeListComponent implements Component, Poolable {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AnimationModeListComponent.class);

	public static class AnimationForDirection {
		private final static ESpriteDirectionStrategy DEFAULT_SPRITE_DIRECTION_STRATEGY = ESpriteDirectionStrategy.STATIC;
		public AtlasAnimation[] animationList = new AtlasAnimation[0];
		public ESpriteDirectionStrategy spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;

		public void reset() {
			spriteDirectionStrategy = DEFAULT_SPRITE_DIRECTION_STRATEGY;
		}
	}

	public static enum AnimationMode {
		NORMAL {
			@Override
			public AnimationForDirection getAnimationForDirection(AnimationModeListComponent sfdcl) {
				return sfdcl.normal;
			}
		},
		DAMAGE {
			@Override
			public AnimationForDirection getAnimationForDirection(AnimationModeListComponent sfdcl) {
				return sfdcl.damage;
			}
		},
		BATTLE {
			@Override
			public AnimationForDirection getAnimationForDirection(AnimationModeListComponent sfdcl) {
				return sfdcl.battle;
			}
		},		
		DEATH {
			@Override
			public AnimationForDirection getAnimationForDirection(AnimationModeListComponent sfdcl) {
				return sfdcl.death;
			}
		}
		;
		public abstract AnimationForDirection getAnimationForDirection(AnimationModeListComponent sfdcl);
	}
	
	public AnimationForDirection normal = new AnimationForDirection();
	public AnimationForDirection battle = new AnimationForDirection();
	public AnimationForDirection damage = new AnimationForDirection();
	public AnimationForDirection death = new AnimationForDirection();
	
	public AnimationModeListComponent() {
	}

	private void setup(AnimationForDirection sd, AtlasAnimation[] al, ESpriteDirectionStrategy estrat) {
		sd.animationList = al;
		sd.spriteDirectionStrategy = estrat;
	}

	private void setup(AnimationForDirection sd, EAnimationList eal, ESpriteDirectionStrategy estrat, boolean cached) {
		sd.animationList = ResourceCache.getAnimationList(eal, cached);
		sd.spriteDirectionStrategy = estrat;
	}
	
	public void setupNormal(AtlasAnimation[] al, ESpriteDirectionStrategy sdf) {
		setup(normal, al, sdf);
	}

	public void setupNormal(EAnimationList al, ESpriteDirectionStrategy sdf, boolean cached) {
		setup(normal, al, sdf, cached);
	}

	public void setupDamage(AtlasAnimation[] al, ESpriteDirectionStrategy sdf) {
		setup(damage, al, sdf);
	}

	public void setupDamage(EAnimationList al, ESpriteDirectionStrategy sdf, boolean cached) {
		setup(damage, al, sdf, cached);
	}
	
	public void setupDeath(AtlasAnimation[] al, ESpriteDirectionStrategy sdf) {
		setup(death, al, sdf);
	}
	public void setupDeath(EAnimationList al, ESpriteDirectionStrategy sdf, boolean cached) {
		setup(death, al, sdf, cached);
	}
	
	public void setupBattle(AtlasAnimation[] al, ESpriteDirectionStrategy sdf) {
		setup(battle, al, sdf);
	}
	public void setupBattle(EAnimationList al, ESpriteDirectionStrategy sdf, boolean cached) {
		setup(battle, al, sdf, cached);
	}

	@Override
	public void reset() {
		normal.reset();
		damage.reset();
		battle.reset();
		death.reset();
	}

	public static AnimationForDirection getDefault() {
		return new AnimationForDirection();		
	}
}
