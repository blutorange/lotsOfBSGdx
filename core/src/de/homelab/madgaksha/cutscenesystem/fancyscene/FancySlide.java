package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

public class FancySlide extends DrawableFancy {
	private final static Logger LOG = Logger.getLogger(FancySlide.class);

	private Interpolation interpolation = Interpolation.linear;
	private float duration = 1.0f;
	private float durationInverse = 1.0f;
	private boolean isDone = false;
	private Vector2 startCropX = new Vector2(1.0f,1.0f);
	private Vector2 startCropY = new Vector2(1.0f,1.0f);
	private Vector2 targetCropX = new Vector2(1.0f,1.0f);
	private Vector2 targetCropY = new Vector2(1.0f,1.0f);
	
	public FancySlide(String key, Float duration, Interpolation interpolation, float targetCropLeft, float targetCropBottom, float targetCropRight, float targetCropTop) { 
		super(key);
		if (duration < 0.01f) throw new IllegalArgumentException("duration must be greate than 0");
		this.duration = duration;
		this.durationInverse = 1.0f/duration;
		this.interpolation = interpolation;
		this.targetCropX.set(targetCropLeft, targetCropRight);
		this.targetCropY.set(targetCropBottom, targetCropTop);
	}
	

	@Override
	public void resetSubclass() {
		interpolation = Interpolation.linear;
		isDone = false;
		duration = durationInverse = 1.0f;
		startCropX.set(1.0f,1.0f);
		startCropY.set(1.0f,1.0f);
		targetCropX.set(1.0f,1.0f);
		targetCropY.set(1.0f,1.0f);
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		drawable.getCropX.as(startCropX);
		drawable.getCropY.as(startCropY);
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
		drawable.setCropXLerp(startCropX, targetCropX, alpha);
		drawable.setCropYLerp(startCropY, targetCropY, alpha);
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
		// Make sure crop is set to its final value.
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
		if (interpolation == null) interpolation = Interpolation.linear;
		
		Float targetCropLeft = FileCutsceneProvider.nextNumber(s);
		if (targetCropLeft == null) {
			LOG.error("expected crop left");
		}
		
		Float targetCropBottom = FileCutsceneProvider.nextNumber(s);
		if (targetCropBottom == null) {
			LOG.error("expected crop bottom");
		}
		
		Float targetCropRight = FileCutsceneProvider.nextNumber(s);
		if (targetCropRight == null) targetCropRight = targetCropLeft;
		
		Float targetCropTop = FileCutsceneProvider.nextNumber(s);
		if (targetCropTop == null) targetCropTop = targetCropBottom;
		
		return new FancySlide(key, duration, interpolation, targetCropLeft, targetCropBottom, targetCropRight, targetCropTop);
	}
}