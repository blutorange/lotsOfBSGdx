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

public class FancyZoom extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(FancyZoom.class);
	private final static float MIN_DURATION = 0.01f;
	private final static Interpolation DEFAULT_INTERPOLATION = Interpolation.linear;
	private final static String DEFAULT_INTERPOLATION_NAME = "linear";

	private float duration = 1.0f;
	private String interpolationName = DEFAULT_INTERPOLATION_NAME;
	private Vector2 targetScale = new Vector2(1f, 1f);

	@Transient private Vector2 startScale = new Vector2(1f, 1f);
	@Transient private Interpolation interpolation = DEFAULT_INTERPOLATION;
	@Transient private float durationInverse = 1.0f;
	@Transient private boolean isDone = false;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(duration);
		out.writeUTF(interpolationName);
		out.writeFloat(targetScale.x);
		out.writeFloat(targetScale.y);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		startScale = new Vector2(1f, 1f);
		targetScale = new Vector2(1f, 1f);
				
		final float duration = in.readFloat();
		if (duration < MIN_DURATION) throw new InvalidDataException("duration must be >= " + MIN_DURATION);
		this.duration = duration;
		
		final String interpolationName = in.readUTF();
		if (interpolationName == null) throw new InvalidDataException("interpolation name must not be null");
		final Interpolation interpolation = FileCutsceneProvider.interpolationFromName(interpolationName);
		if (interpolation == null) throw new InvalidDataException("no such interpolation");
		this.interpolation = interpolation;
		
		final float sx = in.readFloat();
		final float sy = in.readFloat();
		if (sx < 0f || sy < 0f) throw new InvalidDataException("scale must be >= 0");
		this.targetScale.set(sx, sy);
		
		durationInverse = 1.0f/duration;
		isDone = false;
	}

	
	public FancyZoom(String key, float targetScaleX, float targetScaleY, String interpolationName, float duration) {
		super(key);
		if (duration < MIN_DURATION)
			throw new IllegalArgumentException("duration must be >= " + MIN_DURATION);
		this.interpolationName = interpolationName;
		this.targetScale.set(targetScaleX, targetScaleY);
		this.duration = duration;
		this.durationInverse = 1.0f / duration;
		this.interpolation = FileCutsceneProvider.interpolationFromName(interpolationName);
		if (this.interpolation == null) {
			this.interpolationName = DEFAULT_INTERPOLATION_NAME;
			this.interpolation = DEFAULT_INTERPOLATION;
		}
	}

	@Override
	public void resetSubclass() {
		targetScale.set(1f, 1f);
		duration = durationInverse = 1.0f;
		isDone = false;
		interpolation = Interpolation.linear;
	}

	@Override
	public boolean beginSubclass(EventFancyScene scene) {
		drawable.getScale(startScale);
		isDone = false;
		return true;
	}

	@Override
	public void render(Batch batch) {
	}

	@Override
	public void update(float passedTime) {
		if (passedTime >= duration) {
			passedTime = duration;
			isDone = true;
		}
		float alpha = interpolation.apply(passedTime * durationInverse);
		drawable.setScaleLerp(startScale, targetScale, alpha);
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

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
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

		String interpolationName = FileCutsceneProvider.readNextInterpolationName(s);
		if (interpolationName == null)
			interpolationName = DEFAULT_INTERPOLATION_NAME;

		Float targetScaleX = FileCutsceneProvider.nextNumber(s);
		if (targetScaleX == null) {
			LOG.error("expected target scale");
			return null;
		}

		Float targetScaleY = FileCutsceneProvider.nextNumber(s);
		if (targetScaleY == null)
			targetScaleY = targetScaleX;

		return new FancyZoom(key, targetScaleX, targetScaleY, interpolationName, duration);
	}
}
