package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcepool.EParticleEffect;

public class EntityParticleEffect extends AEntity {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EntityParticleEffect.class);

	public EntityParticleEffect(Shape2D shape, EParticleEffect particleEffect, float spin) {
		super();
		PositionComponent positionComponent = MakerUtils.makePositionAtCenter(shape);
		ParticleEffectComponent particleEffectComponent = new ParticleEffectComponent(particleEffect);
		TemporalComponent temporalComponent = new TemporalComponent();
		
		particleEffectComponent.particleEffect.setPosition(positionComponent.x, positionComponent.y);
		
		if (spin != 0.0f) {
			add(new AngularVelocityComponent(spin));
			add(new DirectionComponent(0.0f));
		}
		
		add(particleEffectComponent);
		add(temporalComponent);
		
		particleEffectComponent.particleEffect.start();
	}

	@Override
	public void reinitializeEntity() {
		// TODO Auto-generated method stub
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
	
}
