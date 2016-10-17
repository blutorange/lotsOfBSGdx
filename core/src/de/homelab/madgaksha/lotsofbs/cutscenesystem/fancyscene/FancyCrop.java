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

public class FancyCrop extends AFancyWithDrawable {
	/** Initial version */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyCrop.class);

	private Vector2 cropX = new Vector2(1.0f, 1.0f);
	private Vector2 cropY = new Vector2(1.0f, 1.0f);

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(cropX.x);
		out.writeFloat(cropX.y);
		out.writeFloat(cropY.x);
		out.writeFloat(cropY.y);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		cropX = new Vector2(1.0f, 1.0f);
		cropY = new Vector2(1.0f, 1.0f);

		final float xx = in.readFloat();
		final float xy = in.readFloat();
		final float yx = in.readFloat();
		final float yy = in.readFloat();
		cropX.set(xx,xy);
		cropY.set(yx,yy);
	}

	public FancyCrop(final String key, final float cropLeft, final float cropBottom, final float cropRight, final float cropTop) {
		this(0, 0, key, cropLeft, cropBottom, cropRight, cropTop);
	}

	public FancyCrop(final float startTime, final int z, final String key, final float cropLeft, final float cropBottom, final float cropRight, final float cropTop) {
		super(startTime, z, key);
		cropX.set(cropLeft, cropRight);
		cropY.set(cropBottom, cropTop);
	}

	@Override
	public void resetSubclass() {
		cropX.set(1.0f, 1.0f);
		cropY.set(1.0f, 1.0f);
	}

	@Override
	public boolean beginSubclass(final EventFancyScene efs) {
		drawable.setCrop(cropX, cropY);
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

		final Float cropLeft = FileCutsceneProvider.nextNumber(s);
		if (cropLeft == null) {
			LOG.error("expected crop left");
			return null;
		}

		final Float cropBottom = FileCutsceneProvider.nextNumber(s);
		if (cropBottom == null) {
			LOG.error("expected crop bottom");
			return null;
		}

		Float cropRight = FileCutsceneProvider.nextNumber(s);
		if (cropRight == null)
			cropRight = cropLeft;

		Float cropTop = FileCutsceneProvider.nextNumber(s);
		if (cropTop == null)
			cropTop = cropBottom;

		return new FancyCrop(key, cropLeft, cropBottom, cropRight, cropTop);
	}

}
