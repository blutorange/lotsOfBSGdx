package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.musicPlayer;
import static de.homelab.madgaksha.GlobalBag.playerEntity;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InputDesktopComponent;
import de.homelab.madgaksha.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TriggerScreenComponent;
import de.homelab.madgaksha.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup03Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup04Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup05Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup01Component;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.enums.TrackingOrientationStrategy;
import de.homelab.madgaksha.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.util.GeoUtil;

public final class MakerUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MakerUtils.class);
	private MakerUtils(){}

	public static BoundingBoxComponent makeBoundingBox(Shape2D shape) {
		return new BoundingBoxComponent(GeoUtil.getBoundingBox(shape));
	}

	public static PositionComponent makePositionAtCenter(Shape2D shape) {
		final PositionComponent pc = new PositionComponent();
		if (shape instanceof Rectangle) {
			Rectangle r = (Rectangle)shape;
			pc.x = r.x + 0.5f * r.width;
			pc.y = r.y + 0.5f * r.height;
		}
		else if (shape instanceof Circle) {
			pc.x = ((Circle)shape).x;
			pc.y = ((Circle)shape).y;
		}
		else if (shape instanceof Ellipse) {
			Ellipse e = (Ellipse)shape;
			pc.x = e.x + 0.5f * e.width;
			pc.y = e.y + 0.5f * e.height;
		}
		else if (shape instanceof Polygon) {
			Polygon p = (Polygon)shape;
			Rectangle r = p.getBoundingRectangle();
			pc.x = r.x + 0.5f * r.width;
			pc.y = r.y + 0.5f * r.height;
		}
		else if (shape instanceof Polyline) {
			Polyline pl = (Polyline)shape;
			Polygon pp = new Polygon(pl.getVertices());
			Rectangle r = pp.getBoundingRectangle();
			pc.x = r.x + 0.5f * r.width;
			pc.y = r.y + 0.5f * r.height;
		}
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
	
	public static Entity makePlayer(APlayer player) {
		playerEntity = new Entity();

		SpriteForDirectionComponent sfdc = new SpriteForDirectionComponent(player.getAnimationList(),
				ESpriteDirectionStrategy.ZENITH);
		SpriteAnimationComponent sac = new SpriteAnimationComponent(sfdc);
		SpriteComponent sc = new SpriteComponent(sac);
		ShadowComponent kc = player.makeShadow();
		
		switch (Gdx.app.getType()) {
		case Android:
			//TODO
			break;
		case Applet:
			//TODO
			break;
		case Desktop:
			InputDesktopComponent ic = new InputDesktopComponent();
			ic.lowSpeed = player.getMovementSpeedLow();
			ic.highSpeed = player.getMovementSpeedHigh();
			ic.frictionFactor = player.getMovementFrictionFactor();
			ic.accelerationFactor= player.getMovementAccelerationFactor();
			playerEntity.add(ic);
			break;
		case HeadlessDesktop:
			//TODO
			break;
		case WebGL:
			//TODO
			break;
		case iOS:
			//TODO
			break;
		default:
			//TODO
			break;
		
		}
		
		playerEntity.add(new LeanEffectComponent(30.0f,0.10f,0.0001f));
		playerEntity.add(new HoverEffectComponent(8.0f, 1.0f));
		
		playerEntity.add(new ShouldRotationComponent(new ExponentialGrantStrategy(0.1f)));
		playerEntity.add(new ShouldScaleComponent(new ExponentialGrantStrategy(0.1f)));
			
		playerEntity.add(sc);
		playerEntity.add(sac);
		playerEntity.add(sfdc);

		//playerEntity.add(new CameraFocusComponent());
		playerEntity.add(new TriggerTouchGroup01Component());
		playerEntity.add(new BoundingSphereComponent(player.getBoundingCircle()));
		playerEntity.add(new BoundingBoxComponent(player.getBoundingBox()));
		playerEntity.add(new PositionComponent(level.getMapData().getPlayerInitialPosition(), true));
		playerEntity.add(new VelocityComponent(0.0f, 0.0f));
		playerEntity.add(new RotationComponent(true));
		playerEntity.add(new ScaleComponent());
		playerEntity.add(new DirectionComponent(level.getMapData().getPlayerInitialDirection()));		
		playerEntity.add(new TemporalComponent());
		
		if (kc != null) playerEntity.add(kc);

		return playerEntity;
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
				if (cameraTrackingComponent.focusPoints.size() == 0) {
					musicPlayer.loadNext(level.getBgm());
					musicPlayer.setCrossFade(true);
					musicPlayer.transition(2.0f);
				}
			}
			@Override
			public void entityAdded(Entity entity) {
				if (cameraTrackingComponent.focusPoints.size() == 1) {
					musicPlayer.loadNext(level.getBattleBgm());
					musicPlayer.setCrossFade(true);
					musicPlayer.transition(2.0f);
				}
			}
		});
		return family;
	}
	
	public static Entity makeCamera(ALevel level) {
		final Entity myCamera = new Entity();
		final Family familyFocusPoints = makeFocusPointsFamily();
		
		final ManyTrackingComponent mtc = new ManyTrackingComponent(level.getMapXW(), level.getMapYW(), level.getMapWidthW(),
				level.getMapHeightW());
		mtc.minimumElevation = level.getMapData().getMinimumCameraElevation();
		mtc.maximumElevation = level.getMapData().getMaximumCameraElevation();
		mtc.focusPoints = gameEntityEngine.getEntitiesFor(familyFocusPoints);
		mtc.playerPoint = playerEntity;
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
		
		return myCamera;
	}
}
