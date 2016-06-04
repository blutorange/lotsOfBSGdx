package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.DeathComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.logging.Logger;

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

	@SuppressWarnings("unchecked")
	public DamageSystem(int priority) {
		super(Family.all(PainPointsComponent.class, DamageQueueComponent.class).exclude(InactiveComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PainPointsComponent ppc = Mapper.painPointsComponent.get(entity);
		final DamageQueueComponent dqc = Mapper.damageQueueComponent.get(entity);

		// Change pain points accordingly.
		if (dqc.queuedDamage != 0L) {
			VoiceComponent vc = Mapper.voiceComponent.get(entity);

			ppc.painPoints = MathUtils.clamp(ppc.painPoints + dqc.queuedDamage, 0L, ppc.maxPainPoints);
			ppc.updatePainPoints();
			// Check if entity just died :(
			if (ppc.painPoints == ppc.maxPainPoints) {
				DeathComponent dc = Mapper.deathComponent.get(entity);
				if (dc != null && !dc.dead) {
					if (vc != null && vc.voicePlayer != null)
						vc.voicePlayer.playUnconditionally(vc.onDeath);
					dc.reaper.kill(entity);
					dc.dead = true;
				}
			}
			// Otherwise, play sound on taking damage
			else if (vc != null && vc.voicePlayer != null && dqc.queuedDamage > 0) {
				vc.voicePlayer.play((dqc.queuedDamage * 1000L < THRESHOLD_LIGHT_HEAVY_DAMAGE * ppc.maxPainPoints)
						? vc.onLightDamage : vc.onHeavyDamage);
			}
			dqc.queuedDamage = 0L;
		}

	}
}
