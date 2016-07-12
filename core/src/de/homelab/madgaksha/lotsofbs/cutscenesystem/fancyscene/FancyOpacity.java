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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(opacity);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final float opacity = in.readFloat();
		if (opacity < 0f || opacity > 1f) throw new InvalidDataException("opacity must be between 0 and 1");
		this.opacity = opacity;
	}
	
	public FancyOpacity(String key, float opacity) {
		super(key);
		this.opacity = opacity;
	}

	@Override
	public void resetSubclass() {
		opacity = 1.0f;
	}

	@Override
	public boolean beginSubclass(EventFancyScene scene) {
		drawable.setOpacity(opacity);
		return false;
	}

	@Override
	public void render(Batch batch) {
	}

	@Override
	public void update(float passedTime) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end() {
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		String key = s.next();
		Float opacity = FileCutsceneProvider.nextNumber(s);
		if (opacity == null) {
			LOG.error("expected opacity");
			return null;
		}
		return new FancyOpacity(key, opacity);
	}

}
