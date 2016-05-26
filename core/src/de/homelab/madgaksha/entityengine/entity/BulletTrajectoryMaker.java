package de.homelab.madgaksha.entityengine.entity;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;

public abstract class BulletTrajectoryMaker {
	private float initialPositionX = 0.0f;
	private float initialPositionY = 0.0f;
	private float initialVelocityX = 0.0f;
	private float initialVelocityY = 0.0f;
	
	/** Adds appropriate components for the trajectory to the entity.
	 *  
	 * @param e Entity to setup.
	 */
	protected void setup(Entity e) {
		final PositionComponent pc = gameEntityEngine.createComponent(PositionComponent.class);
		final VelocityComponent vc = gameEntityEngine.createComponent(VelocityComponent.class);
		final TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);	

		pc.setup(initialPositionX, initialPositionY);
		vc.setup(initialVelocityX, initialVelocityY);
		
		e.add(pc);
		e.add(vc);
		e.add(tc);
	}
	
	/** Sets the initial position used for making bullets.
	 * 
	 * @param x x-position.
	 * @param y y-position.
	 */
	public void position(float x, float y) {
		initialPositionX = x;
		initialPositionY = y;
	}
	
	/** Sets the initial velocity used for making bullets.
	 * 
	 * @param vx Velocity in x-direction.
	 * @param vy Velocity in y-direction.
	 */
	public void velocity(float vx, float vy) {
		initialVelocityX = vx;
		initialVelocityY = vy;
	}
	
	/** 
	 * Updates the entity's position for the next frame. 
	 * @param e Entity to update.
	 */
	public abstract void update(Entity e);
}
