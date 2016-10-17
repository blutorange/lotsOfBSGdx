package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyOrigin extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyOrigin.class);

	private Vector2 origin = new Vector2();

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(origin.x);
		out.writeFloat(origin.y);
	}

	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		origin = new Vector2();

		final float x = in.readFloat();
		final float y = in.readFloat();
		origin.set(x, y);
	}

	public FancyOrigin(final String key, final float x, final float y) {
		this(0, 0 , key, x, y);
	}

	public FancyOrigin(final float startTime, final int z, final String key, final float x, final float y) {
		super(startTime, z, key);
		origin.set(x, y);
	}

	@Override
	public void resetSubclass() {
		origin.set(0.0f, 0.0f);
	}

	@Override
	public boolean beginSubclass(final EventFancyScene efs) {
		drawable.setOrigin(origin);
		return false;
	}

	@Override
	public void render(final Batch batch) {
	}

	@Override
	public void update(final float passedTime) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end() {
	}

	/**
	 * @param s
	 *            Scanner from which to read.
	 * @param parentFile
	 *            The file handle of the file being used. Should be used only
	 *            for directories.
	 */
	public static AFancyEvent readNextObject(final Scanner s, final FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		final String key = s.next();
		final Float x = FileCutsceneProvider.nextNumber(s);
		final Float y = FileCutsceneProvider.nextNumber(s);
		if (x == null || y == null) {
			LOG.error("expected position");
			return null;
		}
		return new FancyOrigin(key, x, y);
	}

}
