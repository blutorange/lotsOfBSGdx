package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.audiosystem.VoicePlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AlphaComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AngularVelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.BehaviourComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.FadeEffectComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.LifeComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ScaleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public abstract class BulletTrajectoryMaker implements IMortal {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BulletTrajectoryMaker.class);

	private float initialPositionX = 0.0f;
	private float initialPositionY = 0.0f;
	private float initialVelocityX = 0.0f;
	private float initialVelocityY = 0.0f;
	private float initialScaleX = 1.0f;
	private float initialScaleY = 1.0f;

	protected VoicePlayer voicePlayer;
	private final IEntityCallback onBulletDeath = new IEntityCallback() {
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

	/**
	 * Adds appropriate components for the trajectory to the entity.
	 * 
	 * @param e
	 *            Entity to setup.
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
		if (brain != null)
			bc.brain = brain;
		pc.x = initialPositionX;
		pc.y = initialPositionY;
		vc.x = initialVelocityX;
		vc.y = initialVelocityY;

		// Scale if desired.
		if (initialScaleX != 1.0f || initialScaleY != 1.0f) {
			ScaleComponent scc = getScaleComponent(e);
			scc.setup(initialScaleX, initialScaleX);
			ComponentUtils.scaleShapeOfEntityBy(e, initialScaleX, initialScaleY);
			ComponentUtils.scaleBoundingBoxRenderOfEntityBy(e, initialScaleX, initialScaleY);
			ComponentUtils.scaleBoundingBoxCollisionOfEntityBy(e, initialScaleX, initialScaleY);
		}

		e.add(bc).add(lc).add(pc).add(tc).add(vc);

		if (angularSpeed != 0.0f) {
			final RotationComponent rc = gameEntityEngine.createComponent(RotationComponent.class);
			final AngularVelocityComponent avc = gameEntityEngine.createComponent(AngularVelocityComponent.class);
			avc.setup(angularSpeed);
			e.add(rc).add(avc);
		}

	}

	protected ScaleComponent getScaleComponent(Entity e) {
		ScaleComponent sc = Mapper.scaleComponent.get(e);
		if (sc == null) {
			sc = gameEntityEngine.createComponent(ScaleComponent.class);
			e.add(sc);
		}
		return sc;
	}

	protected abstract IBehaving getBehaviour();

	protected float getInitialPositionX() {
		return initialPositionX;
	}

	protected float getInitialPositionY() {
		return initialPositionY;
	}

	protected float getInitialVelocityX() {
		return initialVelocityX;
	}

	protected float getInitialVelocityY() {
		return initialVelocityY;
	}

	/**
	 * Sets the initial position used for making bullets.
	 * 
	 * @param x
	 *            x-position.
	 * @param y
	 *            y-position.
	 */
	public void position(float x, float y) {
		initialPositionX = x;
		initialPositionY = y;
	}

	public void position(Vector2 v) {
		initialPositionX = v.x;
		initialPositionY = v.y;
	}

	public void position(Entity e) {
		PositionComponent pc = Mapper.positionComponent.get(e);
		if (pc != null)
			position(pc);
	}

	public void position(PositionComponent pc) {
		initialPositionX = pc.x;
		initialPositionY = pc.y;
	}

	/**
	 * @param scaleXY
	 *            Sets the scaling in x and y direction.
	 */
	public void scale(float scaleXY) {
		this.initialScaleX = this.initialScaleY = scaleXY;
	}

	/**
	 * @param scaleX
	 *            Sets the scaling in x direction.
	 * @param scaleY
	 *            Sets the scaling in y direction.
	 */
	public void scale(float scaleX, float scaleY) {
		this.initialScaleX = scaleX;
		this.initialScaleY = scaleY;
	}

	/**
	 * @param life
	 *            The time in seconds the bullet will live until it fades away.
	 */
	public void life(float lifeTime) {
		this.lifeTime = lifeTime;
	}

	public void angularSpeed(float angularSpeed) {
		this.angularSpeed = angularSpeed;
	}

	/**
	 * Sets the initial velocity used for making bullets.
	 * 
	 * @param vx
	 *            Velocity in x-direction.
	 * @param vy
	 *            Velocity in y-direction.
	 */
	public void velocity(float vx, float vy) {
		initialVelocityX = vx;
		initialVelocityY = vy;
	}

	public void velocity(Vector2 v) {
		initialVelocityX = v.x;
		initialVelocityY = v.y;

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
