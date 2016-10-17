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
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;

public class FancyAnimation extends AFancyEvent {
	/** Initial version */
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(FancyAnimation.class);

	private String key = StringUtils.EMPTY;
	private float dpi;
	private EAnimation animation;

	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.writeUTF(key);
		out.writeObject(animation);
		out.writeFloat(dpi);
	}
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		final String key = in.readUTF();
		if (key == null || key.isEmpty()) throw new InvalidDataException("key cannot be null or empty");

		final Object animation = in.readObject();
		if (animation == null || !(animation instanceof EAnimation)) throw new InvalidDataException("unknown animation");

		this.key = key;
		this.animation = (EAnimation) animation;
		dpi = in.readFloat();
	}

	public FancyAnimation(final String key, final float dpi, final EAnimation animation) {
		this(0, 0, key, dpi, animation);
	}

	public FancyAnimation(final float startTime, final int z, final String key, final float dpi, final EAnimation animation) {
		super(startTime, z, true);
		this.dpi = dpi;
		this.key = key;
		this.animation = animation;
	}

	@Override
	public void reset() {
		dpi = 1.0f;
		key = StringUtils.EMPTY;
		animation = null;
	}

	@Override
	public boolean begin(final EventFancyScene scene) {
		scene.requestDrawableAnimation(key).setDrawable(animation, dpi);
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
		scene.requestDrawableAnimation(key);
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

		final Float dpi = FileCutsceneProvider.nextNumber(s);
		if (dpi == null) {
			LOG.error("expected dpi");
			return null;
		}
		final EAnimation animation = FileCutsceneProvider.nextAnimation(s);
		if (animation == null) {
			LOG.error("expected animation");
			return null;
		}
		return new FancyAnimation(key, dpi, animation);
	}
}
