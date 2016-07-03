package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.battleModeActive;
import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.enemyKillCount;
import static de.homelab.madgaksha.GlobalBag.enemyTargetCrossEntity;
import static de.homelab.madgaksha.GlobalBag.game;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.gameScore;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.GlobalBag.playerEntity;
import static de.homelab.madgaksha.GlobalBag.playerHitCircleEntity;
import static de.homelab.madgaksha.GlobalBag.statusScreen;

import org.apache.commons.lang3.ArrayUtils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.AnyChildComponent;
import de.homelab.madgaksha.entityengine.component.BattleDistanceComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.BulletStatusComponent;
import de.homelab.madgaksha.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.DeathComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.EnemyIconComponent;
import de.homelab.madgaksha.entityengine.component.GetHitComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.SiblingComponent;
import de.homelab.madgaksha.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.entityengine.component.StickyComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder2Component;
import de.homelab.madgaksha.entityengine.entity.trajectory.HomingGrantTrajectory;
import de.homelab.madgaksha.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.entityengine.entityutils.SystemUtils;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.grantstrategy.IGrantStrategy;
import de.homelab.madgaksha.grantstrategy.SpeedIncreaseGrantStrategy;
import de.homelab.madgaksha.layer.BattleModeActivateLayer;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcepool.EParticleEffect;
import de.homelab.madgaksha.util.GeoUtil;
import de.homelab.madgaksha.util.InclusiveRange;

public abstract class EnemyMaker extends EntityMaker implements ITrigger, IReceive, IHittable, IMortal {
	private final static Logger LOG = Logger.getLogger(EnemyMaker.class);
	private final static HomingGrantTrajectory homingTrajectory = new HomingGrantTrajectory();

	protected EnemyMaker() {
		super();
	}

	/** Score bullets are taken randomly from this array. */
	private final static BulletShapeMaker[] scoreBulletShapes = new BulletShapeMaker[] { BulletShapeMaker.GEMLET_BLUE,
			BulletShapeMaker.GEMLET_RED, BulletShapeMaker.GEMLET_GREEN, BulletShapeMaker.GEMLET_BROWN, };

	/**
	 * Adds the appropriate components to an entity to be used as an enemy.
	 * 
	 * @param entity
	 *            Entity to setup.
	 * @param shape
	 *            Shape of the entity. Enemy will be positioned at the center of
	 *            its bounding box. If an offset is given, it is added to the
	 *            center. When trigger type is set to touch, the enemy will be
	 *            triggered when the player touches the shape.
	 * @param trigger
	 *            How the enemy should be triggered.
	 * @param initialPosition
	 *            Offset to the initial position, which defaults to the center
	 *            of the bounding box of shape defined in the map.
	 * @param initDir
	 *            Initial looking direction.
	 */
	public void setup(Entity entity, Shape2D shape, MapProperties props, ETrigger trigger, Vector2 initialPosition,
			Float initDir, Float tileRadius) {
		super.setup(entity);

		Float battleInDistance = null;
		Float battleOutDistance = null;
		if (props.containsKey("battleIn")) {
			try {
				battleInDistance = Float.valueOf(String.valueOf(props.get("battleIn")));
			} catch (NumberFormatException e) {
				LOG.error("could not read battleIn distance");
			}
		}
		if (props.containsKey("battleOut")) {
			try {
				battleOutDistance = Float.valueOf(String.valueOf(props.get("battleOut")));
			} catch (NumberFormatException e) {
				LOG.error("could not read battleIn distance");
			}
		}

		// Create components to be added.
		AnyChildComponent acc = new AnyChildComponent();
		Component tc = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);
		PositionComponent pcTrigger = MakerUtils.makePositionAtCenter(shape);
		TriggerTouchComponent ttgc = new TriggerTouchGroup02Component();
		ABoundingBoxComponent bbcEnemyRender = new BoundingBoxRenderComponent(requestedBoundingBoxRender());
		ABoundingBoxComponent bbcEnemyCollision = new BoundingBoxCollisionComponent(requestedBoundingBoxCollision());
		ABoundingBoxComponent bbcTrigger = new BoundingBoxCollisionComponent(GeoUtil.getBoundingBox(shape));
		BattleDistanceComponent bdc = new BattleDistanceComponent(playerEntity,
				tileRadius * requestedBattleInDistance(battleInDistance),
				tileRadius * requestedBattleOutDistance(battleOutDistance));
		BoundingSphereComponent bsc = new BoundingSphereComponent(requestedBoundingCircle());
		PositionComponent pcEnemy = new PositionComponent(initialPosition.x + pcTrigger.x,
				initialPosition.y + pcTrigger.y);
		PainPointsComponent ppc = new PainPointsComponent(requestedMaxPainPoints());
		StatusValuesComponent svc = new StatusValuesComponent(requestedBulletAttack(), requestedBulletResistance(),
				requestedScoreOnKill());
		DamageQueueComponent dqc = new DamageQueueComponent();
		CameraFocusComponent cfc = new CameraFocusComponent();
		DirectionComponent dc = new DirectionComponent(initDir);
		InactiveComponent iac = new InactiveComponent();
		InvisibleComponent ivc = new InvisibleComponent();
		TemporalComponent tpc = new TemporalComponent();
		ComponentQueueComponent cqc = new ComponentQueueComponent();
		EnemyIconComponent eic = new EnemyIconComponent(requestedIconMain().asSprite(), requestedIconSub().asSprite());
		VoiceComponent vc = createVoiceComponent();
		ZOrder2Component zoc = new ZOrder2Component();
		GetHitComponent ghc = new GetHitComponent(this);
		DeathComponent dtc = new DeathComponent(this);

		// Initially, the bounding box is the area the player needs to
		// touch to spawn the enemy. When the enemy spawns, the bounding box
		// must be set to the actual bounding box of the enemy.
		// Bounding box needs to be relative to the enemy's origin.
		bbcTrigger.minX -= pcTrigger.x;
		bbcTrigger.minY -= pcTrigger.y;
		bbcTrigger.maxX -= pcTrigger.x;
		bbcTrigger.maxY -= pcTrigger.y;

		// Setup components to be changed once the enemy spawns.
		cqc.add.add(pcEnemy);
		cqc.add.add(bbcEnemyRender);
		cqc.add.add(bbcEnemyCollision);
		cqc.add.add(bsc);
		cqc.add.add(ghc);
		cqc.add.add(dqc);
		cqc.add.add(bdc);
		cqc.remove.add(BoundingBoxCollisionComponent.class);
		cqc.remove.add(InactiveComponent.class);
		cqc.remove.add(InvisibleComponent.class);
		cqc.remove.add(tc.getClass());
		cqc.remove.add(PositionComponent.class);

		// Add components to entity.
		entity.add(dtc);
		entity.add(ttgc);
		entity.add(acc);
		entity.add(tpc);
		entity.add(cfc);
		entity.add(dc);
		entity.add(iac);
		entity.add(ivc);
		entity.add(bbcTrigger);
		entity.add(pcTrigger);
		entity.add(ppc);
		entity.add(eic);
		entity.add(cqc);
		entity.add(svc);
		entity.add(vc);
		entity.add(zoc);

		if (tc != null)
			entity.add(tc);
	}

	private VoiceComponent createVoiceComponent() {
		VoiceComponent vc = new VoiceComponent(new VoicePlayer());
		vc.onSpawn = requestedVoiceOnSpawn();
		vc.onLightDamage = requestedVoiceOnLightDamage();
		vc.onHeavyDamage = requestedVoiceOnHeavyDamage();
		vc.onBattleModeStart = requestedVoiceOnBattleModeStart();
		vc.onDeath = requestedVoiceOnDeath();
		return vc;
	}

	protected abstract ESound requestedVoiceOnSpawn();

	protected abstract ESound requestedVoiceOnLightDamage();

	protected abstract ESound requestedVoiceOnHeavyDamage();

	protected abstract ESound requestedVoiceOnBattleModeStart();

	protected abstract ESound requestedVoiceOnDeath();

	/** Called when player makes contact with enemy. */
	@Override
	public void callbackTrigger(Entity e, ETrigger t) {
		sinSpawn(e);
		spawned(e, t);
	}

	/** Called when player makes contact with enemy. */
	@Override
	public void callbackTouched(Entity me, Entity you) {
		sinSpawn(me);
		spawned(me, ETrigger.TOUCH);
	}

	/**
	 * Called when enemy gets hit by a player's bullet.
	 * 
	 * @param enemy
	 *            The enemy that got hit.
	 * @param bullet
	 *            The bullet that hit the enemy.
	 */
	@Override
	public void hitByBullet(Entity enemy, Entity bullet) {

	}

	/**
	 * Removes all active bullets of the given enemy.
	 * 
	 * @param enemy
	 *            Enemy whose bullets are to be removed.
	 * @param convertToScore
	 *            Whether the bullets should be converted to homing score
	 *            bullets.
	 */
	private void releaseBullets(Entity enemy, boolean convertScore) {
		AnyChildComponent acc = Mapper.anyChildComponent.get(enemy);
		if (acc != null && acc.childComponent != null) {
			PositionComponent pc = Mapper.positionComponent.get(playerHitCircleEntity);
			IGrantStrategy gs = new SpeedIncreaseGrantStrategy(3.0f, 400.0f);
			// Iterate over all bullets before this bullet.
			for (SiblingComponent sibling = acc.childComponent.prevSiblingComponent; sibling != null; sibling = sibling.prevSiblingComponent) {
				if (convertScore)
					convertBulletToScoreBullet(sibling.me, pc, gs);
				gameEntityEngine.removeEntity(sibling.me);
			}
			// Iterate over this bullet and the following bullets.
			for (SiblingComponent sibling = acc.childComponent; sibling != null; sibling = sibling.nextSiblingComponent) {
				if (convertScore)
					convertBulletToScoreBullet(sibling.me, pc, gs);
				gameEntityEngine.removeEntity(sibling.me);
			}
		}
		if (acc != null)
			acc.childComponent = null;
	}

	private void convertBulletToScoreBullet(Entity bullet, PositionComponent playerEntityPositionComponent,
			IGrantStrategy gs) {
		final PositionComponent pc = Mapper.positionComponent.get(bullet);
		final BulletStatusComponent bsc = Mapper.bulletStatusComponent.get(bullet);
		if (pc == null || bsc == null)
			return;
		homingTrajectory.position(pc.x, pc.y);
		homingTrajectory.lifeTime = 10.0f;
		homingTrajectory.target(playerEntityPositionComponent);
		homingTrajectory.grant(gs);

		final BulletShapeMaker bulletShape = scoreBulletShapes[MathUtils.random(3)];
		BulletMaker.makeAsScoreBullet(bulletShape, homingTrajectory, bsc.score);
	}

	/**
	 * Callback for spawning the enemy.
	 */
	private void sinSpawn(Entity enemy) {
		ComponentUtils.applyComponentQueue(enemy);
		// Let enemy say something when he spawns.
		VoiceComponent vc = Mapper.voiceComponent.get(enemy);
		if (vc != null && vc.voicePlayer != null)
			vc.voicePlayer.play(vc.onSpawn);
		// Let enemy appear in a flash.
		MakerUtils.addParticleEffectGame(requestedParticleEffectOnSpawn(), Mapper.positionComponent.get(enemy));
		SoundPlayer.getInstance().play(requestedSoundOnSpawn());
	}

	/** Gets called when the enemy has been incapacitated. */
	@Override
	public void kill(Entity enemy) {
		// Animate death and play explosion sound.
		MakerUtils.addParticleEffectGame(requestedParticleEffectOnDeath(), Mapper.positionComponent.get(enemy));
		SoundPlayer.getInstance().play(requestedSoundOnDeath());

		// Release active bullets.
		releaseBullets(enemy, true);

		// Score for defeating the enemy.
		StatusValuesComponent svc = Mapper.statusValuesComponent.get(enemy);
		if (svc != null)
			gameScore.increaseBy(MathUtils.random(svc.scoreOnKill.min, svc.scoreOnKill.max));
		gameEntityEngine.removeEntity(enemy);

		// Increase count.
		++enemyKillCount;
	}

	/**
	 * Called when we leave battle mode.
	 * 
	 * @param won
	 *            True if battle mode ended as a result of the player defeating
	 *            an enemy, or true if it was a result of the player fleeing
	 *            from the enemy.
	 */
	public static void exitBattleMode(boolean won) {
		LOG.debug("exiting battle mode");

		battleModeActive = false;
		MusicPlayer.getInstance().loadNext(level.getBgm());
		MusicPlayer.getInstance().setCrossFade(true);
		MusicPlayer.getInstance().transition(4.0f);
		playerHitCircleEntity.add(gameEntityEngine.createComponent(InvisibleComponent.class));

		// Animate fading of battle stigma.
		ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(playerBattleStigmaEntity);
		if (ssc != null)
			ssc.setup(0.0f, 0.0f);
		MakerUtils.addTimedRunnable(3.0f, onBattleStigmaVanished);

		// Animate fading of target cross.
		ssc = Mapper.shouldScaleComponent.get(enemyTargetCrossEntity);
		if (ssc != null)
			ssc.setup(0.0f, 0.0f);
		MakerUtils.addTimedRunnable(3.0f, onTargetCrossVanished);

		// Switch player to normal mode animation.
		ComponentUtils.switchAnimationList(playerEntity, player.getAnimationList(), ESpriteDirectionStrategy.ZENITH);

		// Add particle effect to player for exiting battle mode.
		MakerUtils.addParticleEffectGame(player.getBattleModeEnterParticleEffect(),
				Mapper.positionComponent.get(playerEntity));

		// Play player win phrase and enemy explosion.
		VoiceComponent vc = Mapper.voiceComponent.get(playerHitCircleEntity);
		if (won && vc != null) {
			vc.voicePlayer.play(vc.onBattleModeExit);
		} else
			vc.voicePlayer.play(vc.onBattleModeFlee);

		// Add slow-motion effect and battle fanfare.
		if (won && level.getSoundOnBattleWin() != null) {
			// Battle win fanfare.
			SoundPlayer.getInstance().play(level.getSoundOnBattleWin());
			// Slow-motion effect.
			SystemUtils.disableAction();
			game.setGlobalTimeScale(0.12f);
			MakerUtils.addTimedRunnable(level.getSoundOnBattleWin().getDurationInMilliseconds() * 0.12f * 0.001f,
				new ITimedCallback() {
					@Override
					public void run(Entity entity, Object data) {
						SystemUtils.enableAction();
						game.setGlobalTimeScale(1.0f);
					}
				}
			);
		}

		// Remove target info from status screen.
		statusScreen.untargetEnemy();
	}

	/** Called when we enter battle mode. */
	public static void enterBattleMode(Entity enemy) {
		LOG.debug("entering battle mode");

		battleModeActive = true;

		// Play battle music.
		MusicPlayer.getInstance().loadNext(level.getBattleBgm());
		MusicPlayer.getInstance().setCrossFade(true);
		MusicPlayer.getInstance().transition(2.0f);

		cameraTrackingComponent.playerPoint = enemy;
		SystemUtils.disableAction();
		ComponentUtils.setBulletAction(false);
		playerEntity.add(gameEntityEngine.createComponent(InactiveComponent.class));

		// Switch player to battle mode animation.
		ComponentUtils.switchAnimationList(playerEntity, player.getBattleAnimationList(),
				ESpriteDirectionStrategy.STATIC);

		// Scroll camera back to the player after enemy preview finishes.
		MakerUtils.addTimedRunnable(3.0f, onEnemyPreviewFinish);

		// Show battle slogan.
		game.pushLayer(new BattleModeActivateLayer(Interpolation.elastic, 3.0f));

		// Show player's hit circle.
		playerHitCircleEntity.remove(InvisibleComponent.class);

		// Show battle stigma.
		playerBattleStigmaEntity.remove(InactiveComponent.class);
		playerBattleStigmaEntity.remove(InvisibleComponent.class);

		// Show target cross.
		enemyTargetCrossEntity.remove(InactiveComponent.class);
		enemyTargetCrossEntity.remove(InvisibleComponent.class);
		PositionComponent pc = Mapper.positionComponent.get(enemyTargetCrossEntity);
		if (pc != null)
			pc.setup(enemy);

		// Add particle effect to player for entering battle mode.
		MakerUtils.addParticleEffectGame(player.getBattleModeEnterParticleEffect(),
				Mapper.positionComponent.get(playerEntity));

		// Animate battle stigma.
		ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(playerBattleStigmaEntity);
		ScaleComponent sc = Mapper.scaleComponent.get(playerBattleStigmaEntity);
		if (ssc != null)
			ssc.setup(1.0f, 1.0f);
		if (sc != null)
			sc.setup(0.0f, 0.0f);

		// Play sound effects and voices.
		SoundPlayer.getInstance().play(ESound.BATTLE_STIGMA_APPEAR);
		// Voice player
		VoiceComponent vc = Mapper.voiceComponent.get(playerHitCircleEntity);
		if (vc != null && vc.voicePlayer != null)
			vc.voicePlayer.play(vc.onBattleModeStart);
		// Voice enemy.
		vc = Mapper.voiceComponent.get(enemy);
		if (vc != null && vc.voicePlayer != null)
			vc.voicePlayer.play(vc.onBattleModeStart);
	}

	/**
	 * Called when the target changed and needs to be updated.
	 * 
	 * @param enemy
	 *            Enemy to target.
	 * @param lazySwitch
	 *            If true, checks whether the enemy is targetted currently and
	 *            does not switch if it is.
	 */
	public static void targetSwitched(Entity enemy, boolean lazySwitch) {
		// Check if target did actually change.
		StickyComponent sec = Mapper.stickyComponent.get(enemyTargetCrossEntity);
		if (lazySwitch && sec.stickToPositionComponent == Mapper.positionComponent.get(enemy))
			return;

		LOG.debug("switch target to " + enemy);

		// Apply changes to status screen.
		statusScreen.targetEnemy(enemy);

		// Scale target cross to enemy's size.
		ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(enemyTargetCrossEntity);
		ScaleComponent sc = Mapper.scaleComponent.get(enemyTargetCrossEntity);
		BoundingBoxCollisionComponent bbcc = Mapper.boundingBoxCollisionComponent.get(enemy);
		BoundingBoxRenderComponent bbrc = Mapper.boundingBoxRenderComponent.get(enemyTargetCrossEntity);

		// Start out with large target cross.
		if (sc != null)
			sc.setup(99.0f, 99.0f);

		// And let it get smaller.
		if (ssc != null && bbrc != null && bbcc != null)
			ssc.setup(2.0f * Math.max(bbcc.maxX - bbcc.minX, bbcc.maxY - bbcc.minY)
					/ Math.min(bbrc.maxX - bbrc.minX, bbrc.maxY - bbrc.minY));

		// Make target cross stick to enemy
		if (sec != null)
			sec.setup(enemy, (bbcc.minX + bbcc.maxX) * 0.5f, (bbcc.minY + bbcc.maxY) * 0.5f, true, true);
	}

	/** Called on battle mode exit, after the battle stigma has gone. */
	private final static ITimedCallback onBattleStigmaVanished = new ITimedCallback() {
		@Override
		public void run(Entity entity, Object data) {
			// Check player did not enter battle mode again.
			if (!battleModeActive) {
				playerBattleStigmaEntity.add(gameEntityEngine.createComponent(InvisibleComponent.class));
				playerBattleStigmaEntity.add(gameEntityEngine.createComponent(InactiveComponent.class));
			}
		}
	};

	/** Called on battle mode exit, after the target cross has gone. */
	private final static ITimedCallback onTargetCrossVanished = new ITimedCallback() {
		@Override
		public void run(Entity entity, Object data) {
			// Check player did not enter battle mode again.
			if (!battleModeActive) {
				enemyTargetCrossEntity.add(gameEntityEngine.createComponent(InvisibleComponent.class));
				enemyTargetCrossEntity.add(gameEntityEngine.createComponent(InactiveComponent.class));
			}
		}
	};

	private final static ITimedCallback onEnemyPreviewFinish = new ITimedCallback() {
		@Override
		public void run(Entity entity, Object data) {
			cameraTrackingComponent.playerPoint = playerEntity;
			MakerUtils.addTimedRunnable(0.3f, new ITimedCallback() {
				@Override
				public void run(Entity entity, Object data) {
					SystemUtils.enableAction();
					ComponentUtils.setBulletAction(true);
					playerEntity.remove(InactiveComponent.class);
				}
			});
		}
	};

	/**
	 * May be overridden for other effects.
	 * 
	 * @return Particle effect played when enemy spawns.
	 */
	protected EParticleEffect requestedParticleEffectOnSpawn() {
		return EParticleEffect.ENEMY_APPEAR_FLASH;
	}

	/**
	 * May be overridden for other effects.
	 * 
	 * @return Particle effect played when enemy dies.
	 */
	protected EParticleEffect requestedParticleEffectOnDeath() {
		return EParticleEffect.ENEMY_DIE_SPLASH;
	}

	/**
	 * May be overridden for other effects.
	 * 
	 * @return Explosion sound played when
	 *         {@link #requestedParticleEffectOnDeath()} plays..
	 */
	protected ESound requestedSoundOnSpawn() {
		return ESound.ENEMY_SPAWN_FLASH;
	}

	/**
	 * May be overridden for other effects.
	 * 
	 * @return Explosion sound played when
	 *         {@link #requestedParticleEffectOnDeath()} plays..
	 */
	protected ESound requestedSoundOnDeath() {
		return ESound.ENEMY_DIE_EXPLOSION;
	}

	// =====================
	// Abstract methods
	// =====================
	protected abstract ETexture requestedIconMain();

	protected abstract ETexture requestedIconSub();

	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		IResource<?, ?>[] myRes = new IResource<?, ?>[] { EParticleEffect.ENEMY_APPEAR_FLASH.getTextureAtlas() };
		IResource<? extends Enum<?>, ?>[] yourRes = requestedAdditionalResources();
		return ArrayUtils.addAll(myRes, yourRes);
	};

	protected abstract IResource<? extends Enum<?>, ?>[] requestedAdditionalResources();

	/**
	 * @return The bounding box used for deciding whether the entity is
	 *         on-screen and needs to be rendered.
	 */
	protected abstract Rectangle requestedBoundingBoxRender();

	/**
	 * @return The bounding box used for deciding whether this entity collides
	 *         with another entity.
	 */
	protected abstract Rectangle requestedBoundingBoxCollision();

	protected abstract Circle requestedBoundingCircle();

	/** @return Enemy maximum pain points (pp). */
	protected abstract long requestedMaxPainPoints();

	/**
	 * @return The enemy's bullet attack power. Damage is multiplied by this
	 *         factor.
	 */
	protected abstract float requestedBulletAttack();

	/**
	 * @return The enemy's bullet resistance. Damage is divided by this factor.
	 */
	protected abstract float requestedBulletResistance();

	/** @return The score that will be added when the player kill this enemy. */
	protected abstract InclusiveRange<Long> requestedScoreOnKill();

	/** Called when the enemy spawns. */
	protected abstract void spawned(Entity e, ETrigger t);

	/**
	 * May be overridden for custom values.
	 * 
	 * @param The
	 *            value for the battle in distance as specified on the map, or
	 *            null if not specified.
	 * @return When the player is closer this enemy than the distance, this
	 *         enemy will start fighting.
	 */
	protected float requestedBattleInDistance(Float valueFromMap) {
		return valueFromMap == null ? 35.0f : valueFromMap;
	}

	/**
	 * May be overridden for custom values.
	 * 
	 * @param The
	 *            value for the battle out distance as specified on the map, or
	 *            null if not specified.
	 * @return When the player is further away from this enemy, this enemy will
	 *         stop fighting.
	 */
	protected float requestedBattleOutDistance(Float valueFromMap) {
		return valueFromMap == null ? 45.0f : valueFromMap;
	};
}
