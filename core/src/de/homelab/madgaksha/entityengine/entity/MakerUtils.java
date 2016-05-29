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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.entityengine.component.ColorComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InputDesktopComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.entityengine.component.ManyTrackingComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.entityengine.component.StickyEffectComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TriggerScreenComponent;
import de.homelab.madgaksha.entityengine.component.TriggerStartupComponent;
import de.homelab.madgaksha.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.ViewportComponent;
import de.homelab.madgaksha.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxMapComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;
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

	public static BoundingBoxRenderComponent makeBoundingBoxRender(Shape2D shape) {
		return new BoundingBoxRenderComponent(GeoUtil.getBoundingBox(shape));
	}
	public static BoundingBoxMapComponent makeBoundingBoxMap(Shape2D shape) {
		return new BoundingBoxMapComponent(GeoUtil.getBoundingBox(shape));
	}
	public static BoundingBoxCollisionComponent makeBoundingBoxCollision(Shape2D shape) {
		return new BoundingBoxCollisionComponent(GeoUtil.getBoundingBox(shape));
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
	
	public static Entity makeEnemyTargetCross() {
		Entity targetCross = new Entity();
		AngularVelocityComponent avc = new AngularVelocityComponent(level.getEnemyTargetCrossAngularVelocity());
		InactiveComponent iac = new InactiveComponent();
		InvisibleComponent ivc = new InvisibleComponent();
		PositionComponent pc = new PositionComponent();
		RotationComponent rc = new RotationComponent();
		SpriteComponent sc = new SpriteComponent(level.getEnemyTargetCrossTexture());
		StickyEffectComponent sec = new StickyEffectComponent();
		ScaleComponent slc = new ScaleComponent(0.0f, 0.0f, level.getEnemyTargetCrossTexture().getOriginalScale());
		ShouldScaleComponent ssc = new ShouldScaleComponent(new ExponentialGrantStrategy(0.99f, 0.05f));
		TemporalComponent tc = new TemporalComponent();

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
		targetCross.add(ssc);
		targetCross.add(tc);
		
		return targetCross; 
	}

	
	public static Entity makePlayerBattleStigma(Entity playerE, APlayer playerA) {
		Entity battleStigma = new Entity();
		Rectangle bbm = playerA.getBoundingBoxMap();
		
		AngularVelocityComponent avc = new AngularVelocityComponent(playerA.getBattleStigmaAngularVelocity());
		ColorComponent cc = new ColorComponent();
		InactiveComponent iac = new InactiveComponent();
		InvisibleComponent ivc = new InvisibleComponent();
		PositionComponent pc = new PositionComponent(playerE);
		RotationComponent rc = new RotationComponent();
		SpriteComponent sc = new SpriteComponent(playerA.getBattleStigmaTexture());
		StickyEffectComponent sec = new StickyEffectComponent(playerE, bbm.x+0.5f*bbm.width, bbm.y+0.5f*bbm.height, true, true);
		ScaleComponent slc = new ScaleComponent(0.0f, 0.0f, playerA.getBattleStigmaTexture().getOriginalScale());
		ShouldScaleComponent ssc = new ShouldScaleComponent(new ExponentialGrantStrategy(0.5f, 0.5f));
		TemporalComponent tc = new TemporalComponent();

		battleStigma.add(avc);
		battleStigma.add(cc);
		battleStigma.add(ivc);
		battleStigma.add(iac);
		battleStigma.add(pc);
		battleStigma.add(rc);
		battleStigma.add(sc);
		battleStigma.add(sec);
		battleStigma.add(slc);
		battleStigma.add(ssc);
		battleStigma.add(tc);
		
		
		return battleStigma; 
	}
	
	public static Entity makePlayerHitCircle(Entity playerE, APlayer playerA) {
		Entity hitCircle = new Entity();
		Rectangle bbc = playerA.getBoundingBoxCollision();
		
		PositionComponent pc = new PositionComponent(playerE);
		SpriteComponent sc = new SpriteComponent(playerA.getHitCircleTexture());
		StickyEffectComponent sec = new StickyEffectComponent(playerE, bbc.x+0.5f*bbc.width, bbc.y+0.5f*bbc.height);
		InvisibleComponent ivc = new InvisibleComponent();
		
		if (pc == null || sc == null || sec == null || ivc == null) return null;
		
		hitCircle.add(pc);
		hitCircle.add(sc);
		hitCircle.add(sec);
		hitCircle.add(ivc);
		
		return hitCircle; 
	}
	
	public static Entity makePlayer(APlayer player) {
		Entity e = new Entity();
	
		switch (Gdx.app.getType()) {
		case Android:
			//TODO
			break;
		case Applet:
			//TODO
			break;
		case Desktop:
			InputDesktopComponent ic = new InputDesktopComponent();
			ic.frictionFactor = player.getMovementFrictionFactor();
			ic.accelerationFactorLow = player.getMovementAccelerationFactorLow();
			ic.accelerationFactorHigh = player.getMovementAccelerationFactorHigh();
			ic.battleSpeedLow = player.getMovementBattleSpeedLow();
			ic.battleSpeedHigh = player.getMovementBattleSpeedHigh();
			e.add(ic);
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
		
		SpriteForDirectionComponent sfdc = new SpriteForDirectionComponent(player.getAnimationList(),
				ESpriteDirectionStrategy.ZENITH);
		Shape2D exactShape = player.getExactShapeCollision();
		ShapeComponent shc = null;
		
		SpriteAnimationComponent sac = new SpriteAnimationComponent(sfdc);
		SpriteComponent sc = new SpriteComponent(sac, player.getSpriteOrigin());
		ShadowComponent kc = player.makeShadow();

		ABoundingBoxComponent bbcc = new BoundingBoxCollisionComponent(player.getBoundingBoxCollision());
		ABoundingBoxComponent bbrc = new BoundingBoxRenderComponent(player.getBoundingBoxRender());
		ABoundingBoxComponent bbmc = new BoundingBoxMapComponent(player.getBoundingBoxMap());
		BoundingSphereComponent bsc = new BoundingSphereComponent(player.getBoundingCircle());
		if (exactShape != null) shc = new ShapeComponent(exactShape);

		StatusValuesComponent svc = new StatusValuesComponent(player.getBulletAttack(), player.getBulletResistance());
		PainPointsComponent ppc = new PainPointsComponent(player.getMaxPainPoints());
		DamageQueueComponent dqc = new DamageQueueComponent();
		TemporalComponent tc = new TemporalComponent();
		
		PositionComponent pc = new PositionComponent(level.getMapData().getPlayerInitialPosition(), true);
		VelocityComponent vc = new VelocityComponent(0.0f, 0.0f);
		RotationComponent rc = new RotationComponent(true);
		DirectionComponent dc = new DirectionComponent(level.getMapData().getPlayerInitialDirection());
		ScaleComponent slc = new ScaleComponent();
		
		ShouldRotationComponent src = new ShouldRotationComponent(new ExponentialGrantStrategy(0.1f));
		ShouldScaleComponent ssc = new ShouldScaleComponent(new ExponentialGrantStrategy(0.1f));
		
		HoverEffectComponent hec = new HoverEffectComponent(8.0f, 1.0f);
		LeanEffectComponent lec = new LeanEffectComponent(30.0f,0.10f,0.0001f);		
			
		TriggerTouchComponent ttg1c = new TriggerTouchGroup01Component();
		
		VoiceComponent vcc = new VoiceComponent();
		vcc.onBattleModeStart = player.getVoiceOnBattleStart();
		vcc.onLightDamage = player.getVoiceOnLightDamage();
		vcc.onHeavyDamage = player.getVoiceOnHeavyDamage();
		vcc.onDeath = player.getVoiceOnDeath();
		vcc.voicePlayer = new VoicePlayer();
		
		e.add(vcc);
		e.add(lec);
		e.add(hec);
		e.add(src);
		e.add(ssc);			
		e.add(sc);
		e.add(sac);
		e.add(sfdc);
		e.add(ttg1c);
		e.add(bsc);
		e.add(bbcc);
		e.add(bbmc);
		e.add(bbrc);
		e.add(pc);
		e.add(vc);
		e.add(rc);
		e.add(slc);
		e.add(dc);		
		e.add(tc);
		e.add(dqc);
		e.add(ppc);
		e.add(svc);
		if (kc != null) e.add(kc);
		if (shc != null) e.add(shc);
		
		return e;
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
			// start playing normal bgm again
			@Override
			public void entityRemoved(Entity entity) {
				// Battle mode left.
				if (cameraTrackingComponent.focusPoints.size() == 0) {
					EnemyMaker.exitBattleMode();
				}
			}
			// start playing battle bgm
			@Override
			public void entityAdded(Entity entity) {
				// Battle mode entered
				if (!battleModeActive) {
					EnemyMaker.enterBattleMode(entity);
					EnemyMaker.targetSwitched(entity);
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
		
		return myCamera;
	}

}
