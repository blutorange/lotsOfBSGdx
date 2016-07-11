package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.ITimedCallback;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;

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
	public ITimedCallback callback = null;
	public Object data = null;

	public ParticleEffectComponent() {
	}

	public ParticleEffectComponent(EParticleEffect particleEffect) {
		setup(particleEffect);
	}

	public ParticleEffectComponent(PooledEffect particleEffect) {
		setup(particleEffect);
	}

	public void setup(EParticleEffect particleEffect) {
		this.particleEffect = ResourcePool.obtainParticleEffect(particleEffect);
	}

	public void setup(PooledEffect particleEffect) {
		this.particleEffect = particleEffect;
	}

	@Override
	public void reset() {
		if (particleEffect != null)
			ResourcePool.freeParticleEffect(particleEffect);
		particleEffect = DEFAULT_PARTICLE_EFFECT;
		callback = null;
		data = null;
	}
}
