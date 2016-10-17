package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancySlide extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancySlide.class);
	private final static float MIN_DURATION = 0.01f;
	private final static Interpolation DEFAULT_INTERPOLATION = Interpolation.linear;
	private final static String DEFAULT_INTERPOLATION_NAME = "linear";

	private float duration = 1.0f;
	private String interpolationName = DEFAULT_INTERPOLATION_NAME;
	private Vector2 targetCropX = new Vector2(1.0f, 1.0f);
	private Vector2 targetCropY = new Vector2(1.0f, 1.0f);

	@Transient private Vector2 startCropX = new Vector2(1.0f, 1.0f);
	@Transient private Vector2 startCropY = new Vector2(1.0f, 1.0f);
	@Transient private float durationInverse = 1.0f;
	@Transient private Interpolation interpolation = DEFAULT_INTERPOLATION;
	@Transient private boolean isDone = false;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(duration);
		out.writeUTF(interpolationName);
		out.writeFloat(targetCropX.x);
		out.writeFloat(targetCropX.y);
		out.writeFloat(targetCropY.x);
		out.writeFloat(targetCropY.y);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		targetCropX = new Vector2(1.0f, 1.0f);
		targetCropY = new Vector2(1.0f, 1.0f);
		startCropX = new Vector2(1.0f, 1.0f);
		startCropY = new Vector2(1.0f, 1.0f);

		final float duration = in.readFloat();
		if (duration < MIN_DURATION) throw new InvalidDataException("duration must be >= " + MIN_DURATION);
		this.duration = duration;

		final String interpolationName = in.readUTF();
		if (interpolationName == null) throw new InvalidDataException("interpolation name must not be null");
		final Interpolation interpolation = FileCutsceneProvider.interpolationFromName(interpolationName);
		if (interpolation == null) throw new InvalidDataException("no such interpolation");
		this.interpolation = interpolation;

		final float cxx = in.readFloat();
		final float cxy = in.readFloat();
		final float cyx = in.readFloat();
		final float cyy = in.readFloat();

		if (cxx < 0f || cxx > 1f || cxy < 0f || cxy > 1f || cyx < 0f || cyx > 1f || cyy < 0f || cyy > 1f)
			throw new InvalidDataException("crop must be between 0 and 1");
		targetCropX.set(cxx,cxy);
		targetCropY.set(cyx,cyy);

		startCropX.set(targetCropX);
		startCropY.set(targetCropY);
		durationInverse = 1.0f / duration;
		isDone = false;
	}

	public FancySlide(final String key, final Float duration, final String interpolationName, final float targetCropLeft,
			final float targetCropBottom, final float targetCropRight, final float targetCropTop) {
		this(0, 0, key, duration, interpolationName, targetCropLeft, targetCropBottom, targetCropRight, targetCropTop);
	}

	public FancySlide(final float startTime, final int z, final String key, final Float duration, final String interpolationName, final float targetCropLeft,
			final float targetCropBottom, final float targetCropRight, final float targetCropTop) {
		super(startTime, z, key);
		if (duration < MIN_DURATION)
			throw new IllegalArgumentException("duration must be >= " + MIN_DURATION);
		this.duration = duration;
		durationInverse = 1.0f / duration;
		this.interpolationName = interpolationName;
		targetCropX.set(targetCropLeft, targetCropRight);
		targetCropY.set(targetCropBottom, targetCropTop);
		interpolation = FileCutsceneProvider.interpolationFromName(interpolationName);
		if (interpolation == null) {
			this.interpolationName = DEFAULT_INTERPOLATION_NAME;
			interpolation = DEFAULT_INTERPOLATION;
		}
	}

	@Override
	public void resetSubclass() {
		interpolation = Interpolation.linear;
		isDone = false;
		duration = durationInverse = 1.0f;
		startCropX.set(1.0f, 1.0f);
		startCropY.set(1.0f, 1.0f);
		targetCropX.set(1.0f, 1.0f);
		targetCropY.set(1.0f, 1.0f);
	}

	@Override
	public boolean beginSubclass(final EventFancyScene scene) {
		drawable.getCropX(startCropX);
		drawable.getCropY(startCropY);
		isDone = false;
		return true;
	}

	@Override
	public void render(final Batch batch) {
	}

	@Override
	public void update(float passedTime) {
		if (passedTime >= duration) {
			passedTime = duration;
			isDone = true;
		}
		final float alpha = interpolation.apply(passedTime * durationInverse);
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

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
	public static AFancyEvent readNextObject(final Scanner s, final FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		final String key = s.next();

		final Float duration = FileCutsceneProvider.nextNumber(s);
		if (duration == null) {
			LOG.error("expected duration");
			return null;
		}
		if (duration < 0.001f) {
			LOG.error("duration must be greater than 0");
			return null;
		}

		String interpolationName = FileCutsceneProvider.readNextInterpolationName(s);
		if (interpolationName == null)
			interpolationName = DEFAULT_INTERPOLATION_NAME;

		final Float targetCropLeft = FileCutsceneProvider.nextNumber(s);
		if (targetCropLeft == null) {
			LOG.error("expected crop left");
			return null;
		}

		final Float targetCropBottom = FileCutsceneProvider.nextNumber(s);
		if (targetCropBottom == null) {
			LOG.error("expected crop bottom");
			return null;
		}

		Float targetCropRight = FileCutsceneProvider.nextNumber(s);
		if (targetCropRight == null)
			targetCropRight = targetCropLeft;

		Float targetCropTop = FileCutsceneProvider.nextNumber(s);
		if (targetCropTop == null)
			targetCropTop = targetCropBottom;

		return new FancySlide(key, duration, interpolationName, targetCropLeft, targetCropBottom, targetCropRight,
				targetCropTop);
	}
}