package de.homelab.madgaksha.lotsofbs.entityengine.entity.trajectory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletTrajectoryMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IBehaving;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * A {@link LinearMotionTrajectory} with an initial velocity in the direction of
 * the player.
 * 
 * @author madgaksha
 *
 */
public class LinearAtTargetTrajectory extends BulletTrajectoryMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LinearAtTargetTrajectory.class);

	private final Vector2 v = new Vector2();
	private float velocity = 0.0f;
	private PositionComponent target = new PositionComponent();

	public LinearAtTargetTrajectory() {
		super();
	}

	/**
	 * Sets the initial velocity at which to move at the player. Note that the
	 * direction is managed by this trajectory, {@link #velocity(float)} should
	 * be used directly.
	 * 
	 * @param velocity
	 *            Vector whose length is the initial velocity.
	 */
	@Override
	@Deprecated
	public void velocity(Vector2 velocity) {
		velocity(velocity.len());
	}

	/**
	 * Sets the initial velocity at which to move at the player. Note that the
	 * direction is managed by this trajectory, {@link #velocity(float)} should
	 * be used directly.
	 * 
	 * @param vx
	 *            Vector whose length is the initial velocity.
	 * @param vy
	 *            Vector whose length is the initial velocity.
	 */
	@Override
	@Deprecated
	public void velocity(float vx, float vy) {
		velocity(v.set(vx, vy).len());
	}

	public void velocity(float velocity) {
		this.velocity = velocity;
	}

	public void target(PositionComponent pc) {
		this.target = pc;
	}

	public void target(Entity e) {
		PositionComponent pc = Mapper.positionComponent.get(e);
		if (pc != null)
			this.target = pc;
	}

	@Override
	protected void setup(Entity e) {
		v.set(target.x - getInitialPositionX(), target.y - getInitialPositionY()).nor();
		super.velocity(velocity * v.x, velocity * v.y);
		super.setup(e);
	}

	@Override
	protected IBehaving getBehaviour() {
		return null;
	}

}