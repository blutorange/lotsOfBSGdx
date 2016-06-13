package de.homelab.madgaksha.entityengine.entity;
import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Interpolation;

import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.entityengine.component.AlphaComponent;
import de.homelab.madgaksha.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.entityengine.component.BehaviourComponent;
import de.homelab.madgaksha.entityengine.component.FadeEffectComponent;
import de.homelab.madgaksha.entityengine.component.LifeComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;

public abstract class BulletTrajectoryMaker implements IMortal{
	private float initialPositionX = 0.0f;
	private float initialPositionY = 0.0f;
	private float initialVelocityX = 0.0f;
	private float initialVelocityY = 0.0f;
	private float rotationDegree = 0.0f;
	protected VoicePlayer voicePlayer;
	private final ITimedCallback onBulletDeath = new ITimedCallback() {
		@Override
		public void run(Entity bullet, Object data) {
			BulletMaker.detachBulletFromSiblings(bullet);
			gameEntityEngine.removeEntity(bullet);
		}
	};
	
	/** Time remaining until bullet goes away. */
	protected float lifeTime = 3.0f;
	protected float angularSpeed = 0.0f;
	protected TemporalComponent temporalComponent = null;
	
	/** Adds appropriate components for the trajectory to the entity.
	 *  
	 * @param e Entity to setup.
	 */
	protected void setup(Entity e) {
		final PositionComponent pc = gameEntityEngine.createComponent(PositionComponent.class);
		final VelocityComponent vc = gameEntityEngine.createComponent(VelocityComponent.class);
		final TemporalComponent tc = gameEntityEngine.createComponent(TemporalComponent.class);	
		final BehaviourComponent bc = gameEntityEngine.createComponent(BehaviourComponent.class);
		final LifeComponent lc = gameEntityEngine.createComponent(LifeComponent.class);
		
		final IBehaving brain = getBehaviour();
		
		lc.onDeath = this;
		lc.remainingLife = lifeTime;		
		if (brain != null) bc.brain = brain;
		pc.x = initialPositionX;
		pc.y = initialPositionY;
		vc.x = initialVelocityX;
		vc.y  = initialVelocityY;
		
		if (rotationDegree != 0.0f) {
			RotationComponent rc = gameEntityEngine.createComponent(RotationComponent.class);
			AngularVelocityComponent avc = gameEntityEngine.createComponent(AngularVelocityComponent.class);
			avc.speed = rotationDegree;
			e.add(rc).add(avc);
		}
		
		e.add(bc)
			.add(lc)
			.add(pc)
			.add(tc)
			.add(vc);
		
		if (angularSpeed != 0.0f) {
			final AngularVelocityComponent avc = gameEntityEngine.createComponent(AngularVelocityComponent.class);
			final RotationComponent rc = gameEntityEngine.createComponent(RotationComponent.class);
			avc.setup(angularSpeed);
			e.add(avc)
				.add(rc);
		}
		
	}
	
	protected abstract IBehaving getBehaviour();

	/** Sets the initial position used for making bullets.
	 * 
	 * @param x x-position.
	 * @param y y-position.
	 */
	public void position(float x, float y) {
		initialPositionX = x;
		initialPositionY = y;
	}
	
	/**
	 * @param life The time in seconds the bullet will live until it fades away.
	 */
	public void life(float lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	public void angularSpeed(float angularSpeed) {
		this.angularSpeed = angularSpeed;
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
	
	public void rotation(float degree) {
		rotationDegree = degree;
	}
	
	public void voicePlayer(VoicePlayer vp) {
		this.voicePlayer = vp;
	}
	
	@Override
	public void kill(Entity bullet) {
		FadeEffectComponent fec = gameEntityEngine.createComponent(FadeEffectComponent.class);
		AlphaComponent ac = gameEntityEngine.createComponent(AlphaComponent.class);
		ac.alpha = 1.0f;
		fec.setup(0.35f, 1.0f, 0.0f, Interpolation.linear, onBulletDeath);
		bullet.add(fec);
		bullet.add(ac);
		
		bullet.remove(ReceiveTouchGroup01Component.class);
		bullet.remove(LifeComponent.class);
	}
}
