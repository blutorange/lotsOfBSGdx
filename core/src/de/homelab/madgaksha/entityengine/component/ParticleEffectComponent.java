package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.resourcecache.EParticleEffect;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * Stores the the inverse mass of a component.
 * 
 * Unit: 1/kg
 * 
 * @author madgaksha
 *
 */
public class ParticleEffectComponent implements Component, Poolable {
	private final static ParticleEffect DEFAULT_PARTICLE_EFFECT = null;
	
	public ParticleEffect particleEffect = DEFAULT_PARTICLE_EFFECT;

	public ParticleEffectComponent() {
	}
	public ParticleEffectComponent(EParticleEffect particleEffect) {
		this.particleEffect = ResourceCache.getParticleEffect(particleEffect);
	}
	public ParticleEffectComponent(ParticleEffect particleEffect) {
		this.particleEffect = particleEffect;
	}
	
	@Override
	public void reset() {
		particleEffect = DEFAULT_PARTICLE_EFFECT;
	}

}
