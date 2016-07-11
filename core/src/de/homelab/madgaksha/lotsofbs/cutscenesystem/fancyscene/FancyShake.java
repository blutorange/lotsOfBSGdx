package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.enums.RichterScale;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyShake extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyShake.class);

	private final static float MIN_ADVANCE_ANGLE = 45.0f;
	private final static float MAX_ADVANCE_ANGLE = 315.0f;
	private final static float MIN_AMPLITUDE_RATIO = 0.5f;

	private RichterScale richterScale = RichterScale.M1;
	private EventFancyScene scene = null;
	private float duration = 0.0f;
	private float currentAngle = 0.0f;
	private boolean isDone = false;

	public FancyShake(RichterScale richterScale, float duration) {
		super(true);
		this.richterScale = richterScale;
		this.duration = duration;
	}

	@Override
	public void reset() {
		richterScale = RichterScale.M1;
		duration = 0.0f;
		currentAngle = 0.0f;
		isDone = false;
		scene = null;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		isDone = false;
		return true;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float passedTime) {
		currentAngle = (currentAngle + MathUtils.random(MIN_ADVANCE_ANGLE, MAX_ADVANCE_ANGLE)) % 360.0f;
		float amplitude = MathUtils.random(MIN_AMPLITUDE_RATIO * richterScale.amplitude, richterScale.amplitude);
		amplitude *= GlobalBag.viewportGame.getScreenWidth();
		amplitude *= 0.01f;
		float dx = amplitude * MathUtils.sinDeg(currentAngle);
		float dy = amplitude * MathUtils.cosDeg(currentAngle);
		scene.shakeScreenBy(dx, dy);
		isDone = passedTime >= duration;
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
		scene.shakeScreenBy(0.0f, 0.0f);
	}

	@Override
	public void attachedToScene(EventFancyScene scene) {
		this.scene = scene;
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		RichterScale richterScale = FileCutsceneProvider.nextRichterScale(s);
		if (richterScale == null) {
			LOG.error("expected richter scale");
			return null;
		}

		Float duration = FileCutsceneProvider.nextNumber(s);
		if (duration == null) {
			LOG.error("expected duration");
			return null;
		}

		return new FancyShake(richterScale, duration);
	}
}
