package de.homelab.madgaksha.resourcepool;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Pool;

import de.homelab.madgaksha.entityengine.component.AnimationModeQueueComponent.AnimationModeTransition;
import de.homelab.madgaksha.logging.Logger;

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

	public static void init() {
		for (EParticleEffect resource : EParticleEffect.values()) {
			ParticleEffectPool resourcePool = resource.getPool();
			particleEffectPool.put(resource, resourcePool);
		}
	}

	private ResourcePool() {
	}

	public static AnimationModeTransition obtainAnimationModeTransition() {
		return animationModeTransitionPool.obtain();
	}
	public static void freeAnimationModeTransition(AnimationModeTransition amt) {
		animationModeTransitionPool.free(amt);
	}
	
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
		// particleEffectPool.get(eParticleEffect).free(pooledEffect);
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
