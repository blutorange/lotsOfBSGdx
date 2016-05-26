package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class DamageSystem extends IteratingSystem {
	
	public DamageSystem() {
		this(DefaultPriority.damageSystem);
	}

	@SuppressWarnings("unchecked")
	public DamageSystem(int priority) {
		super(Family.all(PainPointsComponent.class, DamageQueueComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PainPointsComponent ppc = Mapper.painPointsComponent.get(entity);
		final DamageQueueComponent dqc = Mapper.damageQueueComponent.get(entity);

		// Change pain points accordingly.
		ppc.painPoints = MathUtils.clamp(ppc.painPoints + dqc.queuedDamage, 0L, ppc.maxPainPoints);
		dqc.queuedDamage = 0L;
		ppc.painPointsRatio = ((float)ppc.painPoints) / ((float)ppc.maxPainPoints);
		
		// Check if entity just died :(
		if (ppc.painPoints == ppc.maxPainPoints) {
			// TODO
		}
	}
}
