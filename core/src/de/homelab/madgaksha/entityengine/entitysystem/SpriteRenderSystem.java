package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.batchGame;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.visibleWorldBoundingBox;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.AlphaComponent;
import de.homelab.madgaksha.entityengine.component.ColorComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder0Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder1Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder2Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder3Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder4Component;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.util.GeoUtil;

public class SpriteRenderSystem extends EntitySystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SpriteRenderSystem.class);
	private Family familyZ0 = null;
	private Family familyZ1 = null;
	private Family familyZ2 = null;
	private Family familyZ3 = null;
	private Family familyZ4 = null;
	private ImmutableArray<Entity> entitiesZ0;
	private ImmutableArray<Entity> entitiesZ1;
	private ImmutableArray<Entity> entitiesZ2;
	private ImmutableArray<Entity> entitiesZ3;
	private ImmutableArray<Entity> entitiesZ4;

	private float mapBoundaryMinX;
	private float mapBoundaryMinY;
	private float mapBoundaryMaxX;
	private float mapBoundaryMaxY;

	private float shadowRotateX;
	private float shadowRotateY;

	public SpriteRenderSystem() {
		this(DefaultPriority.spriteRenderSystem);
	}

	@SuppressWarnings("unchecked")
	public SpriteRenderSystem(int priority) {
		super(priority);
		this.familyZ0 = Family.all(SpriteComponent.class, PositionComponent.class, ZOrder0Component.class).exclude(InvisibleComponent.class)
				.get();
		this.familyZ1 = Family.all(SpriteComponent.class, PositionComponent.class, ZOrder1Component.class).exclude(InvisibleComponent.class)
				.get();
		this.familyZ2 = Family.all(SpriteComponent.class, PositionComponent.class, ZOrder2Component.class).exclude(InvisibleComponent.class)
				.get();
		this.familyZ3 = Family.all(SpriteComponent.class, PositionComponent.class, ZOrder3Component.class).exclude(InvisibleComponent.class)
				.get();
		this.familyZ4 = Family.all(SpriteComponent.class, PositionComponent.class, ZOrder4Component.class).exclude(InvisibleComponent.class)
				.get();

		mapBoundaryMinX = level.getMapXW();
		mapBoundaryMinY = level.getMapYW();
		mapBoundaryMaxX = mapBoundaryMinX + level.getMapWidthW();
		mapBoundaryMaxY = mapBoundaryMinY + level.getMapHeightW();
	}

	@Override
	public void update(float deltaTime) {
		// Apply projection matrix.
		batchGame.setProjectionMatrix(viewportGame.getCamera().combined);

		// Render map.
		level.getMapRenderer().setView(viewportGame.getCamera().combined,
				MathUtils.clamp(visibleWorldBoundingBox.x, mapBoundaryMinX, mapBoundaryMaxX),
				MathUtils.clamp(visibleWorldBoundingBox.y, mapBoundaryMinY, mapBoundaryMaxY),
				MathUtils.clamp(visibleWorldBoundingBox.x + visibleWorldBoundingBox.width, mapBoundaryMaxX,
						mapBoundaryMaxX),
				MathUtils.clamp(visibleWorldBoundingBox.y + visibleWorldBoundingBox.height, mapBoundaryMinY,
						mapBoundaryMaxY));
		level.getMapRenderer().render();

		// Render sprites.
		batchGame.begin();
		renderEntities(entitiesZ0);
		renderEntities(entitiesZ1);
		renderEntities(entitiesZ2);
		renderEntities(entitiesZ3);
		renderEntities(entitiesZ4);
		batchGame.end();
	}

	private void renderEntities(ImmutableArray<Entity> entities) {
		// Get rotation of camera relative to xy plane.
		final float cameraUpAngleXY = viewportGame.getRotationUpXY();

		for (int i = 0; i < entities.size(); ++i) {
			final Entity entity = entities.get(i);
			final SpriteComponent spc = Mapper.spriteComponent.get(entity);
			final PositionComponent pc = Mapper.positionComponent.get(entity);
			final RotationComponent rc = Mapper.rotationComponent.get(entity);
			final ScaleComponent sc = Mapper.scaleComponent.get(entity);
			final ShadowComponent kc = Mapper.shadowComponent.get(entity);
			final BoundingBoxRenderComponent bbrc = Mapper.boundingBoxRenderComponent.get(entity);
			final ColorComponent cc = Mapper.colorComponent.get(entity);
			final AlphaComponent ac = Mapper.alphaComponent.get(entity);
			
			// Do not render if off-screen.
			if (bbrc != null && !GeoUtil.boundingBoxVisible(bbrc,pc)) continue;

			shadowRotateX = 0.0f;
			shadowRotateY = 0.0f;
		
			// Colorize if desired.
			spc.sprite.setColor(cc != null ? cc.color : Color.WHITE);
			spc.sprite.setAlpha(ac != null ? ac.alpha : 1.0f);
			
			// Rotate if desired.
			if (rc != null) {
				if (rc.inverseToCamera)
					spc.sprite.setRotation(rc.thetaZ - cameraUpAngleXY);
				else
					spc.sprite.setRotation(rc.thetaZ);
				shadowRotateX = rc.thetaZ;
				shadowRotateY = rc.thetaZ;
			}
			
			// Scale if desired.
			if (sc != null)
				spc.sprite.setScale(sc.scaleX*sc.originalScale, sc.scaleY*sc.originalScale);

			// Drop shadow if desired.
			if (kc != null) {
				// Should be a tangent (offsetRotateX =
				// tan(rc.thetaZ)*(shadowPositionY-rotateOriginY)),
				// but for small angles, a linear approximation is good enough.
				shadowRotateX = shadowRotateX * kc.offsetRotateX + kc.offsetX;
				shadowRotateY = shadowRotateY * kc.offsetRotateY + kc.offsetY;
				kc.sprite.setScale(1.0f + pc.offsetX * kc.scaleFactorX + pc.offsetY * kc.scaleFactorY);
				if (kc.relativeToCamera) {
					kc.sprite.setCenter(
							pc.x + shadowRotateY * viewportGame.getCamera().up.x
									+ shadowRotateX * viewportGame.getCamera().up.y,
							pc.y + shadowRotateY * viewportGame.getCamera().up.y
									- shadowRotateX * viewportGame.getCamera().up.x);
				}
				else {
					kc.sprite.setCenter(
							pc.x + shadowRotateX,
							pc.y + shadowRotateY);
				}
				kc.sprite.setRotation(-viewportGame.getRotationUpXY());
				kc.sprite.draw(batchGame);
			}

			// Draw main sprite.
			spc.sprite.setCenter(pc.x + pc.offsetX, pc.y + pc.offsetY);
			spc.sprite.draw(batchGame);
		}
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entitiesZ0 = engine.getEntitiesFor(familyZ0);
		entitiesZ1 = engine.getEntitiesFor(familyZ1);
		entitiesZ2 = engine.getEntitiesFor(familyZ2);
		entitiesZ3 = engine.getEntitiesFor(familyZ3);
		entitiesZ4 = engine.getEntitiesFor(familyZ4);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		entitiesZ0 = null;
		entitiesZ1 = null;
		entitiesZ2 = null;
		entitiesZ3 = null;
		entitiesZ4 = null;
	}
}
