package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DeathComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 *
 * @author madgaksha
 */
public class DamageSystem extends IteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DamageQueueComponent.class);
	public static final long MAX_PAIN_POINTS = 999999999999L; // 10^12-1
	public static final int NUMBER_OF_DIGITS = 12;
	/**
	 * In per-mille (0.001). If damage/maxHP is greater than this theshold,
	 * other voices will be played etc.
	 */
	public static final long THRESHOLD_LIGHT_HEAVY_DAMAGE = 10L;

	public DamageSystem() {
		this(DefaultPriority.damageSystem);
	}

	public DamageSystem(final int priority) {
		super(Family.all(PainPointsComponent.class, DamageQueueComponent.class).exclude(InactiveComponent.class).get(),
				priority);
	}

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
		final PainPointsComponent ppc = Mapper.painPointsComponent.get(entity);
		final DamageQueueComponent dqc = Mapper.damageQueueComponent.get(entity);

		// Change pain points accordingly.
		if (dqc.queuedDamage != 0L) {
			// take damage
			final VoiceComponent vc = Mapper.voiceComponent.get(entity);

			ppc.painPoints = MathUtils.clamp(ppc.painPoints + dqc.queuedDamage, 0L, ppc.maxPainPoints);
			if (dqc.keepOneHp) {
				ppc.painPoints = Math.min(ppc.maxPainPoints - 1, ppc.painPoints);
				dqc.keepOneHp = false;
			}
			ppc.updatePainPoints();
			// Check if entity just died :(
			if (ppc.painPoints == ppc.maxPainPoints) {
				final DeathComponent dc = Mapper.deathComponent.get(entity);
				if (dc != null && !dc.dead) {
					if (vc != null && vc.voicePlayer != null)
						vc.voicePlayer.playUnconditionally(vc.onDeath);
					dc.reaper.kill(entity);
					dc.dead = true;
				}
			}
			// Otherwise, play sound on taking damage
			else if (vc != null && vc.voicePlayer != null) {
				if (dqc.queuedDamage > 0L) {
					vc.voicePlayer.play((dqc.queuedDamage * 1000L < THRESHOLD_LIGHT_HEAVY_DAMAGE * ppc.maxPainPoints)
							? vc.onLightDamage : vc.onHeavyDamage);
				}
				else {
					vc.voicePlayer.play((-dqc.queuedDamage * 1000L < THRESHOLD_LIGHT_HEAVY_DAMAGE * ppc.maxPainPoints)
							? vc.onLightHeal : vc.onHeavyHeal);
				}
			}
		}
		dqc.queuedDamage = 0L;
	}
}
