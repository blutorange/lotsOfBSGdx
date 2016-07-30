package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancyFade extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyFade.class);
	private final static float MIN_DURATION = 0.001f;
	private final static Interpolation DEFAULT_INTERPOLATION = Interpolation.linear;
	private final static String DEFAULT_INTERPOLATION_NAME = "linear";
	
	private String interpolationName = DEFAULT_INTERPOLATION_NAME;
	private float duration = 1.0f;
	private float targetOpacity = 1.0f;

	@Transient private float startOpacity = 1.0f;
	@Transient private Interpolation interpolation = DEFAULT_INTERPOLATION;
	@Transient private float durationInverse = 1.0f;
	@Transient private boolean isDone = false;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(duration);
		out.writeUTF(interpolationName);
		out.writeFloat(targetOpacity);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {	
		final float duration = in.readFloat();
		if (duration < MIN_DURATION) throw new InvalidDataException("duration must be >= " + MIN_DURATION);
		this.duration = duration;
		
		final String interpolationName = in.readUTF();
		if (interpolationName == null) throw new InvalidDataException("interpolation name must not be null");
		final Interpolation interpolation = FileCutsceneProvider.interpolationFromName(interpolationName);
		if (interpolation == null) throw new InvalidDataException("no such interpolation");
		this.interpolation = interpolation;
				
		this.interpolationName = interpolationName;
		targetOpacity = in.readFloat();
		
		startOpacity = targetOpacity;
		durationInverse = 1.0f/duration;
		isDone = false;
	}
	
	public FancyFade(String key, float targetOpacity, String interpolationName, float duration) {
		super(key);
		if (duration < MIN_DURATION)
			throw new IllegalArgumentException("duration must be >= " + MIN_DURATION);
		this.interpolationName = interpolationName;
		this.targetOpacity = targetOpacity;
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
		interpolation = Interpolation.linear;
		isDone = false;
		duration = durationInverse = 1.0f;
		startOpacity = targetOpacity = 1.0f;
	}

	@Override
	public boolean beginSubclass(EventFancyScene efs) {
		startOpacity = drawable.getOpacity();
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

		Float targetOpacity = FileCutsceneProvider.nextNumber(s);
		if (targetOpacity == null) {
			LOG.error("expected target opacity");
			return null;
		}

		return new FancyFade(key, targetOpacity, interpolationName, duration);
	}
}