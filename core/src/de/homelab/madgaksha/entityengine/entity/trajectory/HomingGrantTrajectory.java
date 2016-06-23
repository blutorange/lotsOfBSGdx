package de.homelab.madgaksha.entityengine.entity.trajectory;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ShouldPositionComponent;
import de.homelab.madgaksha.entityengine.component.StickyComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.entity.BulletTrajectoryMaker;
import de.homelab.madgaksha.entityengine.entity.IBehaving;
import de.homelab.madgaksha.grantstrategy.IGrantStrategy;
import de.homelab.madgaksha.grantstrategy.ImmediateGrantStrategy;
import de.homelab.madgaksha.logging.Logger;

public class HomingGrantTrajectory extends BulletTrajectoryMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(HomingGrantTrajectory.class);

	private PositionComponent positionComponent = null;
	private IGrantStrategy grantStrategy = new ImmediateGrantStrategy();
	private boolean ignoreTrackOffset = false;

	public HomingGrantTrajectory() {
		super();
	}

	public void grant(IGrantStrategy grantStrategy) {
		this.grantStrategy = grantStrategy;
	}

	public void target(PositionComponent pc) {
		this.positionComponent = pc;
	}

	public void ignoreTrackOffset(boolean ignoreTrackOffset) {
		this.ignoreTrackOffset = ignoreTrackOffset;
	}

	@Override
	protected void setup(Entity e) {
		super.setup(e);

		final ShouldPositionComponent spc = gameEntityEngine.createComponent(ShouldPositionComponent.class);
		final StickyComponent sc = gameEntityEngine.createComponent(StickyComponent.class);

		spc.grantStrategy = grantStrategy;
		sc.stickToPositionComponent = positionComponent;
		sc.ignoreTrackOffset = ignoreTrackOffset;
		e.remove(VelocityComponent.class);

		e.add(spc).add(sc);
	}

	@Override
	protected IBehaving getBehaviour() {
		return null;
	}

}