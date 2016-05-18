package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.resourcecache.EParticleEffect;

public class ParticleEffectMaker extends AEntityMaker {

	public ParticleEffectMaker(Shape2D shape, EParticleEffect particleEffect) {
		super();
		PositionComponent positionComponent = MakerUtils.makePositionAtCenter(shape);
		ParticleEffectComponent particleEffectComponent = new ParticleEffectComponent(particleEffect);
		add(positionComponent);
		add(particleEffectComponent);
	}

	@Override
	public void reinitializeEntity() {
		// TODO Auto-generated method stub
	}
	
}
