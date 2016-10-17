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

public class FancyOpacity extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(FancyOpacity.class);
	private float opacity;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(opacity);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final float opacity = in.readFloat();
		if (opacity < 0f || opacity > 1f) throw new InvalidDataException("opacity must be between 0 and 1");
		this.opacity = opacity;
	}

	public FancyOpacity(final String key, final float opacity) {
		this(0, 0, key, opacity);
	}

	public FancyOpacity(final float startTime, final int z, final String key, final float opacity) {
		super(startTime, z, key);
		this.opacity = opacity;
	}

	@Override
	public void resetSubclass() {
		opacity = 1.0f;
	}

	@Override
	public boolean beginSubclass(final EventFancyScene scene) {
		drawable.setOpacity(opacity);
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
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
	public static AFancyEvent readNextObject(final Scanner s, final FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		final String key = s.next();
		final Float opacity = FileCutsceneProvider.nextNumber(s);
		if (opacity == null) {
			LOG.error("expected opacity");
			return null;
		}
		return new FancyOpacity(key, opacity);
	}

}
