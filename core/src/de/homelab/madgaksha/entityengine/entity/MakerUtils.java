package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.battleModeActive;
import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.entityengine.component.DeathComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectGameComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.StickyComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.entityengine.component.TriggerScreenComponent;
import de.homelab.madgaksha.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxMapComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup03Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup04Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup05Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder0Component;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.enums.TrackingOrientationStrategy;
import de.homelab.madgaksha.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.grantstrategy.ImmediateGrantStrategy;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.EParticleEffect;
import de.homelab.madgaksha.resourcepool.ResourcePool;
import de.homelab.madgaksha.util.GeoUtil;

public final class MakerUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MakerUtils.class);
	private MakerUtils(){}

	public static BoundingBoxRenderComponent makeBoundingBoxRender(Shape2D shape) {
		return new BoundingBoxRenderComponent(GeoUtil.getBoundingBox(shape));
	}
	public static BoundingBoxMapComponent makeBoundingBoxMap(Shape2D shape) {
		return new BoundingBoxMapComponent(GeoUtil.getBoundingBox(shape));
	}
	public static BoundingBoxCollisionComponent makeBoundingBoxCollision(Shape2D shape) {
		return new BoundingBoxCollisionComponent(GeoUtil.getBoundingBox(shape));
	}
	/** 
	 * Makes a bounding box for the shape relative to the given center.
	 * @param shape The shape.
	 * @param center The center.
	 * @return A bounding box relative to the given center.
	 */
	public static BoundingBoxCollisionComponent makeBoundingBoxCollision(Shape2D shape, PositionComponent center) {
		BoundingBoxCollisionComponent bbc = makeBoundingBoxCollision(shape);
		bbc.minX -= center.x;
		bbc.minY -= center.y;
		bbc.maxX -= center.x;
		bbc.maxY -= center.y;
		return bbc;
	}
	public static BoundingBoxCollisionComponent makeBoundingBoxCollisionRelativeToCenter(Shape2D shape) {
		PositionComponent center = makePositionAtCenter(shape);
		return makeBoundingBoxCollision(shape, center);
	}
	
	public static PositionComponent makePositionAtCenter(Shape2D shape) {
		final PositionComponent pc = new PositionComponent();
		Vector2 center = new Vector2();
		GeoUtil.boundingBoxCenter(shape, center);
		pc.x = center.x;
		pc.y = center.y;
		return pc;
	}

	public static Component makeTrigger(ITrigger triggerAcceptingObject, IReceive triggerReceivingObject, ETrigger trigger, ECollisionGroup group) {
		switch (trigger) {
		case MANUAL:
			return null;
		case SCREEN:		
			return new TriggerScreenComponent(triggerAcceptingObject);
		case STARTUP:
			return new TriggerStartupComponent(triggerAcceptingObject);
		case TOUCH:
			final ReceiveTouchComponent ttc = makeReceiveTouch(group, triggerReceivingObject);
			return ttc;
		default:
			return null;		
		}
	}
	
	public static ReceiveTouchComponent makeReceiveTouch(ECollisionGroup group, IReceive triggerReceivingObject) {
		switch (group) {
		case GROUP_01:
			return new ReceiveTouchGroup01Component(triggerReceivingObject);
		case GROUP_02:
			return new ReceiveTouchGroup02Component(triggerReceivingObject);
		case GROUP_03:
			return new ReceiveTouchGroup03Component(triggerReceivingObject);
		case GROUP_04:
			return new ReceiveTouchGroup04Component(triggerReceivingObject);
		case GROUP_05:
			return new ReceiveTouchGroup05Component(triggerReceivingObject);
		default:
			return new ReceiveTouchGroup05Component(triggerReceivingObject);
		}
	}
	
	public static Entity makeEnemyTargetCross() {
		Entity targetCross = new Entity();
		AngularVelocityComponent avc = new AngularVelocityComponent(level.getEnemyTargetCrossAngularVelocity());
		InactiveComponent iac = new InactiveComponent();
		InvisibleComponent ivc = new InvisibleComponent();
		PositionComponent pc = new PositionComponent();
		RotationComponent rc = new RotationComponent();
		SpriteComponent sc = new SpriteComponent(level.getEnemyTargetCrossTexture());
		StickyComponent sec = new StickyComponent();
		ScaleComponent slc = new ScaleComponent(0.0f, 0.0f, level.getEnemyTargetCrossTexture().getOriginalScale());
		ShouldPositionComponent spc = new ShouldPositionComponent(new ImmediateGrantStrategy());
		ShouldScaleComponent ssc = new ShouldScaleComponent(new ExponentialGrantStrategy(0.99f, 0.05f));
		TemporalComponent tc = new TemporalComponent();
		ZOrder0Component zoc = new ZOrder0Component();
		
		BoundingBoxRenderComponent bbrc = new BoundingBoxRenderComponent(sc.sprite.getX(), sc.sprite.getY(),
				sc.sprite.getX() + sc.sprite.getWidth(), sc.sprite.getY() + sc.sprite.getHeight());
		targetCross.add(avc);
		targetCross.add(bbrc);
		targetCross.add(ivc);
		targetCross.add(iac);
		targetCross.add(pc);
		targetCross.add(rc);
		targetCross.add(sc);
		targetCross.add(sec);
		targetCross.add(slc);
		targetCross.add(spc);
		targetCross.add(ssc);
		targetCross.add(tc);
		targetCross.add(zoc);
		
		return targetCross; 
	}

	/**
	 * Sets up the family for camera tracking focus points and
	 * adds the appropriate listeners for when new enemies appear
	 * and leave.
	 * @return The family for the focus points.
	 */
	public static Family makeFocusPointsFamily() {
		@SuppressWarnings("unchecked")
		final Family family = Family.all(PositionComponent.class,CameraFocusComponent.class).exclude(InactiveComponent.class).get();
		gameEntityEngine.addEntityListener(family, new EntityListener() {
			@Override
			public void entityRemoved(Entity entity) {
				// Battle mode left.
				if (cameraTrackingComponent.focusPoints.size() == 0) {
					DeathComponent dc = Mapper.deathComponent.get(entity);
					EnemyMaker.exitBattleMode(dc != null && dc.dead);
				}
				// Switch target cross to a valid target.
				int focusPointsSize = cameraTrackingComponent.focusPoints.size();
				if (focusPointsSize > 0) {
					int index = Math.min(cameraTrackingComponent.trackedPointIndex, focusPointsSize-1);
					cameraTrackingComponent.trackedPointIndex = index;
					EnemyMaker.targetSwitched(cameraTrackingComponent.focusPoints.get(index), focusPointsSize != 1);
				}
			}
			@Override
			public void entityAdded(Entity entity) {
				// Battle mode entered
				if (!battleModeActive) {
					EnemyMaker.enterBattleMode(entity);
					EnemyMaker.targetSwitched(entity, false);
				}
			}
		});
		return family;
	}
	
	public static Entity makeCamera(ALevel level, Entity playerE) {
		final Entity myCamera = new Entity();
		final Family familyFocusPoints = makeFocusPointsFamily();
		
		final ManyTrackingComponent mtc = new ManyTrackingComponent(level.getMapXW(), level.getMapYW(), level.getMapWidthW(),
				level.getMapHeightW());
		mtc.minimumElevation = level.getMapData().getMinimumCameraElevation();
		mtc.maximumElevation = level.getMapData().getMaximumCameraElevation();
		mtc.focusPoints = gameEntityEngine.getEntitiesFor(familyFocusPoints);
		mtc.playerPoint = playerE;
		mtc.baseDirection = level.getMapData().getBaseDirection();
		mtc.gravity = level.getMapData().getPreferredPlayerLocation();
		mtc.trackingOrientationStrategy = TrackingOrientationStrategy.RELATIVE;
		
		cameraTrackingComponent = mtc;
		
		myCamera.add(mtc);		
		myCamera.add(new PositionComponent(viewportGame.getCamera().position.x,viewportGame.getCamera().position.y,viewportGame.getCamera().position.z));
		myCamera.add(new RotationComponent(viewportGame.getRotationUpXY()));
		myCamera.add(new ShouldPositionComponent(new ExponentialGrantStrategy(0.6f, 0.25f)));
		myCamera.add(new ShouldRotationComponent(new ExponentialGrantStrategy(0.6f, 0.25f)));
		myCamera.add(new ViewportComponent(viewportGame));
		myCamera.add(new TemporalComponent());
		myCamera.add(new TimeScaleComponent());
		return myCamera;
	}

	/** Injects an entity into the entity system that will be called after the given amount of time.
	 * 
	 * @param f Duration in seconds.
	 * @param iTimedCallback Callback to call.
	 */
	public static void addTimedRunnable(float duration, ITimedCallback timedCallback) {
		final Entity e = gameEntityEngine.createEntity();
		addTimedRunnableTo(e, duration, timedCallback);
		gameEntityEngine.addEntity(e);
	}
	
	public static void addTimedRunnableTo(Entity e, float duration, ITimedCallback timedCallback) {
		TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);
		TimedCallbackComponent tcc = gameEntityEngine.createComponent(TimedCallbackComponent.class);
		tcc.setup(timedCallback, null, duration, 1);
		e.add(tc).add(tcc);
	}

	/**
	 * Adds a particle effect at the given position, with game coordiantes.
	 * @param particleEffect Particle effect to add.
	 * @param positionComponent Location where particle effect will be placed.
	 */
	public static void addParticleEffectGame(EParticleEffect particleEffect,PositionComponent positionComponent) {
		final ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectGameComponent.class);
		addParticleEffectGame(particleEffect, positionComponent, pec);
	}
	/**
	 * Adds a particle effect at the given position, with screen coordinates.
	 * @param particleEffect Particle effect to add.
	 * @param positionComponent Location where particle effect will be placed.
	 */
	public static void addParticleEffectScreen(EParticleEffect particleEffect,PositionComponent positionComponent) {
		final ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectScreenComponent.class);
		addParticleEffectGame(particleEffect, positionComponent, pec);
	}

	private static void addParticleEffectGame(EParticleEffect particleEffect,PositionComponent positionComponent, ParticleEffectComponent pec) {
		final Entity deathEffect = gameEntityEngine.createEntity();
		final TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);
		pec.setup(ResourcePool.obtainParticleEffect(particleEffect));
		deathEffect.add(tc);
		deathEffect.add(pec);
		deathEffect.add(positionComponent);
		gameEntityEngine.addEntity(deathEffect);
	}

	
}
