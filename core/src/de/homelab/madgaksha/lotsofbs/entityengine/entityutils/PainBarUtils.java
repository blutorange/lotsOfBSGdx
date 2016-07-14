package de.homelab.madgaksha.lotsofbs.entityengine.entityutils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.DamageSystem;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Utilities for working with an entity's pain points. Usually processed by an
 * appropriate entity system, this should be used sparingly.
 * 
 * @author madgaksha
 *
 */
public class PainBarUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PainBarUtils.class);

	/**
	 * @param e
	 *            Entity to check for pain points
	 * @return Current pain points, or -1 if it does not possess pain points.
	 */
	public static long getPainPoints(Entity e) {
		final PainPointsComponent ppc = Mapper.painPointsComponent.get(e);
		return ppc == null ? -1L : ppc.painPoints;
	}

	/**
	 * @param e
	 *            Entity to check for pain points ratio.
	 * @return Current pain points ratio, or -1.0f if it does not possess pain
	 *         points.
	 */
	public static float getPainPointsRatio(Entity e) {
		final PainPointsComponent ppc = Mapper.painPointsComponent.get(e);
		return ppc == null ? -1.0f : ppc.painPointsRatio;
	}
	
	public static boolean isEntityFullyHealed(Entity e) {
		final PainPointsComponent ppc = Mapper.painPointsComponent.get(e);
		return ppc == null ? true : ppc.painPoints == 0L;
	}

	public static void queueDamage(long damage, DamageQueueComponent dqc) {
		damage = MathUtils.clamp(damage, 0, DamageSystem.MAX_PAIN_POINTS);
		if (dqc.queuedDamage > 0L && damage > DamageSystem.MAX_PAIN_POINTS - dqc.queuedDamage) damage = DamageSystem.MAX_PAIN_POINTS - dqc.queuedDamage;
		dqc.queuedDamage += damage;		
	}

	public static void queueHeal(long heal, DamageQueueComponent dqc) {
		heal = MathUtils.clamp(heal, 0L, DamageSystem.MAX_PAIN_POINTS);
		if (dqc.queuedDamage < 0L && heal > DamageSystem.MAX_PAIN_POINTS - dqc.queuedDamage) heal = DamageSystem.MAX_PAIN_POINTS - dqc.queuedDamage; 
		dqc.queuedDamage -= heal;
	}
}
