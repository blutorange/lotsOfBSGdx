package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancyShow extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyShow.class);

	private float duration;
	@Transient private float lastTime = 0.0f;
	@Transient private boolean isDone = false;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(duration);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final float duration = in.readFloat();
		if (duration < 0f) throw new InvalidDataException("duration must be >= 0");
		this.duration = duration;
	}

	public FancyShow(final String key, final float duration) {
		this(0, 0, key, duration);
	}

	public FancyShow(final float startTime, final int z, final String key, final float duration) {
		super(startTime, z, key);
		this.duration = duration;
		lastTime = 0.0f;
		isDone = false;
	}

	@Override
	public void resetSubclass() {
		duration = 0.0f;
		lastTime = 0.0f;
		isDone = false;
	}

	@Override
	public boolean beginSubclass(final EventFancyScene scene) {
		lastTime = 0.0f;
		isDone = false;
		return true;
	}

	@Override
	public void render(final Batch batch) {
		drawable.render(batch);
	}

	@Override
	public void update(final float currentTime) {
		if (drawable.update(currentTime - lastTime, currentTime) || currentTime >= duration)
			isDone = true;
		lastTime = currentTime;
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
	}

	@Override
	public void resize(final int w, final int h) {
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
		return new FancyShow(key, duration);
	}
}
