package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyPosition extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyPosition.class);

	private Vector2 position = new Vector2();
	private Vector2 delta = new Vector2();

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(position.x);
		out.writeFloat(position.y);
		out.writeFloat(delta.x);
		out.writeFloat(delta.y);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		position = new Vector2();
		delta = new Vector2();
		final float px = in.readFloat();
		final float py = in.readFloat();
		final float dx = in.readFloat();
		final float dy = in.readFloat();
		position.set(px,py);
		delta.set(dx,dy);
	}


	public FancyPosition(final String key, final float x, final float y, final float dx, final float dy) {
		this(0, 0, key, x, y, dx, dy);
	}

	public FancyPosition(final float startTime, final int z, final String key, final float x, final float y, final float dx, final float dy) {
		super(startTime, z, key);
		position.set(x, y);
		delta.set(Math.max(0.0f, dx), Math.max(0.0f, dy));
	}

	@Override
	public void resetSubclass() {
		position.set(0.0f, 0.0f);
	}

	@Override
	public boolean beginSubclass(final EventFancyScene scene) {
		if (delta.x != 0.0f || delta.y != 0.0f) {
			final float dx = MathUtils.random(-delta.x, delta.x);
			final float dy = MathUtils.random(-delta.y, delta.y);
			drawable.setPosition(position.x + dx, position.y + dy);
		} else
			drawable.setPosition(position);
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

		final Float x = FileCutsceneProvider.nextNumber(s);
		final Float y = FileCutsceneProvider.nextNumber(s);

		if (x == null || y == null) {
			LOG.error("expected position");
			return null;
		}

		Float dx = FileCutsceneProvider.nextNumber(s);
		Float dy = FileCutsceneProvider.nextNumber(s);
		if (dx == null)
			dx = 0.0f;
		if (dy == null)
			dy = 0.0f;

		return new FancyPosition(key, x, y, dx, dy);
	}

	@Override
	public String toString() {
		return super.toString() + "@" + position;
	}

}
