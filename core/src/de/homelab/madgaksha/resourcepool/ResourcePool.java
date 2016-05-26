package de.homelab.madgaksha.resourcepool;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

import de.homelab.madgaksha.logging.Logger;

public final class ResourcePool {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ResourcePool.class);
		
	private final static EnumMap<EParticleEffect,ParticleEffectPool> particleEffectPool = new EnumMap<EParticleEffect,ParticleEffectPool>(EParticleEffect.class);
	
	public static void init() {
		for (EParticleEffect resource : EParticleEffect.values()) {
			ParticleEffectPool resourcePool = resource.getPool();
			particleEffectPool.put(resource, resourcePool);
		}		
	}
	
	private ResourcePool(){}
	
	public static PooledEffect obtainParticleEffect(EParticleEffect effect) {
		return particleEffectPool.get(effect).obtain();
	}
	public static void freeParticleEffect(PooledEffect pooledEffect) {
		pooledEffect.free();
		//particleEffectPool.get(eParticleEffect).free(pooledEffect);
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
