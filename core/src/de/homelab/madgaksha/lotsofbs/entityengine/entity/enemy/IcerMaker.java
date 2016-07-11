package de.homelab.madgaksha.lotsofbs.entityengine.entity.enemy;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.statusScreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityFieldComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletShapeMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IBehaving;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.NormalEnemyMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.trajectory.LinearAtTargetTrajectory;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.field.SpiralVelocityField;
import de.homelab.madgaksha.lotsofbs.level.MapData;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimationList;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.util.InclusiveRange;

/**
 * Additional properties:
 * <ul>
 * <li>speedRadial</li>
 * <li>speedTangential</li>
 * <li>minDistance</li>
 * <li>changeDirectionChance</li>
 * <li>bulletIntervalMin</li>
 * <li>bulletIntervalMax</li>
 * <li>bulletAngularSpeed</li>
 * <li>bulletLifeTime</li>
 * <li>bulletVelocity</li>
 * <li>bulletScale</li>
 * </ul>
 * 
 * @author madgaksha
 *
 */
public class IcerMaker extends NormalEnemyMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(IcerMaker.class);
	private final static ESound soundList[] = new ESound[] { ESound.ICER_TOKUGI_0, ESound.ICER_TOKUGI_1, };
	private final static float intervalRatio[] = new float[] { 0.45f, 1.0f, };
	private final static ETexture signList[] = new ETexture[] { ETexture.ICER_TOKUGI_SIGN_0,
			ETexture.ICER_TOKUGI_SIGN_1, };
	private final static BulletShapeMaker[][] bulletListList = {
			new BulletShapeMaker[] { BulletShapeMaker.NOTE_RED, BulletShapeMaker.NOTE_BLUE,
					BulletShapeMaker.NOTE_YELLOW, BulletShapeMaker.NOTE_PINK, },
			new BulletShapeMaker[] { BulletShapeMaker.PLASMA_RED, BulletShapeMaker.PLASMA_BLUE,
					BulletShapeMaker.PLASMA_GREEN, BulletShapeMaker.PLASMA_YELLOW, } };

	// Singleton
	private static class SingletonHolder {
		private static final IcerMaker INSTANCE = new IcerMaker();
	}

	public static IcerMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private IcerMaker() {
		super();
	}

	@Override
	protected IBehaving getBehaviour(MapProperties props) {
		return new MyBehaviour(props);
	}

	@Override
	protected EAnimationList requestedAnimationListNormal() {
		return EAnimationList.ICER_0;
	}

	@Override
	protected EAnimationList requestedAnimationListDamage() {
		return EAnimationList.ICER_1;
	}

	@Override
	protected ESound requestedVoiceOnSpawn() {
		return ESound.HUMMING1;
	}

	@Override
	protected ESound requestedVoiceOnLightDamage() {
		return ESound.BEEP1;
	}

	@Override
	protected ESound requestedVoiceOnHeavyDamage() {
		return ESound.SAW2;
	}

	@Override
	protected ESound requestedVoiceOnBattleModeStart() {
		return ESound.SAW1;
	}

	@Override
	protected ESound requestedVoiceOnDeath() {
		return ESound.CRYING1;
	}

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.ICER_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.ICER_SUB;
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedAdditionalResources() {
		return new IResource<?, ?>[] { ESound.CRYING1, ESound.SAW1, ESound.SAW2, ESound.BEEP1, ESound.HUMMING1,
				ESound.ICER_TOKUGI_0, ESound.ICER_TOKUGI_1, ETexture.ICER_TOKUGI_SIGN_0, ETexture.ICER_TOKUGI_SIGN_1, };
	}

	@Override
	protected Rectangle requestedBoundingBoxRender() {
		return new Rectangle(-64.0f, -64.0f, 128.0f, 128.0f);
	}

	@Override
	protected Rectangle requestedBoundingBoxCollision() {
		return new Rectangle(-64.0f, -64.0f, 128.0f, 128.0f);
	}

	@Override
	protected Circle requestedBoundingCircle() {
		return new Circle(0.0f, 0.0f, 91.0f);
	}

	@Override
	protected long requestedMaxPainPoints() {
		return 6000000L;
	}

	@Override
	protected float requestedBulletAttack() {
		return 0.5f;
	}

	@Override
	protected float requestedBulletResistance() {
		return 5.0f;
	}

	@Override
	protected InclusiveRange<Long> requestedScoreOnKill() {
		return new InclusiveRange<Long>(2000L, 3000L);
	}

	@Override
	protected void spawned(Entity e, ETrigger t) {
	}

	@Override
	public void setup(Entity e, Shape2D shape, MapProperties props, ETrigger spawn, Vector2 initialPos, Float initDir,
			Float tileRadius) {
		float speedRadial = MapData.getNumber(props, "speedRadial", 50.0f);
		float speedTangential = MapData.getNumber(props, "speedTangential", 100.0f);
		float minDistance = MapData.getNumber(props, "minDistance", 256.0f);
		float changeDirectionChance = MapData.getNumber(props, "changeDirectionChance", 0.3f);

		super.setup(e, shape, props, spawn, initialPos, initDir, tileRadius);
		PositionComponent target = Mapper.positionComponent.get(playerHitCircleEntity);
		VelocityComponent vc = gameEntityEngine.createComponent(VelocityComponent.class);
		VelocityFieldComponent vfc = gameEntityEngine.createComponent(VelocityFieldComponent.class);

		vc.setup(new Vector2(speedRadial, 0.0f).rotate(MathUtils.random(360.0f)));
		vfc.field = new SpiralVelocityField(target, speedRadial, speedTangential, minDistance, changeDirectionChance);

		e.add(vc);
		e.add(vfc);
	}

	private static class MyBehaviour implements IBehaving {
		private final static long bulletPower = 3200000L;
		private final LinearAtTargetTrajectory linearAtPlayerTrajectory = new LinearAtTargetTrajectory();
		private float remainingTime;
		private float bulletIntervalMin;
		private float bulletIntervalMax;
		private float bulletAngularSpeed;
		private float bulletLifeTime;
		private float bulletVelocity;
		private float bulletScale;
		private boolean showSign;

		public MyBehaviour(MapProperties props) {
			bulletIntervalMin = MapData.getNumber(props, "bulletIntervalMin", 7.5f);
			bulletIntervalMax = MapData.getNumber(props, "bulletIntervalMax", 10.0f);
			bulletAngularSpeed = MapData.getNumber(props, "bulletAngularSpeed", 720.0f);
			bulletLifeTime = MapData.getNumber(props, "bulletLifeTime", 20.0f);
			bulletVelocity = MapData.getNumber(props, "bulletVelocity", 25.0f);
			bulletScale = MapData.getNumber(props, "bulletScale", 1.5f);
			showSign = MapData.getBoolean(props, "showSign", true);
			remainingTime = bulletIntervalMax;

			if (bulletIntervalMin >= bulletIntervalMax)
				bulletIntervalMin = 0.5f * bulletIntervalMax;
			bulletIntervalMin = Math.max(0.0f, bulletIntervalMin);
			bulletIntervalMax = Math.max(0.0f, bulletIntervalMax);
			bulletLifeTime = Math.max(0.0f, bulletLifeTime);
			bulletScale = Math.max(0.01f, bulletScale);

		}

		@Override
		public boolean behave(Entity enemy) {
			TemporalComponent tc = Mapper.temporalComponent.get(enemy);
			ComponentUtils.lookIntoMovementDirection(enemy);
			remainingTime -= tc.deltaTime;
			if (remainingTime < 0.0f) {
				int type = MathUtils.random(100 * bulletListList.length - 1) / 100;
				BulletShapeMaker bulletList[] = bulletListList[type];
				VoiceComponent vc = Mapper.voiceComponent.get(enemy);
				if (showSign) {
					statusScreen.addTokugiSign(signList[type]);
					vc.voicePlayer.playUnconditionally(soundList[type]);
				} else {
					vc.voicePlayer.play(ESound.THUMB1);
				}
				linearAtPlayerTrajectory.velocity(bulletVelocity);
				linearAtPlayerTrajectory.angularSpeed(bulletAngularSpeed);
				linearAtPlayerTrajectory.position(enemy);
				linearAtPlayerTrajectory.life(bulletLifeTime);
				linearAtPlayerTrajectory.target(playerHitCircleEntity);
				linearAtPlayerTrajectory.scale(bulletScale);
				BulletShapeMaker bulletShape = bulletList[MathUtils.random(bulletList.length - 1)];
				BulletMaker.makeForEnemy(enemy, bulletShape, linearAtPlayerTrajectory, bulletPower);
				remainingTime = intervalRatio[type] * MathUtils.random(bulletIntervalMin, bulletIntervalMax);
			}
			return true;
		}
	}
}
