package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.batchGame;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.worldVisibleMaxX;
import static de.homelab.madgaksha.GlobalBag.worldVisibleMaxY;
import static de.homelab.madgaksha.GlobalBag.worldVisibleMinX;
import static de.homelab.madgaksha.GlobalBag.worldVisibleMinY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.logging.Logger;
public class SpriteRenderSystem extends EntitySystem {

	
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SpriteRenderSystem.class);
	private Family family = null;
	private ImmutableArray<Entity> entities;

	private float mapBoundaryMinX;
	private float mapBoundaryMinY;
	private float mapBoundaryMaxX;
	private float mapBoundaryMaxY;

	
	public SpriteRenderSystem() {
		this(DefaultPriority.spriteRenderSystem);
	}

	@SuppressWarnings("unchecked")
	public SpriteRenderSystem(int priority) {
		super(priority);
		this.family = Family.all(SpriteComponent.class, PositionComponent.class).get();
		
		mapBoundaryMinX = level.getMapXW();
		mapBoundaryMinY = level.getMapYW();
		mapBoundaryMaxX = mapBoundaryMinX + level.getMapWidthW();
		mapBoundaryMaxY = mapBoundaryMinY + level.getMapHeightW();		
	}

	public ImmutableArray<Entity> getEntities() {
		return entities;
	}

	@Override
	public void update(float deltaTime) {
		// Apply projection matrix.
		batchGame.setProjectionMatrix(viewportGame.getCamera().combined);

		// Get rotation of camera relative to xy plane.
		final float cameraUpAngleXY = viewportGame.getRotationUpXY();
		
		// Render map.
		level.getMapRenderer().setView(viewportGame.getCamera().combined,
				MathUtils.clamp(worldVisibleMinX, mapBoundaryMinX, mapBoundaryMaxX),
				MathUtils.clamp(worldVisibleMinY, mapBoundaryMinY, mapBoundaryMaxY),
				MathUtils.clamp(worldVisibleMaxX, mapBoundaryMaxX, mapBoundaryMaxX),
				MathUtils.clamp(worldVisibleMaxY, mapBoundaryMinY, mapBoundaryMaxY));
		GlobalBag.level.getMapRenderer().render();

		// Render sprites.
		batchGame.begin();
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
			spc.sprite.draw(batchGame);
		}
		batchGame.end();
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
