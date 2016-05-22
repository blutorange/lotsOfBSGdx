package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.resourcepool.EParticleEffect;
import de.homelab.madgaksha.resourcepool.ResourcePool;

/**
 * Stores the the inverse mass of a component.
 * 
 * Unit: 1/kg
 * 
 * @author madgaksha
 *
 */
public class ParticleEffectComponent implements Component, Poolable {
	private final static PooledEffect DEFAULT_PARTICLE_EFFECT = null;
	
	public PooledEffect particleEffect = DEFAULT_PARTICLE_EFFECT;
	
	public ParticleEffectComponent() {
	}
	public ParticleEffectComponent(EParticleEffect particleEffect) {
		this.particleEffect = ResourcePool.obtainParticleEffect(particleEffect);
	}
	public ParticleEffectComponent(PooledEffect particleEffect) {
		this.particleEffect = particleEffect;
	}
	
	@Override
	public void reset() {
		particleEffect = DEFAULT_PARTICLE_EFFECT;
	}

}
