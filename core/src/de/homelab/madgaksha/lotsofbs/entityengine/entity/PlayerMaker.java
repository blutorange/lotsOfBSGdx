package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.battleModeActive;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.game;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.level;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.audiosystem.MusicPlayer;
import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.audiosystem.VoicePlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationForDirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnyChildComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.BehaviourComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ColorComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ColorFlashEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DeathComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.GetHitComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InputDesktopComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldRotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShouldScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.StickyComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent.AnimationMode;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxMapComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup01Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder0Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder2Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder4Component;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.lotsofbs.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.lotsofbs.grantstrategy.ImmediateGrantStrategy;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.APlayer;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.shader.FragmentShaderGrayscale;

public final class PlayerMaker implements IHittable, IMortal {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PlayerMaker.class);

	// Singleton
	private static class SingletonHolder {
		private static final PlayerMaker INSTANCE = new PlayerMaker();
	}

	public static PlayerMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private PlayerMaker() {
		super();
	}

	public Entity makePlayerBattleStigma(Entity playerE, APlayer playerA) {
		Entity battleStigma = new Entity();
		Rectangle bbm = playerA.getBoundingBoxMap();

		AngularVelocityComponent avc = new AngularVelocityComponent(playerA.getBattleStigmaAngularVelocity());
		ColorComponent cc = new ColorComponent();
		InactiveComponent iac = new InactiveComponent();
		InvisibleComponent ivc = new InvisibleComponent();
		PositionComponent pc = new PositionComponent(playerE);
		RotationComponent rc = new RotationComponent();
		SpriteComponent sc = new SpriteComponent(playerA.getBattleStigmaTexture());
		StickyComponent sec = new StickyComponent(playerE, bbm.x + 0.5f * bbm.width, bbm.y + 0.5f * bbm.height, true,
				false);
		ScaleComponent slc = new ScaleComponent(0.0f, 0.0f, playerA.getBattleStigmaTexture().getOriginalScale());
		ShouldPositionComponent spc = new ShouldPositionComponent(new ImmediateGrantStrategy());
		ShouldScaleComponent ssc = new ShouldScaleComponent(new ExponentialGrantStrategy(0.5f, 0.5f));
		TemporalComponent tc = new TemporalComponent();
		ZOrder0Component zoc = new ZOrder0Component();

		battleStigma.add(avc);
		battleStigma.add(cc);
		battleStigma.add(ivc);
		battleStigma.add(iac);
		battleStigma.add(pc);
		battleStigma.add(rc);
		battleStigma.add(sc);
		battleStigma.add(sec);
		battleStigma.add(slc);
		battleStigma.add(spc);
		battleStigma.add(ssc);
		battleStigma.add(tc);
		battleStigma.add(zoc);

		return battleStigma;
	}

	public Entity makePlayerHitCircle(final APlayer playerA) {
		Entity hitCircle = new Entity();

		InvisibleComponent ivc = new InvisibleComponent();
		PositionComponent pc = new PositionComponent();
		ShouldPositionComponent spc = new ShouldPositionComponent(new ImmediateGrantStrategy(), true);
		SpriteComponent sc = new SpriteComponent();
		StickyComponent sec = new StickyComponent();
		TemporalComponent tc = new TemporalComponent();
		ZOrder4Component zoc = new ZOrder4Component();

		ABoundingBoxComponent bbcc = new BoundingBoxCollisionComponent();
		ShapeComponent shc = null;
		Shape2D exactShape = player.getExactShapeCollision();
		if (exactShape != null)
			shc = new ShapeComponent();
		StatusValuesComponent svc = new StatusValuesComponent();
		PainPointsComponent ppc = new PainPointsComponent();
		DamageQueueComponent dqc = new DamageQueueComponent();
		AnyChildComponent acc = new AnyChildComponent();
		DeathComponent dtc = new DeathComponent();
		GetHitComponent ghc = new GetHitComponent();
		VoiceComponent vcc = new VoiceComponent();
		TriggerTouchComponent ttg1c = new TriggerTouchGroup01Component();
		hitCircle.add(bbcc);
		if (shc != null)
			hitCircle.add(shc);
		hitCircle.add(svc);
		hitCircle.add(ppc);
		hitCircle.add(dqc);
		hitCircle.add(acc);
		hitCircle.add(dtc);
		hitCircle.add(ghc);
		hitCircle.add(vcc);
		hitCircle.add(ttg1c);
		hitCircle.add(pc);
		hitCircle.add(sc);
		hitCircle.add(spc);
		hitCircle.add(sec);
		hitCircle.add(ivc);
		hitCircle.add(tc);
		hitCircle.add(zoc);

		return hitCircle;
	}

	public void setupPlayerHitCircle(final APlayer player) {
		Vector2 offsetFromPlayer = player.getOffsetPlayerToHitCircle();
		StickyComponent sec = Mapper.stickyComponent.get(playerHitCircleEntity);
		sec.setup(playerEntity, -offsetFromPlayer.x, -offsetFromPlayer.y);
		sec.offsetRelativeToCamera = false;
		sec.ignoreTrackOffset = true;

		SpriteComponent spc = Mapper.spriteComponent.get(playerHitCircleEntity);
		spc.setup(player.getHitCircleTexture());

		Rectangle bbc = player.getBoundingBoxCollision();
		ABoundingBoxComponent bbcc = Mapper.boundingBoxCollisionComponent.get(playerHitCircleEntity);
		bbcc.setup(bbc);

		Shape2D exactShape = player.getExactShapeCollision();
		ShapeComponent shc = Mapper.shapeComponent.get(playerHitCircleEntity);
		if (exactShape != null && shc != null) {
			shc.setup(exactShape);
		}

		PainPointsComponent ppc = Mapper.painPointsComponent.get(playerHitCircleEntity);
		ppc.setup(player.getMaxPainPoints());

		DeathComponent dtc = Mapper.deathComponent.get(playerHitCircleEntity);
		dtc.setup(this);

		GetHitComponent ghc = Mapper.getHitComponent.get(playerHitCircleEntity);
		ghc.setup(this);

		StatusValuesComponent svc = Mapper.statusValuesComponent.get(playerHitCircleEntity);
		svc.setup(player.getBulletAttack(), player.getBulletResistance());

		VoiceComponent vcc = Mapper.voiceComponent.get(playerHitCircleEntity);
		vcc.onBattleModeStart = player.getVoiceOnBattleModeStart();
		vcc.onBattleModeExit = player.getVoiceOnBattleModeEnd();
		vcc.onBattleModeFlee = player.getVoiceOnBattleModeFlee();
		vcc.onLightDamage = player.getVoiceOnLightDamage();
		vcc.onHeavyDamage = player.getVoiceOnHeavyDamage();
		vcc.onDeath = player.getVoiceOnDeath();
		vcc.onEnemyKilled = player.getVoiceOnEnemyKilled();
		vcc.voicePlayer = new VoicePlayer();
	}

	public void setupPlayer(final APlayer player) {
		switch (Gdx.app.getType()) {
		case Android:
			// TODO
			break;
		case Applet:
			// TODO
			break;
		case Desktop:
			InputDesktopComponent ic = Mapper.inputDesktopComponent.get(playerEntity);
			ic.frictionFactor = player.getMovementFrictionFactor();
			ic.accelerationFactorLow = player.getMovementAccelerationFactorLow();
			ic.accelerationFactorHigh = player.getMovementAccelerationFactorHigh();
			ic.battleSpeedLow = player.getMovementBattleSpeedLow();
			ic.battleSpeedHigh = player.getMovementBattleSpeedHigh();
			break;
		case HeadlessDesktop:
			// TODO
			break;
		case WebGL:
			// TODO
			break;
		case iOS:
			// TODO
			break;
		default:
			// TODO
			break;

		}

		AnimationModeQueueComponent smqc = Mapper.animationModeQueueComponent.get(playerEntity);
		smqc.currentMode = AnimationMode.NORMAL;

		AnimationModeListComponent sfdlc = Mapper.animationModeListComponent.get(playerEntity);
		sfdlc.setupNormal(player.getNormalAnimationList(), ESpriteDirectionStrategy.STATIC, true);
		sfdlc.setupBattle(player.getBattleAnimationList(), ESpriteDirectionStrategy.STATIC, true);
		sfdlc.setupDamage(player.getDamageAnimationList(), ESpriteDirectionStrategy.STATIC, true);
		sfdlc.setupDeath(player.getDeathAnimationList(), ESpriteDirectionStrategy.STATIC, true);

		AnimationForDirectionComponent sfdc = Mapper.animationForDirectionComponent.get(playerEntity);
		sfdc.setup(player.getNormalAnimationList(), ESpriteDirectionStrategy.STATIC);

		SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(playerEntity);
		sac.setup(sfdc);

		SpriteComponent sc = Mapper.spriteComponent.get(playerEntity);
		sc.setup(sac, player.getSpriteOrigin());

		ShadowComponent kc = Mapper.shadowComponent.get(playerEntity);
		player.setupShadow(kc);
		kc.relativeToCamera = false;

		ABoundingBoxComponent bbrc = Mapper.boundingBoxRenderComponent.get(playerEntity);
		bbrc.setup(player.getBoundingBoxRender());

		ABoundingBoxComponent bbmc = Mapper.boundingBoxMapComponent.get(playerEntity);
		bbmc.setup(player.getBoundingBoxMap());

		BoundingSphereComponent bsc = Mapper.boundingSphereComponent.get(playerEntity);
		bsc.setup(player.getBoundingCircle());

		PositionComponent pc = Mapper.positionComponent.get(playerEntity);
		pc.setup(level.getMapData().getPlayerInitialPosition(), true);

		VelocityComponent vc = Mapper.velocityComponent.get(playerEntity);
		vc.setup(0.0f, 0.0f);

		RotationComponent rc = Mapper.rotationComponent.get(playerEntity);
		rc.setup(false);

		DirectionComponent dc = Mapper.directionComponent.get(playerEntity);
		dc.setup(level.getMapData().getPlayerInitialDirection());

		ShouldRotationComponent src = Mapper.shouldRotationComponent.get(playerEntity);
		src.setup(new ExponentialGrantStrategy(0.1f));

		ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(playerEntity);
		ssc.setup(new ExponentialGrantStrategy(0.1f));

		HoverEffectComponent hec = Mapper.hoverEffectComponent.get(playerEntity);
		hec.setup(8.0f, 1.0f);

		LeanEffectComponent lec = Mapper.leanEffectComponent.get(playerEntity);
		lec.setup(30.0f, 0.10f, 0.0001f);

		BehaviourComponent bc = Mapper.behaviourComponent.get(playerEntity);
		bc.setup(onBehave);
	}

	public Entity makePlayer(final APlayer player) {
		final Entity e = new Entity();

		// Use appropriate input scheme for device.
		switch (Gdx.app.getType()) {
		case Android:
			// TODO
			break;
		case Applet:
			// TODO
			break;
		case Desktop:
			InputDesktopComponent ic = new InputDesktopComponent();
			e.add(ic);
			break;
		case HeadlessDesktop:
			// TODO
			break;
		case WebGL:
			// TODO
			break;
		case iOS:
			// TODO
			break;
		default:
			// TODO
			break;

		}

		AnimationModeQueueComponent smqc = new AnimationModeQueueComponent();
		AnimationModeListComponent sfdlc = new AnimationModeListComponent();
		AnimationForDirectionComponent sfdc = new AnimationForDirectionComponent();
		SpriteAnimationComponent sac = new SpriteAnimationComponent();
		SpriteComponent sc = new SpriteComponent();

		ShadowComponent kc = new ShadowComponent();

		ABoundingBoxComponent bbrc = new BoundingBoxRenderComponent();
		ABoundingBoxComponent bbmc = new BoundingBoxMapComponent();
		BoundingSphereComponent bsc = new BoundingSphereComponent();
		TemporalComponent tc = new TemporalComponent();
		TimeScaleComponent tsc = new TimeScaleComponent();

		PositionComponent pc = new PositionComponent();
		VelocityComponent vc = new VelocityComponent();
		RotationComponent rc = new RotationComponent();
		DirectionComponent dc = new DirectionComponent();
		ScaleComponent slc = new ScaleComponent();

		ShouldRotationComponent src = new ShouldRotationComponent();
		ShouldScaleComponent ssc = new ShouldScaleComponent();

		HoverEffectComponent hec = new HoverEffectComponent();
		LeanEffectComponent lec = new LeanEffectComponent();

		BehaviourComponent bc = new BehaviourComponent();

		VoiceComponent vcc = new VoiceComponent();

		ZOrder2Component zoc = new ZOrder2Component();

		e.add(bc).add(vcc).add(lec).add(hec).add(src).add(tsc).add(ssc).add(sc).add(sac).add(sfdc).add(bsc).add(bbmc)
				.add(bbrc).add(pc).add(vc).add(rc).add(slc).add(dc).add(tc).add(zoc).add(kc).add(sfdlc).add(smqc);

		return e;
	}

	public void kill(Entity hitCircle) {
		game.setGameLost();
		EnemyMaker.exitBattleMode(false);

		// Do not allow input anymore and disable player.
		playerEntity.remove(BehaviourComponent.class);
		playerEntity.remove(HoverEffectComponent.class);
		playerEntity.remove(InputDesktopComponent.class);
		playerEntity.remove(LeanEffectComponent.class);
		playerEntity.remove(ShadowComponent.class);
		playerEntity.remove(ShouldRotationComponent.class);
		playerEntity.remove(ShouldScaleComponent.class);
		playerEntity.remove(VelocityComponent.class);
		playerEntity.remove(VoiceComponent.class);

		// Animate battle stigma -> make red and fade away.
		final StickyComponent scStigma = Mapper.stickyComponent.get(playerBattleStigmaEntity);
		if (scStigma != null)
			scStigma.setup(playerEntity);
		final ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(playerBattleStigmaEntity);
		if (ssc != null) {
			ssc.scaleX = ssc.scaleY = 0.0f;
			ssc.grantStrategy = new ExponentialGrantStrategy(0.1f, 1.0f);
		}
		final ColorComponent cc = Mapper.colorComponent.get(playerBattleStigmaEntity);
		if (cc != null)
			cc.setup(player.getBattleStigmaColorWhenHit());
		playerBattleStigmaEntity.remove(ColorFlashEffectComponent.class);

		// Remove hit circle as there won't be any battles anymore.
		gameEntityEngine.removeEntity(playerHitCircleEntity);

		// Show death animation and and pause after a few seconds after it
		// finishes.
		final ITimedCallback onDeathEffectDone = new ITimedCallback() {
			@Override
			public void run(Entity entity, Object data) {
				ComponentUtils.transitionToSpriteMode(playerEntity, AnimationMode.DEATH);
				MakerUtils.addTimedRunnable(6.0f, new ITimedCallback() {
					@Override
					public void run(Entity entity, Object data) {
						game.pause();
					}
				});
			}
		};
		MakerUtils.addParticleEffectScreen(player.getParticleEffectOnDeath(),
				Mapper.positionComponent.get(playerEntity), onDeathEffectDone);

		// Player death sound effect.
		SoundPlayer.getInstance().play(ESound.PLAYER_EXPLODE_ON_DEATH);

		// Game over music.
		MusicPlayer.getInstance().loadNext(level.getGameOverBgm());
		MusicPlayer.getInstance().setCrossFade(true);
		MusicPlayer.getInstance().transition(1.0f);

		// Show everything in greyscale.
		game.setFragmentShaderBatchGame(
				new FragmentShaderGrayscale(0.0f, 0.9f, 1.0f, 0.2f, 9.0f, Interpolation.sineIn));
	}

	@Override
	public void hitByBullet(Entity me, Entity you) {
		if (Mapper.colorFlashEffectComponent.get(playerBattleStigmaEntity) == null) {
			final ColorFlashEffectComponent cfec = gameEntityEngine.createComponent(ColorFlashEffectComponent.class);
			if (Mapper.inactiveComponent.get(playerEntity) == null) {
				cfec.setup(Color.WHITE, player.getBattleStigmaColorWhenHit(), Interpolation.circle, 0.1f, 6);
				playerBattleStigmaEntity.add(cfec);
				final VoiceComponent vc = Mapper.voiceComponent.get(playerEntity);
				if (vc != null)
					SoundPlayer.getInstance().play(ESound.BATTLE_STIGMA_ABSORB, 10.0f);
			}
		}
	}

	/**
	 * Callback for automatic player behaviors such as firing weapons etc.
	 */
	private final static IBehaving onBehave;
	static {
		onBehave = new IBehaving() {
			@Override
			public boolean behave(Entity e) {
				if (battleModeActive) {
					TemporalComponent tc = Mapper.temporalComponent.get(e);
					player.getEquippedWeapon().fire(playerEntity, tc.deltaTime);
				}
				return false;
			}
		};
	}
}
