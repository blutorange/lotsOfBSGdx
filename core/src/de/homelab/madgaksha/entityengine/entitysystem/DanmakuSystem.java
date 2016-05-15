package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.ForceComponent;
import de.homelab.madgaksha.entityengine.component.TrajectoryComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;

/**
 * Updates the force component velocity component of each bullet according to
 * its trajectory.
 * 
 * @author madgaksha
 *
 */
public class DanmakuSystem extends IteratingSystem {

	private static Vector2 v = new Vector2();
	
	public DanmakuSystem() {
		this(DefaultPriority.danmakuSystem);
	}
	@SuppressWarnings("unchecked")
	public DanmakuSystem(int priority) {
		super(Family.all(VelocityComponent.class,TrajectoryComponent.class,ForceComponent.class).get(),priority);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TrajectoryComponent tc = Mapper.trajectoryComponent.get(entity);
		ForceComponent fc = Mapper.forceComponent.get(entity);
		VelocityComponent vc = Mapper.velocityComponent.get(entity);
		v.set(vc.x, vc.y).rotate(tc.angleToVelocity);
		fc.x = tc.forceCoefficient*v.x;
		fc.y = tc.forceCoefficient*v.y;
	}

}
