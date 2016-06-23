package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectGameComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcepool.EParticleEffect;

public class ParticleEffectMaker extends EntityMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ParticleEffectMaker.class);

	// Singleton
	private static class SingletonHolder {
		private static final ParticleEffectMaker INSTANCE = new ParticleEffectMaker();
	}

	public static ParticleEffectMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private ParticleEffectMaker() {
		super();
	}

	/**
	 * Adds the appropriate components to an {@link Entity} to be used as a
	 * ParticleEffect component. Drawn in game coordinates.
	 * 
	 * @param entity
	 *            Entity to setup.
	 * @param shape
	 *            Shape of the entity. Particle effect will be positioned at the
	 *            center of its bounding box.
	 * @param particleEffect
	 *            Particle effect to add.
	 * @param spin
	 *            Angular velocity. 0.0f to disable spinning.
	 */
	public void setupGame(Entity entity, Shape2D shape, EParticleEffect particleEffect, float spin) {
		setup(new ParticleEffectGameComponent(particleEffect), entity, shape, particleEffect, spin);
	}

	/**
	 * Adds the appropriate components to an {@link Entity} to be used as a
	 * ParticleEffect component. Drawn in screen coordinates, useful for
	 * rotation. Position is unaffected.
	 * 
	 * @param entity
	 *            Entity to setup.
	 * @param shape
	 *            Shape of the entity. Particle effect will be positioned at the
	 *            center of its bounding box.
	 * @param particleEffect
	 *            Particle effect to add.
	 * @param spin
	 *            Angular velocity. 0.0f to disable spinning.
	 */
	public void setupScreen(Entity entity, Shape2D shape, EParticleEffect particleEffect, float spin) {
		setup(new ParticleEffectScreenComponent(particleEffect), entity, shape, particleEffect, spin);
	}

	private void setup(ParticleEffectComponent particleEffectComponent, Entity entity, Shape2D shape,
			EParticleEffect particleEffect, float spin) {
		super.setup(entity);

		final PositionComponent positionComponent = MakerUtils.makePositionAtCenter(shape);
		particleEffectComponent.setup(particleEffect);
		final TemporalComponent temporalComponent = new TemporalComponent();

		particleEffectComponent.particleEffect.setPosition(positionComponent.x, positionComponent.y);

		if (spin != 0.0f) {
			entity.add(new AngularVelocityComponent(spin));
			entity.add(new DirectionComponent(0.0f));
		}

		entity.add(particleEffectComponent);
		entity.add(temporalComponent);

		particleEffectComponent.particleEffect.start();
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
}
