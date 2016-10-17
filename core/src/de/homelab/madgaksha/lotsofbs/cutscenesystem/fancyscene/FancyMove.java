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
import de.homelab.madgaksha.lotsofbs.path.APath;
import de.homelab.madgaksha.lotsofbs.path.EPath;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancyMove extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyMove.class);
	private final static Interpolation DEFAULT_INTERPOLATION = Interpolation.linear;
	private final static String DEFAULT_INTERPOLATION_NAME = "linear";

	private APath path;
	private String interpolationName = DEFAULT_INTERPOLATION_NAME;

	@Transient private Vector2 vector = new Vector2();
	@Transient private Interpolation interpolation = DEFAULT_INTERPOLATION;
	@Transient private boolean isDone = false;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(path);
		out.writeUTF(interpolationName);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		vector = new Vector2();

		final Object path = in.readObject();
		if (path == null || !(path instanceof APath)) throw new InvalidDataException("unknown path");
		this.path = (APath) path;

		final String interpolationName = in.readUTF();
		if (interpolationName == null) throw new InvalidDataException("interpolation name must not be null");
		final Interpolation interpolation = FileCutsceneProvider.interpolationFromName(interpolationName);
		if (interpolation == null) throw new InvalidDataException("no such interpolation");
		this.interpolation = interpolation;

		vector = new Vector2();
		isDone = false;
	}

	public FancyMove(final String key, final APath path, final String interpolationName) {
		this(0, 0, key, path, interpolationName);
	}

	public FancyMove(final float startTime, final int z, final String key, final APath path, final String interpolationName) {
		super(startTime, z, key);
		this.path = path;
		interpolation = FileCutsceneProvider.interpolationFromName(interpolationName);
		if (interpolation == null) {
			this.interpolationName = DEFAULT_INTERPOLATION_NAME;
			interpolation = DEFAULT_INTERPOLATION;
		}
	}

	@Override
	public void resetSubclass() {
		path = null;
		isDone = false;
		interpolation = Interpolation.linear;
	}

	@Override
	public boolean beginSubclass(final EventFancyScene efs) {
		drawable.getPosition(vector);
		path.setOrigin(vector);
		isDone = false;
		return true;
	}

	@Override
	public void render(final Batch batch) {
	}

	@Override
	public void update(float passedTime) {
		if (passedTime >= path.getTMax()) {
			passedTime = path.getTMax();
			isDone = true;
		}
		path.applyWithInterpolation(passedTime, vector, interpolation);
		drawable.setPosition(vector);
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
		// Make sure the sprite ends up at the correct position.
		update(path.getTMax());
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

		final EPath path = FileCutsceneProvider.nextPath(s);
		if (path == null) {
			LOG.error("expected path");
			return null;
		}

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

		if (!s.hasNext()) {
			LOG.error("expected relative/absolute flag");
			return null;
		}
		final boolean relativePath = s.next().equalsIgnoreCase("r");

		final APath newPath = path.readNextObject(duration, relativePath, 1.0f, 1.0f, s);
		if (newPath == null) {
			LOG.error("expected path data");
			return null;
		}

		return new FancyMove(key, newPath, interpolationName);
	}

}
