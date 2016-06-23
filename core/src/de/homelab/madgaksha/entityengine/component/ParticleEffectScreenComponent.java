package de.homelab.madgaksha.entityengine.component;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

import de.homelab.madgaksha.resourcepool.EParticleEffect;

/**
 * Stores the the inverse mass of a component.
 * 
 * Unit: 1/kg
 * 
 * @author madgaksha
 *
 */
public class ParticleEffectScreenComponent extends ParticleEffectComponent {
	public ParticleEffectScreenComponent() {
	}

	public ParticleEffectScreenComponent(EParticleEffect particleEffect) {
		setup(particleEffect);
	}

	public ParticleEffectScreenComponent(PooledEffect particleEffect) {
		setup(particleEffect);
	}

}
