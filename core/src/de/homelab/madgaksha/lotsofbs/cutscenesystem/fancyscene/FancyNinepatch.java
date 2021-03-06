package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ENinePatch;

public class FancyNinepatch extends AFancyEvent {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyNinepatch.class);

	private String key = StringUtils.EMPTY;
	private float unitPerPixel = 1.0f;
	private ENinePatch ninePatch;

	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.writeUTF(key);
		out.writeObject(ninePatch);
		out.writeFloat(unitPerPixel);
	}
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		final String key = in.readUTF();
		if (key == null || key.isEmpty()) throw new InvalidDataException("key cannot be null or empty");

		final Object ninePatch = in.readObject();
		if (ninePatch == null || !(ninePatch instanceof ENinePatch)) throw new InvalidDataException("unknown nine patch");

		this.key = key;
		this.ninePatch = (ENinePatch) ninePatch;
		unitPerPixel = in.readFloat();
	}

	public FancyNinepatch(final String key, final ENinePatch ninePatch) {
		this(0, 0, key, ninePatch);
	}

	public FancyNinepatch(final float startTime, final int z, final String key, final ENinePatch ninePatch) {
		super(startTime, z, true);
		this.key = key;
		this.ninePatch = ninePatch;
	}

	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		ninePatch = null;
	}

	@Override
	public boolean begin(final EventFancyScene scene) {
		scene.requestDrawableNinePatch(key).setDrawable(ninePatch, unitPerPixel);
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

	@Override
	public void drawableChanged(final EventFancyScene scene, final String changedKey) {
	}

	@Override
	public void attachedToScene(final EventFancyScene scene) {
		scene.requestDrawableNinePatch(key);
	}

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
	public static AFancyEvent readNextObject(final Scanner s, final FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected nine patch name");
			return null;
		}
		final String key = s.next();

		final ENinePatch ninePatch = FileCutsceneProvider.nextNinePatch(s);

		return ninePatch != null ? new FancyNinepatch(key, ninePatch) : null;
	}
}
