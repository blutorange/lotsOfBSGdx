package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.DisableIteratingSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ConeDistributionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Updates an object's position its velocity over a small time step dt.
 * 
 * @author madgaksha
 */
public class ConeDistributionSystem extends DisableIteratingSystem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ConeDistributionSystem.class);

	private final Vector2 v1 = new Vector2();
	
	public ConeDistributionSystem() {
		this(DefaultPriority.coneDistributionSystem);
	}

	@SuppressWarnings("unchecked")
	public ConeDistributionSystem(int priority) {
		super(DisableIteratingSystem.all(PositionComponent.class, ConeDistributionComponent.class, TemporalComponent.class)
				.exclude(InactiveComponent.class), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PositionComponent pc = Mapper.positionComponent.get(entity);
		final ConeDistributionComponent cdc = Mapper.coneDistributionComponent.get(entity);
		final TemporalComponent tc = Mapper.temporalComponent.get(entity);

		// sanitize
		final int size = cdc.distributionPoints.size();
		if (size == 0) return;
		cdc.apexPoint = cdc.apexPoint < size ? cdc.apexPoint : size;

		// move to apex point and position component 
		v1.set(pc.x, pc.y).add(cdc.offsetToApex);
		cdc.distributionPoints.get(cdc.apexPoint).setup(v1);
		
		// move to the center
		v1.add(cdc.offsetToBase);
		
		// update the current rotation
		cdc.degrees += cdc.angularVelocity * tc.deltaTime;
		
		// and position the other components on an ellipse
		final float deltaAngle = 360.0f / (float)(size-1);
		float angle = cdc.degrees;
		for (int i = 0; i != cdc.apexPoint; ++i) {
			final PositionComponent point = cdc.distributionPoints.get(i);
			point.x = v1.x + MathUtils.cosDeg(angle) * cdc.radius1;
			point.y = v1.y + MathUtils.sinDeg(angle) * cdc.radius2;
			angle += deltaAngle;			
		}
		for (int i = cdc.apexPoint + 1; i != size; ++i) {
			final PositionComponent point = cdc.distributionPoints.get(i);
			point.x = v1.x + MathUtils.cosDeg(angle) * cdc.radius1;
			point.y = v1.y + MathUtils.sinDeg(angle) * cdc.radius2;
			angle += deltaAngle;			
		}
	}
}
