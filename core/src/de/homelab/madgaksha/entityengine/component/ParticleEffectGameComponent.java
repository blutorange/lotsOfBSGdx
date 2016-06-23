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
public class ParticleEffectGameComponent extends ParticleEffectComponent {
	public ParticleEffectGameComponent() {
	}

	public ParticleEffectGameComponent(EParticleEffect particleEffect) {
		setup(particleEffect);
	}

	public ParticleEffectGameComponent(PooledEffect particleEffect) {
		setup(particleEffect);
	}

}
