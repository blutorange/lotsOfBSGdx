package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.battleModeActive;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DeathComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectGameComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.StickyComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerScreenComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxMapComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup03Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup04Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup05Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder0Component;
import de.homelab.madgaksha.lotsofbs.enums.ECollisionGroup;
import de.homelab.madgaksha.lotsofbs.enums.TrackingOrientationStrategy;
import de.homelab.madgaksha.lotsofbs.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.lotsofbs.grantstrategy.ImmediateGrantStrategy;
import de.homelab.madgaksha.lotsofbs.level.ALevel;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;
import de.homelab.madgaksha.lotsofbs.util.GeoUtil;
import javafx.geometry.Pos;

public final class MakerUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MakerUtils.class);

	private MakerUtils() {
	}

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
	 * 
	 * @param shape
	 *            The shape.
	 * @param center
	 *            The center.
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

	public static Component makeTrigger(ITrigger triggerAcceptingObject, IReceive triggerReceivingObject,
			ETrigger trigger, ECollisionGroup group) {
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
	 * Sets up the family for camera tracking focus points and adds the
	 * appropriate listeners for when new enemies appear and leave.
	 * 
	 * @return The family for the focus points.
	 */
	public static Family makeFocusPointsFamily() {
		@SuppressWarnings("unchecked")
		final Family family = Family.all(PositionComponent.class, CameraFocusComponent.class)
				.exclude(InactiveComponent.class).get();
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
					int index = Math.min(cameraTrackingComponent.trackedPointIndex, focusPointsSize - 1);
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

		final ManyTrackingComponent mtc = new ManyTrackingComponent(level.getMapXW(), level.getMapYW(),
				level.getMapWidthW(), level.getMapHeightW());
		mtc.minimumElevation = level.getMapData().getMinimumCameraElevation();
		mtc.maximumElevation = level.getMapData().getMaximumCameraElevation();
		mtc.focusPoints = gameEntityEngine.getEntitiesFor(familyFocusPoints);
		mtc.playerPoint = playerE;
		mtc.baseDirection = level.getMapData().getBaseDirection();
		mtc.gravity = level.getMapData().getPreferredPlayerLocation();
		mtc.trackingOrientationStrategy = TrackingOrientationStrategy.RELATIVE;

		cameraTrackingComponent = mtc;

		myCamera.add(mtc);
		myCamera.add(new PositionComponent(viewportGame.getCamera().position.x, viewportGame.getCamera().position.y,
				viewportGame.getCamera().position.z));
		myCamera.add(new RotationComponent(viewportGame.getRotationUpXY()));
		myCamera.add(new ShouldPositionComponent(new ExponentialGrantStrategy(0.6f, 0.25f)));
		myCamera.add(new ShouldRotationComponent(new ExponentialGrantStrategy(0.6f, 0.25f)));
		myCamera.add(new ViewportComponent(viewportGame));
		myCamera.add(new TemporalComponent());
		myCamera.add(new TimeScaleComponent());
		return myCamera;
	}

	/**
	 * Injects an entity into the entity system that will be called after the
	 * given amount of time.
	 * 
	 * @param f
	 *            Duration in seconds.
	 * @param iTimedCallback
	 *            Callback to call.
	 */
	public static void addTimedRunnable(float duration, IEntityCallback timedCallback) {
		final Entity e = gameEntityEngine.createEntity();
		addTimedRunnableTo(e, duration, timedCallback, null, true);
		gameEntityEngine.addEntity(e);
	}
	
	public static void addTimedRunnable(float duration, IEntityCallback timedCallback, Object data) {
		final Entity e = gameEntityEngine.createEntity();
		addTimedRunnableTo(e, duration, timedCallback, data, true);
		gameEntityEngine.addEntity(e);
	}

	public static void addTimedRunnableTo(Entity e, float duration, IEntityCallback timedCallback) {
		addTimedRunnableTo(e, duration, timedCallback, null);		
	}
	
	public static void addTimedRunnableTo(Entity e, float duration, IEntityCallback callback, Object data) {
		addTimedRunnableTo(e, duration, callback, data, false);
	}
	public static void addTimedRunnableTo(Entity e, float duration, IEntityCallback timedCallback, Object data, boolean removeEntityOnCompletion) {
		TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);
		TimedCallbackComponent tcc = gameEntityEngine.createComponent(TimedCallbackComponent.class);
		tcc.setup(timedCallback, data, duration, 1, removeEntityOnCompletion);
		e.add(tc).add(tcc);
	}

	/**
	 * Adds a particle effect at the given position, with game coordiantes.
	 * 
	 * @param particleEffect
	 *            Particle effect to add.
	 * @param positionComponent
	 *            Location where particle effect will be placed.
	 */
	public static void addParticleEffectGame(EParticleEffect particleEffect, PositionComponent positionComponent) {
		final ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectGameComponent.class);
		addParticleEffect(particleEffect, positionComponent, pec, null);
	}

	public static void addParticleEffectGame(EParticleEffect particleEffect, PositionComponent positionComponent,
			IEntityCallback callback) {
		final ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectGameComponent.class);
		addParticleEffect(particleEffect, positionComponent, pec, callback);
	}

	/**
	 * Adds a particle effect at the given position, with screen coordinates.
	 * 
	 * @param particleEffect
	 *            Particle effect to add.
	 * @param positionComponent
	 *            Location where particle effect will be placed.
	 * @return The newly created particle effect component.
	 */
	public static void addParticleEffectScreen(EParticleEffect particleEffect, PositionComponent positionComponent) {
		final ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectScreenComponent.class);
		addParticleEffect(particleEffect, positionComponent, pec, null);
	}

	public static void addParticleEffectScreen(EParticleEffect particleEffect, PositionComponent positionComponent,
			IEntityCallback callback) {
		final ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectScreenComponent.class);
		addParticleEffect(particleEffect, positionComponent, pec, callback);
	}

	private static void addParticleEffect(EParticleEffect particleEffect, PositionComponent positionComponent,
			ParticleEffectComponent pec, IEntityCallback callback) {
		final Entity entity = gameEntityEngine.createEntity();
		final TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);
		final PositionComponent pc = gameEntityEngine.createComponent(PositionComponent.class);
		pc.setup(positionComponent);
		pec.setup(ResourcePool.obtainParticleEffect(particleEffect));
		pec.callback = REMOVE_ENTITY;
		pec.callbackData = callback;
		entity.add(tc);
		entity.add(pec);
		entity.add(pc);
		gameEntityEngine.addEntity(entity);
	}

	private final static IEntityCallback REMOVE_ENTITY = new IEntityCallback() {
		@Override
		public void run(Entity entity, Object data) {
			if (data != null && (data instanceof IEntityCallback)) ((IEntityCallback)data).run(entity, null);
			gameEntityEngine.removeEntity(entity);
		}
	};
}
