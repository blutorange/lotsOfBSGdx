package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.GlobalBag.gameScore;
import static de.homelab.madgaksha.GlobalBag.playerEntity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.AnyChildComponent;
import de.homelab.madgaksha.entityengine.component.BulletStatusComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.GetHitComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.ParentComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.SiblingComponent;
import de.homelab.madgaksha.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.zorder.ZOrder1Component;
import de.homelab.madgaksha.entityengine.entitysystem.DamageSystem;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETextureAtlas;
import de.homelab.madgaksha.resourcecache.IResource;

public class BulletMaker extends EntityMaker {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BulletMaker.class);

	/** Frequency at which an entity hit by a bullet takes 1x damage, in Hz. */
	private final static float DAMAGE_FREQUENCY = 20.0f;
	/** Lower range of the random damage variance, in percent. */
	private final static long DAMAGE_LOWER_RANGE = 80L;
	/** Upper range of the random damage variance, in percent. */
	private final static long DAMAGE_UPPER_RANGE = 120L;

	private VoicePlayer scoreBulletVoicePlayer = new VoicePlayer();
	
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

	public static void makeForPlayer(BulletShapeMaker bulletShape, BulletTrajectoryMaker bulletTrajectory,
			long power) {
		final Entity entity = gameEntityEngine.createEntity();
		final ReceiveTouchComponent rtc = gameEntityEngine.createComponent(ReceiveTouchGroup02Component.class);
		gameEntityEngine.createComponent(ReceiveTouchGroup01Component.class);
		SingletonHolder.INSTANCE.setup(entity, playerEntity, rtc, bulletShape, bulletTrajectory, onDamageBulletHit, power);
		gameEntityEngine.addEntity(entity);
	}

	public static void makeForEnemy(Entity enemy, BulletShapeMaker bulletShape, BulletTrajectoryMaker bulletTrajectory,
			long power) {
		final Entity entity = gameEntityEngine.createEntity();
		final ReceiveTouchComponent rtc = gameEntityEngine.createComponent(ReceiveTouchGroup01Component.class);
		SingletonHolder.INSTANCE.setup(entity, enemy, rtc, bulletShape, bulletTrajectory, onDamageBulletHit, power);
		gameEntityEngine.addEntity(entity);
	}

	public static void makeAsScoreBullet(BulletShapeMaker bulletShape, BulletTrajectoryMaker bulletTrajectory,
			long score) {
		final Entity entity = gameEntityEngine.createEntity();
		final ReceiveTouchComponent rtc = gameEntityEngine.createComponent(ReceiveTouchGroup01Component.class);
		SingletonHolder.INSTANCE.setup(entity, playerEntity, rtc, bulletShape, bulletTrajectory, onScoreBulletHit, score);
		gameEntityEngine.addEntity(entity);
	}

	/**
	 * Adds the appropriate components to the entity so that it can be used as a
	 * bullet.
	 * 
	 * @param e
	 *            Entity to setup.
	 * @param bulletShape
	 *            The bullet's shape.
	 * @param bulletTrajectory
	 *            The bullet's trajectory.
	 */
	private void setup(Entity e, Entity parent, ReceiveTouchComponent rtc, BulletShapeMaker bulletShape,
			BulletTrajectoryMaker bulletTrajectory, IReceive hitHandler, long power) {
		super.setup(e);

		final AnyChildComponent acc = Mapper.anyChildComponent.get(parent);
		final SiblingComponent siblingEntryPoint = acc.childComponent;
		
		// Setup shape and trajectory.
		bulletShape.setup(e);
		bulletTrajectory.setup(e);

		// Setup collision detection and damage calculation.
		final ParentComponent pc = gameEntityEngine.createComponent(ParentComponent.class);
		final BulletStatusComponent bsc = gameEntityEngine.createComponent(BulletStatusComponent.class);
		final ZOrder1Component zoc = gameEntityEngine.createComponent(ZOrder1Component.class);
		final SiblingComponent sc = gameEntityEngine.createComponent(SiblingComponent.class);

		rtc.triggerReceivingObject = hitHandler;
		pc.parent = parent;
		bsc.setup(power, bulletShape.score);

		// Setup linked list of bullets belonging to this entity (enemy/player).
		sc.prevSiblingComponent = siblingEntryPoint;
		if (siblingEntryPoint != null) {
			sc.nextSiblingComponent = siblingEntryPoint.nextSiblingComponent;
			if (siblingEntryPoint.nextSiblingComponent != null)
				siblingEntryPoint.nextSiblingComponent.prevSiblingComponent = sc;
			siblingEntryPoint.nextSiblingComponent = sc;
		}
		sc.me = e;
		acc.childComponent = sc;

		e.add(sc)
			.add(bsc)
			.add(rtc)
			.add(pc)
			.add(zoc);
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return new IResource<?, ?>[] { ETextureAtlas.BULLETS_BASIC, };
	}

	/**
	 * Callback when damage bullet touched something. We need to calculate the
	 * damage and make the object hit take damage.
	 */
	private final static IReceive onDamageBulletHit = new IReceive() {
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
					factor = ppc != null ? (ppc.painPointsRatio * ppc.painPointsRatio + 1.0f) : 1.0f;
					attackNum = svcAttacker != null ? svcAttacker.bulletAttackNum : 1L;
				} else {
					factor = 1.0f;
					attackNum = 1L;
				}

				factor *= tc.deltaTime * DAMAGE_FREQUENCY;

				// Read info from defender, his bullet resistance.
				final long resistanceNum = svcYou != null ? svcYou.bulletResistanceNum : 1L;

				// Attack power is higher the more pain the attacker had to
				// endure.
				final long factorNum = (int) (factor * 1000.0f);
				final long factorDen = 1000L;

				// Calculate damage.
				// damage = basePower * attackPower / resistance *
				// (1+painPointsRation^2) * random(0.8..1.2)
				long damage = (bsc.power * factorNum * attackNum) / (resistanceNum * factorDen);

				// Apply random variance.
				damage *= MathUtils.random(DAMAGE_LOWER_RANGE, DAMAGE_UPPER_RANGE);
				damage /= 100L;

				// Queue defender to take damage.
				dqc.queuedDamage += MathUtils.clamp(damage, 0L, DamageSystem.MAX_PAIN_POINTS - damage);

				// Custom stuff on getting hit.
				GetHitComponent ghc = Mapper.getHitComponent.get(you);
				if (ghc != null)
					ghc.hittable.hitByBullet(you, me);
			}
		}
	};

	/**
	 * Callback when a score bullet touched something.
	 */
	private final static IReceive onScoreBulletHit = new IReceive() {
		@Override
		public void callbackTouched(Entity bullet, Entity player) {
			final BulletStatusComponent bsc = Mapper.bulletStatusComponent.get(bullet);
			SingletonHolder.INSTANCE.scoreBulletVoicePlayer.play(ESound.SCORE_BULLET_HIT, false);
			if (bsc != null) gameScore.increaseBy(bsc.power);
			detachBulletFromSiblings(bullet);
			gameEntityEngine.removeEntity(bullet);
		}
	};
	
	/**
	 * Removes the bullet from the underlying linked list.
	 * @param bullet Bullet to remove.
	 */
	static void detachBulletFromSiblings(Entity bullet) {
		final SiblingComponent sc = Mapper.siblingComponent.get(bullet);
		final AnyChildComponent acc = Mapper.anyChildComponent.get(Mapper.parentComponent.get(bullet).parent);
		acc.childComponent = null;
		if (sc.prevSiblingComponent != null) {
			sc.prevSiblingComponent.nextSiblingComponent = sc.nextSiblingComponent;
			acc.childComponent = sc.prevSiblingComponent;
		}
		if (sc.nextSiblingComponent != null) {
			sc.nextSiblingComponent.prevSiblingComponent = sc.prevSiblingComponent;
			
			acc.childComponent = sc.nextSiblingComponent;
		}
		sc.prevSiblingComponent = null;
		sc.nextSiblingComponent = null;
	}
}
