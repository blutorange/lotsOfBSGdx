package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;

import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;

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
