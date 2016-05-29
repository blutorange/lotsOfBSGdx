package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.game;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.level;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.GlobalBag.playerEntity;
import static de.homelab.madgaksha.GlobalBag.playerHitCircleEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.ColorComponent;
import de.homelab.madgaksha.entityengine.component.ColorFlashEffectComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.DeathComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.GetHitComponent;
import de.homelab.madgaksha.entityengine.component.HoverEffectComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InputDesktopComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.LeanEffectComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectComponent;
import de.homelab.madgaksha.entityengine.component.ParticleEffectScreenComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
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
import de.homelab.madgaksha.entityengine.component.StickyComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxMapComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder0Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder2Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder4Component;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.grantstrategy.ExponentialGrantStrategy;
import de.homelab.madgaksha.grantstrategy.ImmediateGrantStrategy;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcepool.ResourcePool;
import de.homelab.madgaksha.shader.FragmentShaderGrayscaleDarkened;

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
	private PlayerMaker () {
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
		StickyComponent sec = new StickyComponent(playerE, bbm.x+0.5f*bbm.width, bbm.y+0.5f*bbm.height, true, true);
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
	
	public Entity makePlayerHitCircle(final Entity playerE, final APlayer playerA) {
		Entity hitCircle = new Entity();
		Rectangle bbc = playerA.getBoundingBoxCollision();
		
		InvisibleComponent ivc = new InvisibleComponent();
		PositionComponent pc = new PositionComponent();
		ShouldPositionComponent spc = new ShouldPositionComponent(new ImmediateGrantStrategy(), true);
		SpriteComponent sc = new SpriteComponent(playerA.getHitCircleTexture());
		StickyComponent sec = new StickyComponent(playerE, bbc.x+0.5f*bbc.width, bbc.y+0.5f*bbc.height);
		TemporalComponent tc = new TemporalComponent();
		ZOrder4Component zoc = new ZOrder4Component();

		if (pc == null || sc == null || sec == null || ivc == null) return null;
		
		hitCircle.add(pc);
		hitCircle.add(sc);
		hitCircle.add(spc);
		hitCircle.add(sec);
		hitCircle.add(ivc);
		hitCircle.add(tc);
		hitCircle.add(zoc);
		
		return hitCircle; 
	}
	
	public Entity makePlayer(final APlayer player) {
		final Entity e = new Entity();
	
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
		TimeScaleComponent tsc = new TimeScaleComponent();

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
		
		DeathComponent dtc = new DeathComponent(this);
		GetHitComponent ghc = new GetHitComponent(this);
		
		ZOrder2Component zoc = new ZOrder2Component();
		
		VoiceComponent vcc = new VoiceComponent();
		vcc.onBattleModeStart = player.getVoiceOnBattleStart();
		vcc.onLightDamage = player.getVoiceOnLightDamage();
		vcc.onHeavyDamage = player.getVoiceOnHeavyDamage();
		vcc.onDeath = player.getVoiceOnDeath();
		vcc.voicePlayer = new VoicePlayer();
		
		e.add(dtc);
		e.add(ghc);
		e.add(vcc);
		e.add(lec);
		e.add(hec);
		e.add(src);
		e.add(tsc);
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
		e.add(zoc);
		if (kc != null) e.add(kc);
		if (shc != null) e.add(shc);
		
		return e;
	}
	
	public void kill(Entity e) {
		// Do not allow input anymore.
		e.remove(InputDesktopComponent.class);
		
		e.remove(HoverEffectComponent.class);
		e.remove(ShadowComponent.class);
		e.remove(SpriteForDirectionComponent.class);
		e.remove(SpriteAnimationComponent.class);
		
		// Animate battle stigma -> fade away.
		final StickyComponent scStigma = Mapper.stickyComponent.get(playerBattleStigmaEntity);
		if (scStigma != null) {
			scStigma.setup(e);
		}

		final ShouldScaleComponent ssc = Mapper.shouldScaleComponent.get(playerBattleStigmaEntity);
		if (ssc != null) {
			ssc.scaleX = ssc.scaleY = 0.0f;
			ssc.grantStrategy = new ExponentialGrantStrategy(0.1f, 1.0f);
		}
		
		final ColorComponent cc = Mapper.colorComponent.get(playerBattleStigmaEntity);
		if (cc != null) cc.setup(player.getBattleStigmaColorWhenHit());

		playerBattleStigmaEntity.remove(ColorFlashEffectComponent.class);
		
		gameEntityEngine.removeEntity(playerHitCircleEntity);
		
		// Show dead player animation.
		final SpriteComponent sc = Mapper.spriteComponent.get(e);	
		if (sc != null) sc.setup(player.getDeathSprite());
		
		// Stop player movement.
		final VelocityComponent vc = Mapper.velocityComponent.get(e);
		vc.setup(0.0f, 0.0f, 0.0f);
		e.add(gameEntityEngine.createComponent(InactiveComponent.class));
		
		// Show death animation.
		final Entity deathEffect = gameEntityEngine.createEntity();

		final PositionComponent pc = gameEntityEngine.createComponent(PositionComponent.class);
		final ParticleEffectComponent pec = gameEntityEngine.createComponent(ParticleEffectScreenComponent.class);
		final StickyComponent sec = gameEntityEngine.createComponent(StickyComponent.class);
		final ShouldPositionComponent spc = gameEntityEngine.createComponent(ShouldPositionComponent.class);
		final TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);
		
		pec.particleEffect = ResourcePool.obtainParticleEffect(player.getParticleEffectOnDeath());
		sec.setup(e);
		spc.grantStrategy = new ImmediateGrantStrategy();
		
		deathEffect.add(pc);
		deathEffect.add(pec);
		deathEffect.add(sec);
		deathEffect.add(spc);
		deathEffect.add(tc);		

		
		// Player death sound effect.
		SoundPlayer.getInstance().play(ESound.PLAYER_EXPLODE_ON_DEATH);
		gameEntityEngine.addEntity(deathEffect);
		
		// Game over music.
		MusicPlayer.getInstance().loadNext(level.getGameOverBgm());
		MusicPlayer.getInstance().setCrossFade(true);
		MusicPlayer.getInstance().transition(1.0f);
		
		// Show everything in greyscale.
		game.setFragmentShaderBatchGame(new FragmentShaderGrayscaleDarkened(0.0f, 0.8f, 5.0f));
		
		game.pause(false);
		
	}
	
	public void hitByBullet(Entity me, Entity you) {
		if (Mapper.colorFlashEffectComponent.get(playerBattleStigmaEntity) == null) {
			final ColorFlashEffectComponent cfec = gameEntityEngine.createComponent(ColorFlashEffectComponent.class);
			if (Mapper.inactiveComponent.get(me) == null) { 
				cfec.setup(Color.WHITE, player.getBattleStigmaColorWhenHit(), Interpolation.circle, 0.1f, 6);
				playerBattleStigmaEntity.add(cfec);
				final VoiceComponent vc = Mapper.voiceComponent.get(playerEntity);
				if (vc != null) SoundPlayer.getInstance().play(ESound.BATTLE_STIGMA_ABSORB,10.0f);
			}
		}
	}

}
