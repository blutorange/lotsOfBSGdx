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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(path);
		out.writeUTF(interpolationName);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		vector = new Vector2();
		
		Object path = in.readObject();
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
	
	public FancyMove(String key, APath path, String interpolationName) {
		super(key);
		this.path = path;
		this.interpolation = FileCutsceneProvider.interpolationFromName(interpolationName);
		if (this.interpolation == null) {
			this.interpolationName = DEFAULT_INTERPOLATION_NAME;
			this.interpolation = DEFAULT_INTERPOLATION;
		}
	}

	@Override
	public void resetSubclass() {
		path = null;
		isDone = false;
		interpolation = Interpolation.linear;
	}

	@Override
	public boolean beginSubclass(EventFancyScene efs) {
		drawable.getPosition(vector);
		path.setOrigin(vector);
		isDone = false;
		return true;
	}

	@Override
	public void render(Batch batch) {
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
	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		String key = s.next();

		EPath path = FileCutsceneProvider.nextPath(s);
		if (path == null) {
			LOG.error("expected path");
			return null;
		}

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

		if (!s.hasNext()) {
			LOG.error("expected relative/absolute flag");
			return null;
		}
		boolean relativePath = s.next().equalsIgnoreCase("r");

		APath newPath = path.readNextObject(duration, relativePath, 1.0f, 1.0f, s);
		if (newPath == null) {
			LOG.error("expected path data");
			return null;
		}

		return new FancyMove(key, newPath, interpolationName);
	}

}
