package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
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
		GlobalBag.level.getMapRenderer().setView(viewport.getCamera().combined,
				MathUtils.clamp(GlobalBag.worldVisibleMinX, mapBoundaryMinX, mapBoundaryMaxX),
				MathUtils.clamp(GlobalBag.worldVisibleMinY, mapBoundaryMinY, mapBoundaryMaxY),
				MathUtils.clamp(GlobalBag.worldVisibleMaxX, mapBoundaryMaxX, mapBoundaryMaxX),
				MathUtils.clamp(GlobalBag.worldVisibleMaxY, mapBoundaryMinY, mapBoundaryMaxY));
		GlobalBag.level.getMapRenderer().render();

		// Render sprites.
		batch.begin();
		for (int i = 0; i < entities.size(); ++i) {
			final Entity entity = entities.get(i);
			final SpriteComponent spc = Mapper.spriteComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final RotationComponent rc = Mapper.rotationComponent.get(entity);
			final ScaleComponent sc = Mapper.scaleComponent.get(entity);

			if (rc != null) {
				if (rc.inverseToCamera)
					spc.sprite.setRotation(rc.thetaZ - cameraUpAngleXY);
				else
					spc.sprite.setRotation(rc.thetaZ);
			}
			if (sc != null) {
				spc.sprite.setScale(sc.scaleX);
				spc.sprite.setScale(sc.scaleY);
			}
			spc.sprite.setCenter(pc.x+pc.offsetX, pc.y+pc.offsetY);
			spc.sprite.draw(batch);
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
