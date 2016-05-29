package de.homelab.madgaksha.entityengine.entitysystem;
import static de.homelab.madgaksha.GlobalBag.batchGame;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class ParticleEffectRenderSystem extends EntitySystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ParticleEffectRenderSystem.class);
	private ImmutableArray<Entity> entities;

	public ParticleEffectRenderSystem() {
		super(DefaultPriority.particleEffectRenderSystem);
	}

	@Override
	public void update(float deltaTime) {
		// Apply projection matrix.
		batchGame.setProjectionMatrix(viewportGame.getCamera().combined);

		// Draw each particle effect.
		batchGame.begin();
		for (int i = 0; i < entities.size(); ++i) {
			final Entity entity = entities.get(i);
			final ParticleEffectComponent pec = Mapper.particleEffectComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final DirectionComponent dc = Mapper.directionComponent.get(entity);
			deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
			if (dc != null) setAngle(pec.particleEffect, dc.degree);
			if (pc != null) pec.particleEffect.setPosition(pc.x, pc.y);
			pec.particleEffect.draw(batchGame, deltaTime);
			if (pec.particleEffect.isComplete()) entity.remove(ParticleEffectComponent.class);
		}
		batchGame.end();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(
				TemporalComponent.class,
				ParticleEffectComponent.class)
				.exclude(InvisibleComponent.class, InactiveComponent.class).get());
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entities = null;
	}
	
	public void setAngle(PooledEffect pe, float angle) {
		float r = pe.getEmitters().get(0).getAngle().getLowMin();
		for (ParticleEmitter e : pe.getEmitters()) {
			final ScaledNumericValue snv = e.getAngle();
			snv.setHighMax(snv.getHighMax()-r+angle);
			snv.setHighMin(snv.getHighMin()-r+angle);
			snv.setLowMax(snv.getLowMax()-r+angle);
			snv.setLowMin(snv.getLowMin()-r+angle);
		}
	}

}
