package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.level.GameViewport;
import de.homelab.madgaksha.logging.Logger;

public class SpriteRenderSystem extends EntitySystem {

	private final static Logger LOG = Logger.getLogger(SpriteRenderSystem.class);
	private SpriteBatch batch = null;
	private GameViewport viewport = null;
	private Family family = null;
	private ImmutableArray<Entity> entities;

	private float mapBoundaryMinX;
	private float mapBoundaryMinY;
	private float mapBoundaryMaxX;
	private float mapBoundaryMaxY;

	
	public SpriteRenderSystem(GameViewport v, SpriteBatch b) {
		this(v, b, DefaultPriority.spriteRenderSystem);
	}

	@SuppressWarnings("unchecked")
	public SpriteRenderSystem(GameViewport v, SpriteBatch b, int priority) {
		super(priority);
		this.family = Family.all(SpriteComponent.class, PositionComponent.class).get();
		this.batch = b;
		this.viewport = v;
		
		mapBoundaryMinX = GlobalBag.level.getMapXW();
		mapBoundaryMinY = GlobalBag.level.getMapYW();
		mapBoundaryMaxX = mapBoundaryMinX + GlobalBag.level.getMapWidthW();
		mapBoundaryMaxY = mapBoundaryMinY + GlobalBag.level.getMapHeightW();		
	}

	public ImmutableArray<Entity> getEntities() {
		return entities;
	}

	@Override
	public void update(float deltaTime) {
		// Apply projection matrix.
		batch.setProjectionMatrix(viewport.getCamera().combined);

		// Get rotation of camera relative to xy plane.
		final float cameraUpAngleXY = viewport.getRotationUpXY();
		
		// Render map.
		GlobalBag.tiledMapRenderer.setView(viewport.getCamera().combined,
				MathUtils.clamp(GlobalBag.worldVisibleMinX, mapBoundaryMinX, mapBoundaryMaxX),
				MathUtils.clamp(GlobalBag.worldVisibleMinY, mapBoundaryMinY, mapBoundaryMaxY),
				MathUtils.clamp(GlobalBag.worldVisibleMaxX, mapBoundaryMaxX, mapBoundaryMaxX),
				MathUtils.clamp(GlobalBag.worldVisibleMaxY, mapBoundaryMinY, mapBoundaryMaxY));
		GlobalBag.tiledMapRenderer.render();

		// Render sprites.
		batch.begin();
		for (int i = 0; i < entities.size(); ++i) {
			final Entity entity = entities.get(i);
			final SpriteComponent sc = Mapper.spriteComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final RotationComponent rc = Mapper.rotationComponent.get(entity);

			if (rc != null) {
				if (rc.inverseToCamera)
					sc.sprite.setRotation(rc.thetaZ - cameraUpAngleXY);
				else
					sc.sprite.setRotation(rc.thetaZ);
			}

			// TODO remove me, testing only
			pc.x += (i == 0 ? Game.testx : Game.testx2); // <-- this
			pc.y += (i == 0 ? Game.testy : Game.testy2); // <-- this

			sc.sprite.setCenter(pc.x, pc.y);
			sc.sprite.draw(batch);
		}
		batch.end();
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(family);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entities = null;
	}
}
