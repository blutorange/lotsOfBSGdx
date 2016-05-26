package de.homelab.madgaksha.entityengine.entity.trajectory;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.entity.BulletTrajectoryMaker;
import de.homelab.madgaksha.logging.Logger;

public class LinearMotionTrajectory extends BulletTrajectoryMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LinearMotionTrajectory.class);

	public LinearMotionTrajectory () {
		super();
	}
	
	@Override
	protected void setup(Entity e) {
		super.setup(e);
	}
	
	@Override
	public void update(Entity e) {
		
	}
}