package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.battleModeActive;
import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.enemyTargetCrossEntity;
import static de.homelab.madgaksha.GlobalBag.game;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.GlobalBag.playerEntity;
import static de.homelab.madgaksha.GlobalBag.playerHitCircleEntity;
import static de.homelab.madgaksha.GlobalBag.statusScreen;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
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
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.EnemyIconComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.entityengine.component.StickyComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder2Component;
import de.homelab.madgaksha.entityengine.entitysystem.AiSystem;
import de.homelab.madgaksha.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.layer.BattleModeActivateLayer;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcepool.EParticleEffect;
import de.homelab.madgaksha.resourcepool.ResourcePool;
import de.homelab.madgaksha.util.GeoUtil;

public abstract class EnemyMaker extends EntityMaker implements IBehaving, ITrigger, IReceive {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EnemyMaker.class);
	
	protected EnemyMaker() {
		super();
	}
	
	/**
	 * Adds the appropriate components to an entity to be used as an enemy.
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
	public void setup(Entity entity, Shape2D shape, ETrigger trigger, Vector2 initialPosition, Float initDir) {
		super.setup(entity);
		
		// Create components to be added.
		AnyChildComponent acc = new AnyChildComponent();
		Component tc = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);
		PositionComponent pcTrigger = MakerUtils.makePositionAtCenter(shape);
		ABoundingBoxComponent bbcEnemyRender = new BoundingBoxRenderComponent(requestedBoundingBoxRender());
		ABoundingBoxComponent bbcEnemyCollision = new BoundingBoxCollisionComponent(requestedBoundingBoxCollision());
		ABoundingBoxComponent bbcTrigger = new BoundingBoxCollisionComponent(GeoUtil.getBoundingBox(shape));
		BoundingSphereComponent bsc = new BoundingSphereComponent(requestedBoundingCircle());
		PositionComponent pcEnemy = new PositionComponent(initialPosition.x + pcTrigger.x,
				initialPosition.y + pcTrigger.y);
		PainPointsComponent ppc = new PainPointsComponent(requestedMaxPainPoints());
		StatusValuesComponent svc = new StatusValuesComponent(requestedBulletAttack(), requestedBulletResistance());
		DamageQueueComponent dqc = new DamageQueueComponent();
		CameraFocusComponent cfc = new CameraFocusComponent();
		DirectionComponent dc = new DirectionComponent(initDir);
		InactiveComponent iac = new InactiveComponent();
		InvisibleComponent ivc = new InvisibleComponent();		
		TemporalComponent tpc = new TemporalComponent();
		ComponentQueueComponent cqc = new ComponentQueueComponent();
		EnemyIconComponent eic = new EnemyIconComponent(requestedIconMain().asSprite(), requestedIconSub().asSprite());
		VoiceComponent vc = createVoiceComponent(); 
		ZOrder2Component zoc = gameEntityEngine.createComponent(ZOrder2Component.class);
		
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
		cqc.remove.add(BoundingBoxCollisionComponent.class);
		cqc.remove.add(InactiveComponent.class);
		cqc.remove.add(InvisibleComponent.class);
		cqc.remove.add(tc.getClass());
		cqc.remove.add(PositionComponent.class);
		
		// Add components to entity.
		entity.add(acc);
		entity.add(tpc);
		entity.add(cfc);
		entity.add(dc);
		entity.add(iac);
		entity.add(ivc);
		entity.add(bbcTrigger);
		entity.add(pcTrigger);
		entity.add(ppc);
		entity.add(dqc);
		entity.add(eic);
		entity.add(cqc);
		entity.add(svc);
		entity.add(vc);
		entity.add(zoc);
				
		if (tc != null)	entity.add(tc);
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

	@Override
	public void callbackTrigger(Entity e, ETrigger t) {
		sinSpawn(e);
		spawned(e, t);
	}

	@Override
	public void callbackTouched(Entity me, Entity you) {
		sinSpawn(me);
		spawned(me, ETrigger.TOUCH);
	}

	/**
	 * Callback for spawning the enemy.
	 */
	private void sinSpawn(Entity e) {
		ComponentUtils.applyComponentQueue(e);
		VoiceComponent vc = Mapper.voiceComponent.get(e);
		if (vc != null && vc.voicePlayer != null) vc.voicePlayer.play(vc.onSpawn);
	}
	
	/** Called when we leave battle mode. */
	public static void exitBattleMode() {
		battleModeActive = false;
		MusicPlayer.getInstance().loadNext(level.getBgm());
		MusicPlayer.getInstance().setCrossFade(true);
		MusicPlayer.getInstance().transition(2.0f);
		playerHitCircleEntity.add(gameEntityEngine.createComponent(InvisibleComponent.class));
		
		// Hide player battle stigma.
		playerBattleStigmaEntity.add(gameEntityEngine.createComponent(InvisibleComponent.class));
		playerBattleStigmaEntity.add(gameEntityEngine.createComponent(InactiveComponent.class));
		
		// Hide target cross.
		enemyTargetCrossEntity.add(gameEntityEngine.createComponent(InvisibleComponent.class));
		enemyTargetCrossEntity.add(gameEntityEngine.createComponent(InactiveComponent.class));
	}
	
	/** Called when we enter battle mode. */
	public static void enterBattleMode(Entity enemy) {
		battleModeActive = true;
		MusicPlayer.getInstance().loadNext(level.getBattleBgm());
		MusicPlayer.getInstance().setCrossFade(true);
		MusicPlayer.getInstance().transition(2.0f);
		
		cameraTrackingComponent.playerPoint = enemy;
		gameEntityEngine.getSystem(AiSystem.class).setProcessing(false);
		playerEntity.add(gameEntityEngine.createComponent(InactiveComponent.class));
		MakerUtils.addTimedRunnable(3.0f,new ITimedCallback() {
			@Override
			public void run(Entity entity, Object data) {
				cameraTrackingComponent.playerPoint = playerEntity;
				MakerUtils.addTimedRunnable(0.3f, new ITimedCallback() {					
					@Override
					public void run(Entity entity, Object data) {
						gameEntityEngine.getSystem(AiSystem.class).setProcessing(true);
						playerEntity.remove(InactiveComponent.class);						
					}
				});
			}
		});
		
		game.pushLayer(new BattleModeActivateLayer(Interpolation.elastic,3.0f));
		
		// Show player's hit circle.
		playerHitCircleEntity.remove(InvisibleComponent.class);
		
		// Show battle stigma.
		playerBattleStigmaEntity.remove(InactiveComponent.class);
		playerBattleStigmaEntity.remove(InvisibleComponent.class);
		
		// Show target cross.
		enemyTargetCrossEntity.remove(InactiveComponent.class);
		enemyTargetCrossEntity.remove(InvisibleComponent.class);
		PositionComponent pc = Mapper.positionComponent.get(enemyTargetCrossEntity);
		if (pc != null) pc.setup(enemy);
		
		// Add nice particle effect to battle stigma.
		ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectScreenComponent.class);
		pec.particleEffect = ResourcePool.obtainParticleEffect(EParticleEffect.PLAYER_BATTLE_MODE_ENTER_BURST);//player.getBattleStigmaStartupParticleEffect();
		playerBattleStigmaEntity.add(pec);
		
		// Animate battle stigma.
		ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(playerBattleStigmaEntity);
		ScaleComponent sc = Mapper.scaleComponent.get(playerBattleStigmaEntity);
		if (ssc != null) ssc.setup(1.0f,1.0f);
		if (sc != null) sc.setup(0.0f, 0.0f);	
		
		// Play sound effects and voices.
		SoundPlayer.getInstance().play(ESound.BATTLE_STIGMA_APPEAR);
		// Voice player
		VoiceComponent vc= Mapper.voiceComponent.get(playerEntity);
		if (vc != null && vc.voicePlayer != null) vc.voicePlayer.play(vc.onBattleModeStart);
		// Voice enemy.
		vc = Mapper.voiceComponent.get(enemy);
		if (vc != null && vc.voicePlayer != null) vc.voicePlayer.play(vc.onBattleModeStart);
	}

	/** Called when the target changed and needs to be updated. */
	public static void targetSwitched(Entity enemy) {
		statusScreen.targetEnemy(enemy);
		// Make target cross stick to enemy.
		StickyComponent sec = Mapper.stickyComponent.get(enemyTargetCrossEntity);
		// Scale target cross to enemy's size.
		ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(enemyTargetCrossEntity);
		ScaleComponent sc = Mapper.scaleComponent.get(enemyTargetCrossEntity);
		BoundingBoxCollisionComponent bbcc = Mapper.boundingBoxCollisionComponent.get(enemy);
		BoundingBoxRenderComponent bbrc = Mapper.boundingBoxRenderComponent.get(enemyTargetCrossEntity);
		if (sc != null) sc.setup(99.0f, 99.0f);
		if (ssc != null && bbrc != null && bbcc != null)
			ssc.setup(2.0f * Math.max(bbcc.maxX - bbcc.minX, bbcc.maxY - bbcc.minY)
					/ Math.min(bbrc.maxX - bbrc.minX, bbrc.maxY - bbrc.minY));
		if (sec != null)
			sec.setup(enemy, (bbcc.minX + bbcc.maxX) * 0.5f, (bbcc.minY + bbcc.maxY) * 0.5f, true, true);

	}
	
	// =====================
	//   Abstract methods
	// =====================
	protected abstract ETexture requestedIconMain();
	protected abstract ETexture requestedIconSub();

	protected abstract IResource<? extends Enum<?>, ?>[] requestedResources();

	/** @return The bounding box used for deciding whether the entity is on-screen and needs to be rendered. */
	protected abstract Rectangle requestedBoundingBoxRender();
	/** @return The bounding box used for deciding whether this entity collides with another entity. */
	protected abstract Rectangle requestedBoundingBoxCollision();

	protected abstract Circle requestedBoundingCircle();

	/** @return Enemy maximum pain points (pp). */
	protected abstract long requestedMaxPainPoints();

	/** @return The enemy's bullet atack power. Damage is multiplied by this factor. */
	protected abstract float requestedBulletAttack();
	/** @return The enemy's bullet resistance. Damage is divided by this factor. */
	protected abstract float requestedBulletResistance();
	
	/** Called when the enemy spawns. */
	protected abstract void spawned(Entity e, ETrigger t);
	
	@Override
	public abstract void behave(Entity enemy);
}
