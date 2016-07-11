package de.homelab.madgaksha.lotsofbs.entityengine.entity.trajectory;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.audiosystem.VoicePlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AlphaComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ForceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletTrajectoryMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IBehaving;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;

/**
 * First fade in, then wait, then do a linear motion in the specified direction.
 */
public class FadeInAimTrajectory extends BulletTrajectoryMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FadeInAimTrajectory.class);
	private final static Vector2 v = new Vector2();

	private float velocityAim = 0.0f;
	private float timeFadeIn = 0.0f;
	private float timeWaitAfterFadeIn = 0.0f;
	private float waveAmplitude = 25.0f;
	private float waveFrequency = 360.0f * 5.0f;
	private Entity aim;
	private ESound soundOnFire = null;

	public FadeInAimTrajectory() {
		super();
	}

	public void aim(Entity aim) {
		this.aim = aim;
	}

	public void timeFadeIn(float f) {
		this.timeFadeIn = f;
	}

	public void timeWaitAfterFadeIn(float f) {
		this.timeWaitAfterFadeIn = f;
	}

	public void velocityAim(float f) {
		this.velocityAim = f;
	}

	public void soundOnFire(ESound s) {
		this.soundOnFire = s;
	}

	/**
	 * @param f
	 *            Frequency in Hz.
	 */
	public void waveFrequency(float f) {
		this.waveFrequency = 360.0f * f;
	}

	public void waveAmplitude(float f) {
		this.waveAmplitude = f;
	}

	@Override
	protected void setup(Entity e) {
		super.setup(e);
		AlphaComponent ac = gameEntityEngine.createComponent(AlphaComponent.class);
		ForceComponent fc = gameEntityEngine.createComponent(ForceComponent.class);
		ac.alpha = 0.0f;
		e.add(ac).add(fc);
	}

	@Override
	protected IBehaving getBehaviour() {
		return aim != null ? new BulletBehaviour(aim, timeFadeIn, timeWaitAfterFadeIn, velocityAim, waveFrequency,
				waveAmplitude, soundOnFire, voicePlayer) : null;
	}

	private final static class BulletBehaviour implements IBehaving {
		private int mode = 0;
		private final float timeFadeIn;
		private final float timeWaitAfterFadeIn;
		private final float velocity;
		private float baseX, baseY;
		private final Entity aim;
		private final ESound soundOnFire;
		private final VoicePlayer voicePlayer;
		private final float waveFrequency;
		private final float waveAmplitude;

		public BulletBehaviour(Entity aim, float timeFadeIn, float timeWaitAfterFadeIn, float velocity,
				float waveFrequency, float waveAmplitude, ESound soundOnFire, VoicePlayer vp) {
			this.aim = aim;
			this.timeFadeIn = timeFadeIn;
			this.timeWaitAfterFadeIn = timeWaitAfterFadeIn;
			this.velocity = velocity;
			this.soundOnFire = soundOnFire;
			this.voicePlayer = vp;
			this.waveAmplitude = waveAmplitude;
			this.waveFrequency = waveFrequency;
		}

		@Override
		public boolean behave(Entity bullet) {
			TemporalComponent tc = Mapper.temporalComponent.get(bullet);
			switch (mode) {
			case 0:
				// Fade-in
				AlphaComponent ac = Mapper.alphaComponent.get(bullet);
				if (tc.totalTime >= timeFadeIn) {
					ac.alpha = 1.0f;
					mode = 1;
					tc.totalTime = 0.0f;
				} else
					ac.alpha = tc.totalTime / timeFadeIn;
				break;
			case 1:
				// Wait until bullet aims and goes off
				if (tc.totalTime >= timeWaitAfterFadeIn) {
					mode = 2;
					tc.totalTime = 0.0f;
				}
				break;
			case 2:
				if (aim != null) {
					if (voicePlayer != null)
						voicePlayer.play(soundOnFire);
					PositionComponent pcTarget = Mapper.positionComponent.get(aim);
					PositionComponent pcBullet = Mapper.positionComponent.get(bullet);
					VelocityComponent vc = Mapper.velocityComponent.get(bullet);
					v.set(pcTarget.x - pcBullet.x, pcTarget.y - pcBullet.y).nor();
					vc.x = velocity * v.x;
					vc.y = velocity * v.y;
					baseX = v.y;
					baseY = -v.x;
				}
				mode = 3;
				break;
			case 3:
				PositionComponent pc = Mapper.positionComponent.get(bullet);
				float s = MathUtils.sinDeg(tc.totalTime * waveFrequency);
				pc.offsetX = baseX * s * waveAmplitude;
				pc.offsetY = baseY * s * waveAmplitude;
			}
			return true;
		}
	}

}