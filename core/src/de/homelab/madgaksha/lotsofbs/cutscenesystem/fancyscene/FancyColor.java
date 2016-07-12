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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(color.r);
		out.writeFloat(color.g);
		out.writeFloat(color.b);
		out.writeFloat(color.a);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		color = new Color(Color.WHITE);
		
		final float r = in.readFloat();
		final float g = in.readFloat();
		final float b = in.readFloat();
		final float a = in.readFloat();
		if (r < 0f || r > 1f || g < 0f || g > 1f || b < 0f || b > 1f || a < 0f || a > 1f)
			throw new InvalidDataException("color channel values must be between 0 and 1");
		color.set(r, g, b, a);
	}

	public FancyColor(String key, Color color) {
		super(key);
		this.color.set(color);
	}

	@Override
	public void resetSubclass() {
		color.set(Color.WHITE);
	}

	@Override
	public boolean beginSubclass(EventFancyScene scene) {
		drawable.setColor(color);
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

		Color color = FileCutsceneProvider.readNextColor(s);
		if (color == null) {
			LOG.error("expected color");
			return null;
		}

		return new FancyColor(key, color);
	}

}
