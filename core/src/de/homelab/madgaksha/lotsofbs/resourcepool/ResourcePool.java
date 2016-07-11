package de.homelab.madgaksha.lotsofbs.resourcepool;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Pool;

import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeQueueComponent.AnimationModeTransition;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

public final class ResourcePool {
	private final static Logger LOG = Logger.getLogger(ResourcePool.class);

	private final static EnumMap<EParticleEffect, ParticleEffectPool> particleEffectPool = new EnumMap<EParticleEffect, ParticleEffectPool>(
			EParticleEffect.class);

	private final static Pool<AnimationModeTransition> animationModeTransitionPool = new Pool<AnimationModeTransition>(
			100, 2000) {
		@Override
		protected AnimationModeTransition newObject() {
			return new AnimationModeTransition();
		}
	};
	
	private final static Pool<CroppableAtlasSprite> spritePool = new Pool<CroppableAtlasSprite>(100,  10000) {
		@Override
		protected CroppableAtlasSprite newObject() {
			return new CroppableAtlasSprite();
		}		
	};

	public static void init() {
		for (EParticleEffect resource : EParticleEffect.values()) {
			ParticleEffectPool resourcePool = resource.getPool();
			particleEffectPool.put(resource, resourcePool);
		}
	}

	private ResourcePool() {
	}

	//===============================
	//   Croppable Atlas Sprite
	//===============================
	public CroppableAtlasSprite obtainSprite(ETexture texture) {
		return obtainSprite(ResourceCache.getTexture(texture));
	}

	public static CroppableAtlasSprite obtainSprite(AtlasRegion region) {
		CroppableAtlasSprite sprite = spritePool.obtain();
		sprite.setAtlasRegion(region);
		return sprite;
	}
	
	public static void freeSprite(CroppableAtlasSprite sprite) {
		spritePool.free(sprite);
	}
	
	//===============================
	//   Animation Mode Transition
	//===============================
	public static AnimationModeTransition obtainAnimationModeTransition() {
		return animationModeTransitionPool.obtain();
	}
	public static void freeAnimationModeTransition(AnimationModeTransition amt) {
		animationModeTransitionPool.free(amt);
	}

	//===============================
	//        Particle Effect
	//===============================
	public static PooledEffect obtainParticleEffect(EParticleEffect effect) {
		try {
			return particleEffectPool.get(effect).obtain();
		} catch (Exception e) {
			LOG.error("could not obtain particle effect", e);
			return null;
		}
	}
	public static void freeParticleEffect(PooledEffect pooledEffect) {
		pooledEffect.free();
	}

	public static void clearParticleEffect(EParticleEffect a) {
		particleEffectPool.get(a).clear();
	}

	public static void clearAllParticleEffect() {
		for (EParticleEffect pe : particleEffectPool.keySet()) {
			clearParticleEffect(pe);
		}
	}
}
