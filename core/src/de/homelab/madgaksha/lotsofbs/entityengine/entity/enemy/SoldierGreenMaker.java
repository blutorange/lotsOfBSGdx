package de.homelab.madgaksha.lotsofbs.entityengine.entity.enemy;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.audiosystem.VoicePlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletShapeMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IBehaving;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.NormalEnemyMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.trajectory.FadeInAimTrajectory;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.trajectory.LinearMotionTrajectory;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimationList;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.util.InclusiveRange;

public class SoldierGreenMaker extends NormalEnemyMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SoldierGreenMaker.class);

	private final LinearMotionTrajectory linearMotionTrajectory;
	private final FadeInAimTrajectory fadeInAimTrajectory;
	private final VoicePlayer voicePlayer = new VoicePlayer();

	// Singleton
	private static class SingletonHolder {
		private static final SoldierGreenMaker INSTANCE = new SoldierGreenMaker();
	}

	public static SoldierGreenMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private SoldierGreenMaker() {
		super();
		linearMotionTrajectory = new LinearMotionTrajectory();
		linearMotionTrajectory.life(10.0f);
		linearMotionTrajectory.angularSpeed(540.0f);

		fadeInAimTrajectory = new FadeInAimTrajectory();
		fadeInAimTrajectory.timeFadeIn(0.1f);
		fadeInAimTrajectory.life(8.5f);
		fadeInAimTrajectory.velocityAim(250.0f);
		fadeInAimTrajectory.soundOnFire(ESound.ENEMY_SPAWN_FLASH);
		fadeInAimTrajectory.voicePlayer(new VoicePlayer());
		fadeInAimTrajectory.waveAmplitude(50f);
		fadeInAimTrajectory.waveFrequency(0.273f);

	}

	@SuppressWarnings("unchecked")
	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedAdditionalResources() {
		return new IResource[] { EAnimationList.SOLDIER_GREEN_0, ESound.HORA_KOCCHI_DA_ZE, ESound.NURRGH, ESound.UAARGH,
				ESound.NURUKATTA_KA, BulletShapeMaker.FLOWER_BLACK.getResource(),
				BulletShapeMaker.FLOWER_RED.getResource(), BulletShapeMaker.FLOWER_BLUE.getResource(),
				BulletShapeMaker.FLOWER_CYAN.getResource(), BulletShapeMaker.FLOWER_GREEN.getResource(),
				BulletShapeMaker.FLOWER_PINK.getResource(), BulletShapeMaker.FLOWER_WHITE.getResource(),
				BulletShapeMaker.FLOWER_YELLOW.getResource(), };
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
		return ETexture.SOLDIER_GREEN_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.SOLDIER_GREEN_SUB;
	}

	@Override
	protected long requestedMaxPainPoints() {
		return 100000000L / 3L;
	}

	@Override
	protected IBehaving getBehaviour(MapProperties props) {
		return new IBehaving() {
			private Vector2 v = new Vector2(350.0f, 0.0f);
			private Vector2 w = new Vector2();
			private BulletShapeMaker[] shapes = new BulletShapeMaker[] { BulletShapeMaker.FLOWER_BLUE,
					BulletShapeMaker.FLOWER_CYAN, BulletShapeMaker.FLOWER_GREEN, BulletShapeMaker.FLOWER_PINK,
					BulletShapeMaker.FLOWER_RED, BulletShapeMaker.FLOWER_WHITE, BulletShapeMaker.FLOWER_YELLOW };
			private final BulletShapeMaker[] shapes2 = new BulletShapeMaker[] { BulletShapeMaker.PACMAN_BLUE,
					BulletShapeMaker.PACMAN_GREEN, BulletShapeMaker.PACMAN_ORANGE, BulletShapeMaker.PACMAN_RED,
					BulletShapeMaker.PACMAN_PINK, BulletShapeMaker.PACMAN_YELLOW, BulletShapeMaker.PACMAN_BLACK, };

			@Override
			public boolean behave(Entity enemy) {
				final PositionComponent pcEnemy = Mapper.positionComponent.get(enemy);
				final TemporalComponent tc = Mapper.temporalComponent.get(enemy);
				if (tc.totalTime > 2.5f) {
					voicePlayer.play(ESound.ENEMY_DIE_EXPLOSION);
					final PositionComponent pcPlayer = Mapper.positionComponent.get(GlobalBag.playerEntity);
					float angle = w.set(pcPlayer.x, pcPlayer.y).sub(pcEnemy.x, pcEnemy.y).angle();
					tc.totalTime = 0.0f;
					linearMotionTrajectory.position(pcEnemy.x, pcEnemy.y);
					for (int i = 0; i != 60; ++i) {
						v.set(250.0f, 0.0f).rotate(angle - 80.0f + 160.0f * i / 39.0f);
						linearMotionTrajectory.velocity(v.x, v.y);
						BulletMaker.makeForEnemy(enemy, shapes[MathUtils.random(shapes.length - 1)],
								linearMotionTrajectory, 23000000L);
					}
				}
				for (int i = 0; i != 4; ++i) {
					v.set(250.0f, 0.0f).rotate(MathUtils.random(0.0f, 360.0f));
					linearMotionTrajectory.velocity(v.x, v.y);
					BulletMaker.makeForEnemy(enemy, BulletShapeMaker.FLOWER_BLACK, linearMotionTrajectory, 7000000L);

					fadeInAimTrajectory.waveAmplitude(45.0f);
					fadeInAimTrajectory.position(pcEnemy.x, pcEnemy.y);
					fadeInAimTrajectory.aim(playerEntity);
					BulletMaker.makeForEnemy(enemy, shapes2[MathUtils.random(shapes2.length - 1)], fadeInAimTrajectory,
							500000L);
				}
				return true;
			}
		};
	}

	@Override
	protected EAnimationList requestedAnimationListNormal() {
		return EAnimationList.SOLDIER_GREEN_0;
	}

	@Override
	protected EAnimationList requestedAnimationListDamage() {
		return EAnimationList.SOLDIER_GREEN_1;
	}

	@Override
	protected float requestedBulletAttack() {
		return 1.2f;
	}

	@Override
	protected float requestedBulletResistance() {
		return 0.8f;
	}

	@Override
	protected ESound requestedVoiceOnSpawn() {
		return ESound.HORA_KOCCHI_DA_ZE;
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
		return new InclusiveRange<Long>(2000L, 3000L);
	}
}
