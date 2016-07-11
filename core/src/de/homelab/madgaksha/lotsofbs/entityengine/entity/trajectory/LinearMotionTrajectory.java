package de.homelab.madgaksha.lotsofbs.entityengine.entity.trajectory;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletTrajectoryMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IBehaving;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class LinearMotionTrajectory extends BulletTrajectoryMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LinearMotionTrajectory.class);

	public LinearMotionTrajectory() {
		super();
	}

	@Override
	protected void setup(Entity e) {
		super.setup(e);
	}

	@Override
	protected IBehaving getBehaviour() {
		return null;
	}
}