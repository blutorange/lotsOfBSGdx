package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.batchGame;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.logging.Logger;
/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class ParticleEffectRenderSystem extends EntitySystem {
	
	private final static Logger LOG = Logger.getLogger(ParticleEffectRenderSystem.class);
	private ImmutableArray<Entity> entities;
	
	public ParticleEffectRenderSystem() {
		super(DefaultPriority.particleEffectRenderSystem);
	}

	@Override
	public void update(float deltaTime) {
		// Apply projection matrix.
		batchGame.setProjectionMatrix(viewportGame.getCamera().combined);

		// Render sprites.
		batchGame.begin();
		for (int i = 0; i < entities.size(); ++i) {
			final Entity entity = entities.get(i);
			final ParticleEffectComponent pec = Mapper.particleEffectComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);

			// Apply projection matrix.
			pec.particleEffect.start();
			pec.particleEffect.setDuration(500000);
			if (pec.particleEffect.isComplete()) pec.particleEffect.reset();
			pec.particleEffect.setPosition(pc.x,pc.y);
			pec.particleEffect.draw(batchGame,0.05f);
		}
		batchGame.end();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class, ParticleEffectComponent.class).exclude(InvisibleComponent.class).get());
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entities = null;
	}
	
}
