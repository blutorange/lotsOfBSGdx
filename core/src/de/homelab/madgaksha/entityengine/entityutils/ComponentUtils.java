package de.homelab.madgaksha.entityengine.entityutils;

import static de.homelab.madgaksha.GlobalBag.cameraEntity;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.BulletStatusComponent;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.EnemyIconComponent;
import de.homelab.madgaksha.entityengine.component.GetHitComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.QuakeEffectComponent;
import de.homelab.madgaksha.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.entityengine.component.AnimationModeListComponent.AnimationMode;
import de.homelab.madgaksha.entityengine.component.AnimationModeQueueComponent;
import de.homelab.madgaksha.entityengine.component.StatusValuesComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.entityengine.entitysystem.DamageSystem;
import de.homelab.madgaksha.enums.RichterScale;
import de.homelab.madgaksha.field.SpiralVelocityField;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.util.GeoUtil;

/**
 * Utilities for working with an entity's pain points.
 * 
 * @author madgaksha
 *
 */
public final class ComponentUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ComponentUtils.class);
	private final static Vector2 v1 = new Vector2();
	@SuppressWarnings("unchecked")
	private final static Family FAMILY_BULLET = Family.all(BulletStatusComponent.class).get();
	@SuppressWarnings("unchecked")
	private final static Family FAMILY_ENEMY = Family.all(EnemyIconComponent.class).exclude(InactiveComponent.class)
			.get();

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

	public static void setBulletAction(boolean active) {
		if (active)
			for (Entity e : gameEntityEngine.getEntitiesFor(FAMILY_BULLET))
				e.remove(InactiveComponent.class);
		else
			for (Entity e : gameEntityEngine.getEntitiesFor(FAMILY_BULLET))
				e.add(gameEntityEngine.createComponent(InactiveComponent.class));

	}

	public static void convertAllActiveBulletsToScoreBullets() {
		for (Entity enemy : gameEntityEngine.getEntitiesFor(FAMILY_ENEMY)) {
			EnemyMaker.releaseBullets(enemy, true);
		}
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

	/**
	 * Use {@link SpiralVelocityField} instead.
	 */
	// /**
	// * Circles on a spiral around the target, keeping at a minimum distance.
	// * @param who
	// * @param toWhom
	// * @param speedRadial
	// * @param speedTangential
	// * @param minDistance
	// */
	// public static void moveIntoDirection(Entity who, Entity toWhom, float
	// speedRadial, float speedTangential, float minDistance) {
	// VelocityComponent vcWho = Mapper.velocityComponent.get(who);
	// PositionComponent pcWho = Mapper.positionComponent.get(who);
	// PositionComponent pcToWhom = Mapper.positionComponent.get(toWhom);
	// v1.set(pcToWhom.x, pcToWhom.y).sub(pcWho.x, pcWho.y);
	// float len = v1.len();
	// if (len < minDistance) return;
	// v1.scl(1.0f/len);
	// vcWho.x = speedRadial * v1.x + speedTangential * v1.y;
	// vcWho.y = speedRadial * v1.y - speedTangential * v1.x;
	// }

	public static void lookIntoMovementDirection(Entity who) {
		final VelocityComponent vc = Mapper.velocityComponent.get(who);
		final DirectionComponent dc = Mapper.directionComponent.get(who);
		v1.set(vc.x, vc.y);
		dc.degree = 630.0f - v1.angle();
	}

	public static void scaleBoundingBoxRenderOfEntityBy(Entity e, float initialScaleX, float initialScaleY) {
		ABoundingBoxComponent bbrc = Mapper.boundingBoxRenderComponent.get(e);
		if (bbrc != null)
			scaleBoundingBoxOfEntityBy(bbrc, e, initialScaleX, initialScaleY);
	}

	public static void scaleBoundingBoxCollisionOfEntityBy(Entity e, float initialScaleX, float initialScaleY) {
		ABoundingBoxComponent bbcc = Mapper.boundingBoxCollisionComponent.get(e);
		if (bbcc != null)
			scaleBoundingBoxOfEntityBy(bbcc, e, initialScaleX, initialScaleY);
	}

	public static void scaleBoundingBoxOfEntityBy(ABoundingBoxComponent bbc, Entity e, float initialScaleX,
			float initialScaleY) {
		float cx = 0.5f * (bbc.maxX + bbc.minX);
		float cy = 0.5f * (bbc.maxY + bbc.minY);
		float hw = 0.5f * (bbc.maxX - bbc.minX);
		float hh = 0.5f * (bbc.maxY - bbc.minY);
		bbc.maxX = cx + initialScaleX * hw;
		bbc.minX = cx - initialScaleX * hw;
		bbc.maxY = cy + initialScaleY * hh;
		bbc.minY = cy - initialScaleY * hh;
	}

	public static void scaleShapeOfEntityBy(Entity e, float initialScaleX, float initialScaleY) {
		final ShapeComponent sc = Mapper.shapeComponent.get(e);
		if (sc == null)
			return;
		GeoUtil.scaleShapeBy(sc, initialScaleX, initialScaleY);
	}

	/**
	 * Adds the given sprite mode to the entities queue. It needs a
	 * {@link AnimationModeQueueComponent}.
	 * 
	 * @param e
	 *            Entity to setup.
	 * @param targetMode
	 *            Mode to transition to.
	 * @param waitForCompletion
	 *            Whether we should wait for the current animation to complete.
	 * @param resetAnimation
	 *            Whether the new animation should be reset.
	 */
	public static void transitionToSpriteMode(Entity e, AnimationMode targetMode, boolean waitForCompletion,
			boolean resetAnimation) {
		final AnimationModeQueueComponent smqc = Mapper.animationModeQueueComponent.get(e);
		if (smqc != null) {
			smqc.queue(targetMode, waitForCompletion, resetAnimation);
		}
	}

	/**
	 * Same as
	 * {@link #transitionToSpriteMode(Entity, AnimationMode, boolean, boolean)},
	 * but defaults to true for waitForCompletion and resetAnimation.
	 * 
	 * @param e
	 *            Entity to setup.
	 * @param targetMode
	 *            Mode to transition to.
	 */
	public static void transitionToSpriteMode(Entity e, AnimationMode targetMode) {
		transitionToSpriteMode(e, targetMode, true, true);
	}
}
