package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.GlobalBag.playerEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.BulletStatusComponent;
import de.homelab.madgaksha.entityengine.component.ColorFlashEffectComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.ParentComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.entityengine.entitysystem.DamageSystem;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.IResource;

public class BulletMaker extends EntityMaker implements IReceive {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BulletMaker.class);
	
	/** Frequency at which an entity hit by a bullet takes 1x damage, in Hz. */
	private final static float DAMAGE_FREQUENCY = 20.0f;
	/** Lower range of the random damage variance, in percent. */
	private final static long DAMAGE_LOWER_RANGE = 80L;
	/** Upper range of the random damage variance, in percent. */
	private final static long DAMAGE_UPPER_RANGE = 120L;
	
	// Singleton
	private static class SingletonHolder {
		private static final BulletMaker INSTANCE = new BulletMaker();
	}
	public static BulletMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}
	private BulletMaker() {
		super();
	}
	
	public static Entity makeEntity(Entity parent, BulletShapeMaker bulletShape, BulletTrajectoryMaker bulletTrajectory, long power) {
		Entity entity = gameEntityEngine.createEntity();
		getInstance().setup(entity, parent, bulletShape, bulletTrajectory, power);
		return entity;
	}
	
	/**
	 * Adds the appropriate components to the entity so that it can be used as a bullet.
	 * @param e Entity to setup.
	 * @param bulletShape The bullet's shape.
	 * @param bulletTrajectory The bullet's trajectory.
	 */
	public void setup(Entity e, Entity parent, BulletShapeMaker bulletShape, BulletTrajectoryMaker bulletTrajectory, long power) {
		super.setup(e);
		
		// Setup shape and trajectory.
		bulletShape.setup(e);
		bulletTrajectory.setup(e);
		
		// Setup collision detection and damage calculation.
		ReceiveTouchComponent rtg1c = gameEntityEngine.createComponent(ReceiveTouchGroup01Component.class);
		ParentComponent pc = gameEntityEngine.createComponent(ParentComponent.class);
		BulletStatusComponent bsc = gameEntityEngine.createComponent(BulletStatusComponent.class);
		
		rtg1c.setup(this);
		pc.setup(parent);
		bsc.setup(power);
		
		e.add(bsc);
		e.add(rtg1c);
		e.add(pc);
	}
	
	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
	
	/**
	 * Callback when bullet touched something. We need to calculate the damage and make
	 * the object hit take damage.
	 */
	@Override
	public void callbackTouched(Entity me, Entity you) {
		// Read basic info for damage calculation. 
		final ParentComponent pc = Mapper.parentComponent.get(me);
		final BulletStatusComponent bsc = Mapper.bulletStatusComponent.get(me);
		final StatusValuesComponent svcYou = Mapper.statusValuesComponent.get(you);
		final DamageQueueComponent dqc = Mapper.damageQueueComponent.get(you);
		final TemporalComponent tc = Mapper.temporalComponent.get(me);
		
		if (dqc != null && bsc != null) {
			
			final long attackNum;
			float factor;
			
			// Read info from attacker, his pain points and bullet attack.
			if (pc != null && pc.parent != null) {
				final PainPointsComponent ppc = Mapper.painPointsComponent.get(pc.parent);
				final StatusValuesComponent svcAttacker = Mapper.statusValuesComponent.get(pc.parent);
				factor = ppc != null ? (ppc.painPointsRatio*ppc.painPointsRatio+1.0f) : 1.0f;
				attackNum = svcAttacker != null ? svcAttacker.bulletAttackNum : 1L;
			}
			else {
				factor = 1.0f;
				attackNum = 1L;
			}
			
			factor *= tc.deltaTime * DAMAGE_FREQUENCY;
			
			// Read info from defender, his bullet resistance.
			final long resistanceNum = svcYou != null ? svcYou.bulletResistanceNum : 1L;

			// Attack power is higher the more pain the attacker had to endure.
			final long factorNum = (int)(factor * 1000.0f);
			final long factorDen = 1000L;
			
			// Calculate damage.
			// damage = basePower * attackPower / resistance * (1+painPointsRation^2) * random(0.8..1.2)
			long damage = (bsc.power * factorNum*attackNum)/(resistanceNum*factorDen);
			
			// Apply random variance.
			damage *= MathUtils.random(DAMAGE_LOWER_RANGE, DAMAGE_UPPER_RANGE);
			damage /= 100L;
			
			// Queue defender to take damage.
			dqc.queuedDamage += MathUtils.clamp(damage, 0L, DamageSystem.MAX_PAIN_POINTS - damage);
			
			// Make battle stigma glow.
			if (you == playerEntity && Mapper.colorFlashEffectComponent.get(playerBattleStigmaEntity) == null) {
				final ColorFlashEffectComponent cfec = gameEntityEngine.createComponent(ColorFlashEffectComponent.class);
				cfec.setup(Color.WHITE, player.getBattleStigmaColorWhenHit(), Interpolation.circle, 0.1f, 6);
				playerBattleStigmaEntity.add(cfec);
				VoiceComponent vc = Mapper.voiceComponent.get(playerEntity);
				if (vc != null) SoundPlayer.getInstance().play(ESound.BATTLE_STIGMA_ABSORB,10.0f);
			}
		}
	}
}
