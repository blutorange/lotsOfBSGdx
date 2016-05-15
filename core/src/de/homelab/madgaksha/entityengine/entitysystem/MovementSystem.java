package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TimeScaleComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.level.ALevel;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class MovementSystem extends IteratingSystem {

	private float worldXMin, worldXMax;
	private float worldYMin, worldYMax;
	
	public MovementSystem() {
		this(DefaultPriority.movementSystem);
	}

	@SuppressWarnings("unchecked")
	public MovementSystem(int priority) {
		super(Family.all(PositionComponent.class, VelocityComponent.class).get(), priority);
		worldXMin = GlobalBag.level.getMapXW();
		worldYMin = GlobalBag.level.getMapYW();
		worldXMax = GlobalBag.level.getMapWidthW();
		worldYMax = GlobalBag.level.getMapHeightW();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PositionComponent rc = Mapper.positionComponent.get(entity);
		final VelocityComponent vc = Mapper.velocityComponent.get(entity);
		final TimeScaleComponent tsfc = Mapper.timeScaleComponent.get(entity);
		if (tsfc != null)
			deltaTime *= tsfc.timeScalingFactor;
		rc.x += vc.x * deltaTime;
		rc.y += vc.y * deltaTime;
		rc.z += vc.z * deltaTime;
		if (rc.limitToMap) {
			rc.x = MathUtils.clamp(rc.x, worldXMin, worldXMax);
			rc.y = MathUtils.clamp(rc.y, worldYMin, worldYMax);
		}
	}

}
