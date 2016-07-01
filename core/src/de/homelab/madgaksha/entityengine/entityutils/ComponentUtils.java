package de.homelab.madgaksha.entityengine.entityutils;

import static de.homelab.madgaksha.GlobalBag.cameraEntity;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.GetHitComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.QuakeEffectComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.SpriteForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.entitysystem.DamageSystem;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.enums.RichterScale;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;

/**
 * Utilities for working with an entity's pain points. Usually processed by an
 * appropriate entity system, this should be used sparingly.
 * 
 * @author madgaksha
 *
 */
public final class ComponentUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ComponentUtils.class);
	private final static Vector2 v1 = new Vector2();

	/** Frequency at which an entity hit by a bullet takes 1x damage, in Hz. */
	public final static float DAMAGE_FREQUENCY = 20.0f;
	/** Lower range of the random damage variance, in percent. */
	public final static long DAMAGE_LOWER_RANGE = 80L;
	/** Upper range of the random damage variance, in percent. */
	public final static long DAMAGE_UPPER_RANGE = 120L;
	
	public static void applyComponentQueue(Entity e, ComponentQueueComponent cqc) {
		for (Class<? extends Component> c : cqc.remove)
			e.remove(c);
		for (Component c : cqc.add)
			e.add(c);
		cqc.add.clear();
		cqc.remove.clear();
	}

	/** Applied the component queue of the entity, if it exists. */
	public static void applyComponentQueue(Entity e) {
		final ComponentQueueComponent cqc = Mapper.componentQueueComponent.get(e);
		if (cqc != null)
			applyComponentQueue(e, cqc);
	}

	/**
	 * Switches the animation list to the given animation list.
	 * 
	 * @param entity
	 *            Entity whose sprite animation list needs to be changed.
	 * @param animationList
	 *            The new animation list.
	 */
	public static void switchAnimationList(Entity entity, EAnimationList animationList) {
		SpriteForDirectionComponent sfdc = Mapper.spriteForDirectionComponent.get(entity);
		SpriteAnimationComponent sac = Mapper.spriteAnimationComponent.get(entity);
		SpriteComponent sc = Mapper.spriteComponent.get(entity);
		if (sfdc != null && sac != null && sc != null) {
			sfdc.setup(animationList, ESpriteDirectionStrategy.ZENITH);
			sac.setup(sfdc);
			sc.setup(sac);
		}
	}

	public static void enableScreenQuake(float amplitude, float frequency) {
		final QuakeEffectComponent qec = gameEntityEngine.createComponent(QuakeEffectComponent.class);
		qec.setup(amplitude, frequency);
		cameraEntity.add(qec);
	}

	public static void enableScreenQuake(RichterScale quake) {
		enableScreenQuake(quake.amplitude, quake.frequency);
	}

	public static void disableScreenQuake() {
		cameraEntity.remove(QuakeEffectComponent.class);
	}

	public static void lookIntoDirection(Entity who, Entity atWhom) {
		final PositionComponent pcWho = Mapper.positionComponent.get(who);
		final PositionComponent pcAtWhom = Mapper.positionComponent.get(atWhom);
		final DirectionComponent dc = Mapper.directionComponent.get(who);
		v1.set(pcAtWhom.x - pcWho.x, pcAtWhom.y - pcWho.y);
		dc.degree = 630.0f - v1.angle();
	}
	
	public static void dealDamage(Entity attacker, Entity defender, long basePower, boolean keepOneHp) {
		// Read basic info for damage calculation.
		final StatusValuesComponent svcAttacker = attacker == null ? null : Mapper.statusValuesComponent.get(attacker);
		final PainPointsComponent ppcAttacker = attacker == null ? null : Mapper.painPointsComponent.get(attacker);
		final TemporalComponent tcAttacker = attacker == null ? null : Mapper.temporalComponent.get(attacker);

		final StatusValuesComponent svcDefender = Mapper.statusValuesComponent.get(defender);
		final DamageQueueComponent dqc = Mapper.damageQueueComponent.get(defender);

		if (dqc != null) {
			long damage = basePower;
			final long attackNum;
			float factor;

			if (svcDefender != null && ppcAttacker != null && tcAttacker != null && svcAttacker != null) {
				// Read info from defender, his bullet resistance.
				final long resistanceNum = svcDefender.bulletResistanceNum;
	
				// Read info from attacker, his pain points and bullet attack.
				factor = (ppcAttacker.painPointsRatio * ppcAttacker.painPointsRatio + 1.0f);
				attackNum = svcAttacker.bulletAttackNum;
	
				factor *= tcAttacker.deltaTime * DAMAGE_FREQUENCY;
	
				// Attack power is higher the more pain the attacker had to
				// endure.
				final long factorNum = (int) (factor * 1000.0f);
				final long factorDen = 1000L;
	
				// Calculate damage.
				// damage = basePower * attackPower / resistance *
				// (1+painPointsRation^2) * random(0.8..1.2)
				damage = (basePower * factorNum * attackNum) / (resistanceNum * factorDen);
			}
			
			// Apply random variance.
			damage *= MathUtils.random(DAMAGE_LOWER_RANGE, DAMAGE_UPPER_RANGE);
			damage /= 100L;

			// Queue defender to take damage.
			dqc.queuedDamage += MathUtils.clamp(damage, 0L, DamageSystem.MAX_PAIN_POINTS - damage);
			dqc.keepOneHp = dqc.keepOneHp || keepOneHp;
			
			// Custom stuff on getting hit.
			GetHitComponent ghc = Mapper.getHitComponent.get(defender);
			if (ghc != null)
				ghc.hittable.hitByBullet(defender, attacker);
		}
	}
}
