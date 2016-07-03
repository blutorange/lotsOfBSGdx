package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

public class FancyZoom extends DrawableFancy {
	private final static Logger LOG = Logger.getLogger(FancyZoom.class);

	private float duration = 1.0f;
	private float durationInverse = 1.0f;
	private Vector2 originalScale = new Vector2(1f, 1f);
	private Vector2 targetScale = new Vector2(1f, 1f);
	private Interpolation interpolation = Interpolation.linear;
	private boolean isDone = false;

	public FancyZoom(String key, float targetScaleX, float targetScaleY, Interpolation interpolation, float duration) {
		super(key);
		this.targetScale.set(targetScaleX, targetScaleY);
		this.interpolation = interpolation;
		this.duration = duration;
		this.durationInverse = 1.0f / duration;
	}

	@Override
	public void resetSubclass() {
		targetScale.set(1f, 1f);
		duration = durationInverse = 1.0f;
		isDone = false;
		interpolation = Interpolation.linear;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		drawable.getScale.as(originalScale);
		isDone = false;
		return true;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float passedTime) {
		if (passedTime >= duration) {
			passedTime = duration;
			isDone = true;
		}
		float alpha = interpolation.apply(passedTime * durationInverse);
		drawable.setScaleLerp(originalScale, targetScale, alpha);
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
		// Make sure zoom is set to its final value.
		update(duration);
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		String key = s.next();

		Float duration = FileCutsceneProvider.nextNumber(s);
		if (duration == null) {
			LOG.error("expected duration");
			return null;
		}
		if (duration < 0.001f) {
			LOG.error("duration must be greater than 0");
			return null;
		}

		Interpolation interpolation = FileCutsceneProvider.readNextInterpolation(s);
		if (interpolation == null)
			interpolation = Interpolation.linear;

		Float targetScaleX = FileCutsceneProvider.nextNumber(s);
		if (targetScaleX == null) {
			LOG.error("expected target scale");
			return null;
		}

		Float targetScaleY = FileCutsceneProvider.nextNumber(s);
		if (targetScaleY == null)
			targetScaleY = targetScaleX;

		return new FancyZoom(key, targetScaleX, targetScaleY, interpolation, duration);
	}
}
