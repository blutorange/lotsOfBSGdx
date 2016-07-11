package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchGame;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportPixel;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectGameComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class ParticleEffectRenderSystem extends EntitySystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ParticleEffectRenderSystem.class);
	private final static Vector2 v = new Vector2();
	private ImmutableArray<Entity> entitiesGame;
	private ImmutableArray<Entity> entitiesScreen;

	public ParticleEffectRenderSystem() {
		super(DefaultPriority.particleEffectRenderSystem);
	}

	@Override
	public void update(float deltaTime) {
		// Apply projection matrix.
		batchGame.setProjectionMatrix(viewportGame.getCamera().combined);

		// Draw each particle effect.
		batchGame.begin();
		for (int i = 0; i < entitiesGame.size(); ++i) {
			final Entity entity = entitiesGame.get(i);
			final ParticleEffectGameComponent pec = Mapper.particleEffectGameComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final DirectionComponent dc = Mapper.directionComponent.get(entity);
			deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
			if (dc != null)
				setAngle(pec.particleEffect, dc.degree);
			if (pc != null)
				pec.particleEffect.setPosition(pc.x, pc.y);
			pec.particleEffect.draw(batchGame, deltaTime);
			// Remove particle effect when done.
			if (pec.particleEffect.isComplete()) {
				if (pec.callback != null)
					pec.callback.run(entity, pec.data);
				entity.remove(ParticleEffectGameComponent.class);
			}
		}
		batchGame.end();

		// Apply projection matrix.
		batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);

		// Draw each particle effect.
		batchPixel.begin();
		for (int i = 0; i < entitiesScreen.size(); ++i) {
			final Entity entity = entitiesScreen.get(i);
			final ParticleEffectComponent pec = Mapper.particleEffectScreenComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final DirectionComponent dc = Mapper.directionComponent.get(entity);
			deltaTime = Mapper.temporalComponent.get(entity).deltaTime;
			if (dc != null)
				setAngle(pec.particleEffect, dc.degree);
			if (pc != null) {
				v.set(pc.x, pc.y);
				viewportGame.project(v);
				pec.particleEffect.setPosition(v.x, v.y);
			}
			pec.particleEffect.draw(batchPixel, deltaTime);
			// Remove particle effect when done.
			if (pec.particleEffect.isComplete()) {
				if (pec.callback != null)
					pec.callback.run(entity, pec.data);
				entity.remove(ParticleEffectScreenComponent.class);
			}

		}
		batchPixel.end();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entitiesGame = engine.getEntitiesFor(Family.all(TemporalComponent.class, ParticleEffectGameComponent.class)
				.exclude(InvisibleComponent.class, InactiveComponent.class).get());
		entitiesScreen = engine.getEntitiesFor(Family.all(TemporalComponent.class, ParticleEffectScreenComponent.class)
				.exclude(InvisibleComponent.class, InactiveComponent.class).get());
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entitiesGame = null;
		entitiesScreen = null;
	}

	public void setAngle(PooledEffect pe, float angle) {
		float r = pe.getEmitters().get(0).getAngle().getLowMin();
		for (ParticleEmitter e : pe.getEmitters()) {
			final ScaledNumericValue snv = e.getAngle();
			snv.setHighMax(snv.getHighMax() - r + angle);
			snv.setHighMin(snv.getHighMin() - r + angle);
			snv.setLowMax(snv.getLowMax() - r + angle);
			snv.setLowMin(snv.getLowMin() - r + angle);
		}
	}

}
