package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyColor extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(FancyColor.class);
	private Color color = new Color(Color.WHITE);

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(color.r);
		out.writeFloat(color.g);
		out.writeFloat(color.b);
		out.writeFloat(color.a);
	}

	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		color = new Color(Color.WHITE);

		final float r = in.readFloat();
		final float g = in.readFloat();
		final float b = in.readFloat();
		final float a = in.readFloat();
		if (r < 0f || r > 1f || g < 0f || g > 1f || b < 0f || b > 1f || a < 0f || a > 1f)
			throw new InvalidDataException("color channel values must be between 0 and 1");
		color.set(r, g, b, a);
	}

	public FancyColor(final String key, final Color color) {
		this(0, 0, key, color);
	}

	public FancyColor(final float startTime, final int z, final String key, final Color color) {
		super(startTime, z, key);
		this.color.set(color);
	}

	@Override
	public void resetSubclass() {
		color.set(Color.WHITE);
	}

	@Override
	public boolean beginSubclass(final EventFancyScene scene) {
		drawable.setColor(color);
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

		final Color color = FileCutsceneProvider.readNextColor(s);
		if (color == null) {
			LOG.error("expected color");
			return null;
		}

		return new FancyColor(key, color);
	}

}
