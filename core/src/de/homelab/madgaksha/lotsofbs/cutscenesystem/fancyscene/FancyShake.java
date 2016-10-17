package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.enums.RichterScale;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancyShake extends AFancyEvent {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyShake.class);
	private final static float MIN_DURATION = 0.01f;

	private final static float MIN_ADVANCE_ANGLE = 45.0f;
	private final static float MAX_ADVANCE_ANGLE = 315.0f;
	private final static float MIN_AMPLITUDE_RATIO = 0.5f;

	private RichterScale richterScale = RichterScale.M1;
	private float duration = 0.0f;

	@Transient private float currentAngle = 0.0f;
	@Transient private EventFancyScene scene = null;
	@Transient private boolean isDone = false;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(richterScale);
		out.writeFloat(duration);
	}

	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final Object richterScale = in.readObject();
		if (richterScale == null || !(richterScale instanceof RichterScale)) throw new InvalidDataException("unknown richter scale");
		this.richterScale = (RichterScale) richterScale;

		final float duration = in.readFloat();
		if (duration < MIN_DURATION)
			throw new InvalidDataException("duration must be >= " + MIN_DURATION);
		this.duration = duration;
		currentAngle = 0.0f;
		scene = null;
		isDone = false;
	}

	public FancyShake(final RichterScale richterScale, final float duration) {
		this(0f, 0, richterScale, duration);
	}

	public FancyShake(final float startTime, final int z, final RichterScale richterScale, final float duration) {
		super(startTime, z, true);
		if (duration < MIN_DURATION)
			throw new IllegalArgumentException("duration must be >= " + MIN_DURATION);
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
	public boolean begin(final EventFancyScene efs) {
		currentAngle = 0.0f;
		isDone = false;
		return true;
	}

	@Override
	public void render(final Batch batch) {
	}

	@Override
	public void drawableChanged(final EventFancyScene scene, final String changedKey) {
	}

	@Override
	public void update(final float passedTime) {
		currentAngle = (currentAngle + MathUtils.random(MIN_ADVANCE_ANGLE, MAX_ADVANCE_ANGLE)) % 360.0f;
		final float amplitude = 0.015f * MathUtils.random(MIN_AMPLITUDE_RATIO * richterScale.amplitude, richterScale.amplitude);
		final float dx = amplitude * MathUtils.sinDeg(currentAngle);
		final float dy = amplitude * MathUtils.cosDeg(currentAngle);
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
	public void attachedToScene(final EventFancyScene scene) {
		this.scene = scene;
	}

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
	public static AFancyEvent readNextObject(final Scanner s, final FileHandle parentFile) {
		final RichterScale richterScale = FileCutsceneProvider.nextRichterScale(s);
		if (richterScale == null) {
			LOG.error("expected richter scale");
			return null;
		}

		final Float duration = FileCutsceneProvider.nextNumber(s);
		if (duration == null) {
			LOG.error("expected duration");
			return null;
		}

		return new FancyShake(richterScale, duration);
	}
}