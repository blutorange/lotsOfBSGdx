package de.homelab.madgaksha.lotsofbs.entityengine.entity.enemy;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.audiosystem.VoicePlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletShapeMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IBehaving;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.NormalEnemyMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.trajectory.FadeInAimTrajectory;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimationList;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.util.InclusiveRange;

public class SoldierRedMaker extends NormalEnemyMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SoldierRedMaker.class);

	private final BulletShapeMaker[] BULLET_SHAPE_LIST = new BulletShapeMaker[] { BulletShapeMaker.PACMAN_BLUE,
			BulletShapeMaker.PACMAN_GREEN, BulletShapeMaker.PACMAN_ORANGE, BulletShapeMaker.PACMAN_RED,
			BulletShapeMaker.PACMAN_PINK, BulletShapeMaker.PACMAN_YELLOW, BulletShapeMaker.PACMAN_BLACK, };

	// Singleton
	private static class SingletonHolder {
		private static final SoldierRedMaker INSTANCE = new SoldierRedMaker();
	}

	public static SoldierRedMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private SoldierRedMaker() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedAdditionalResources() {
		return new IResource[] { EAnimationList.SOLDIER_RED_0, ESound.HOOORGH, ESound.NURRGH, ESound.UAARGH,
				ESound.NURUKATTA_KA, BulletShapeMaker.PACMAN_LIGHTYELLOW.getResource(), };
	}

	@Override
	public void setup(Entity e, Shape2D shape, MapProperties props, ETrigger spawn, Vector2 initialPos, Float initDir,
			Float tileRadius) {
		super.setup(e, shape, props, spawn, initialPos, initDir, tileRadius);
	}

	@Override
	protected Rectangle requestedBoundingBoxRender() {
		return new Rectangle(-32.0f, -32.0f, 64.0f, 64.0f);
	}

	@Override
	protected Rectangle requestedBoundingBoxCollision() {
		return new Rectangle(-32.0f, -32.0f, 64.0f, 64.0f);
	}

	@Override
	protected Circle requestedBoundingCircle() {
		return new Circle(0.0f, 0.0f, 32.0f);
	}

	@Override
	protected void spawned(Entity e, ETrigger t) {
	}

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.SOLDIER_RED_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.SOLDIER_RED_SUB;
	}

	@Override
	protected long requestedMaxPainPoints() {
		return 4000000L;
	}

	@Override
	protected IBehaving getBehaviour(MapProperties props) {
		final FadeInAimTrajectory fadeInAimTrajectory = new FadeInAimTrajectory();
		fadeInAimTrajectory.timeFadeIn(0.1f);
		fadeInAimTrajectory.life(6.5f);
		fadeInAimTrajectory.velocityAim(250.0f);
		fadeInAimTrajectory.soundOnFire(ESound.ENEMY_SPAWN_FLASH);
		fadeInAimTrajectory.voicePlayer(new VoicePlayer());
		fadeInAimTrajectory.waveAmplitude(50f);
		fadeInAimTrajectory.waveFrequency(0.273f);
		return new IBehaving() {
			private float timeBetweenBarrages = 1.5f;
			private float timeBetweenBullets = 0.1f;
			private float distanceBetweenBullets = 30.0f;
			private float distanceInitial = 40.0f;
			private int mode = 0;
			private BulletShapeMaker shape = BULLET_SHAPE_LIST[0];

			@Override
			public boolean behave(Entity enemy) {
				// Look at player.
				ComponentUtils.lookIntoDirection(enemy, playerEntity);
				// Danmaku.
				TemporalComponent tc = Mapper.temporalComponent.get(enemy);
				switch (mode) {
				case 0:
					tc.totalTime = 0.0f;
					mode = 1;
					shape = BULLET_SHAPE_LIST[MathUtils.random(BULLET_SHAPE_LIST.length - 1)];
					break;
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					if (tc.totalTime > timeBetweenBullets) {
						if (mode == 1)
							SoundPlayer.getInstance().play(ESound.ACTIVATE_ITEM);
						fadeInAimTrajectory.timeWaitAfterFadeIn(0.9f - 0.1f * mode);
						// Fire!
						tc.totalTime -= timeBetweenBullets;
						DirectionComponent dc = Mapper.directionComponent.get(enemy);
						PositionComponent pc = Mapper.positionComponent.get(enemy);
						float x = pc.x
								+ (mode * distanceBetweenBullets + distanceInitial) * MathUtils.cosDeg((-dc.degree));
						float y = pc.y
								+ (mode * distanceBetweenBullets + distanceInitial) * MathUtils.sinDeg((-dc.degree));
						fadeInAimTrajectory.waveAmplitude(15f * (mode - 1f));
						fadeInAimTrajectory.position(x, y);
						fadeInAimTrajectory.aim(playerEntity);
						BulletMaker.makeForEnemy(enemy, shape, fadeInAimTrajectory, 4000000L);

						fadeInAimTrajectory.position(pc.x + pc.x - x, pc.y + pc.y - y);
						BulletMaker.makeForEnemy(enemy, shape, fadeInAimTrajectory, 4000000L);

						++mode;
					}
					break;
				case 11:
					tc.totalTime = 0.0f;
					++mode;
					break;
				case 12:
				default:
					if (tc.totalTime > timeBetweenBarrages) {
						tc.totalTime = 0.0f;
						mode = 0;
					}
				}
				return true;
			}
		};
	}

	@Override
	protected EAnimationList requestedAnimationListNormal() {
		return EAnimationList.SOLDIER_RED_0;
	}

	@Override
	protected EAnimationList requestedAnimationListDamage() {
		return EAnimationList.SOLDIER_RED_1;
	}

	@Override
	protected float requestedBulletAttack() {
		return 0.8f;
	}

	@Override
	protected float requestedBulletResistance() {
		return 1.2f;
	}

	@Override
	protected ESound requestedVoiceOnSpawn() {
		return ESound.HOOORGH;
	}

	@Override
	protected ESound requestedVoiceOnLightDamage() {
		return ESound.NURRGH;
	}

	@Override
	protected ESound requestedVoiceOnHeavyDamage() {
		return ESound.NURUKATTA_KA;
	}

	@Override
	protected ESound requestedVoiceOnBattleModeStart() {
		return null;
	}

	@Override
	protected ESound requestedVoiceOnDeath() {
		return ESound.UAARGH;
	}

	@Override
	protected InclusiveRange<Long> requestedScoreOnKill() {
		return new InclusiveRange<Long>(1000L, 1500L);
	}
}