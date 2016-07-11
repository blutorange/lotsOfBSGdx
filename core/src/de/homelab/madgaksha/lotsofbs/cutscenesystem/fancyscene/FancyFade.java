package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyFade extends AFancyWithDrawable {
	private final static Logger LOG = Logger.getLogger(FancyFade.class);

	private Interpolation interpolation = Interpolation.linear;
	private float duration = 1.0f;
	private float durationInverse = 1.0f;
	private boolean isDone = false;
	private float startOpacity = 1.0f;
	private float targetOpacity = 1.0f;

	public FancyFade(String key, float targetOpacity, Interpolation interpolation, float duration) {
		super(key);
		if (duration < 0.01f)
			throw new IllegalArgumentException("duration must be greate than 0");
		this.targetOpacity = targetOpacity;
		this.duration = duration;
		this.durationInverse = 1.0f / duration;
		this.interpolation = interpolation;
	}

	@Override
	public void resetSubclass() {
		interpolation = Interpolation.linear;
		isDone = false;
		duration = durationInverse = 1.0f;
		startOpacity = targetOpacity = 1.0f;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		startOpacity = drawable.getOpacity();
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
		drawable.setOpacity(interpolation.apply(startOpacity, targetOpacity, passedTime * durationInverse));
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
		// Make sure the opacity is set to its final value.
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

		Float targetOpacity = FileCutsceneProvider.nextNumber(s);
		if (targetOpacity == null) {
			LOG.error("expected target opacity");
		}

		return new FancyFade(key, targetOpacity, interpolation, duration);
	}
}