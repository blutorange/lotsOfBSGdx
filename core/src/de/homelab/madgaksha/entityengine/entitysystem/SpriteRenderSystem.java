package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.level.GameViewport;
import de.homelab.madgaksha.logging.Logger;

public class SpriteRenderSystem extends IteratingSystem {

	private final static Logger LOG = Logger.getLogger(SpriteRenderSystem.class);
	private SpriteBatch batch = null;
	private GameViewport viewport = null;

	public SpriteRenderSystem(GameViewport viewport, SpriteBatch batch) {
		this(viewport, batch, DefaultPriority.spriteRenderSystem);
	}

	@SuppressWarnings("unchecked")
	public SpriteRenderSystem(GameViewport viewport, SpriteBatch batch, int priority) {
		super(Family.all(SpriteComponent.class, PositionComponent.class).get(), priority);
		this.batch = batch;
		this.viewport = viewport;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final SpriteComponent sc = Mapper.spriteComponent.get(entity);
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();

		sc.sprite.setPosition(pc.x, pc.y);
		sc.sprite.draw(batch);

		batch.end();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
	}
}
