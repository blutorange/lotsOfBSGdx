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
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancyShow extends AFancyWithDrawable {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(FancyShow.class);

	private float duration;
	@Transient private float lastTime = 0.0f;
	@Transient private boolean isDone = false;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeFloat(duration);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final float duration = in.readFloat();
		if (duration < 0f) throw new InvalidDataException("duration must be >= 0");
		this.duration = duration;
	}
	
	public FancyShow(String key, float duration) {
		super(key);
		this.duration = duration;
		this.lastTime = 0.0f;
		this.isDone = false;
	}

	@Override
	public void resetSubclass() {
		duration = 0.0f;
		lastTime = 0.0f;
		isDone = false;
	}

	@Override
	public boolean beginSubclass(EventFancyScene scene) {
		lastTime = 0.0f;
		isDone = false;
		return true;
	}

	@Override
	public void render(Batch batch) {
		drawable.render(batch);
	}

	@Override
	public void update(float currentTime) {
		if (drawable.update(currentTime - lastTime, currentTime) || currentTime >= duration)
			isDone = true;
		lastTime = currentTime;
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
	}

	@Override
	public void resize(int w, int h) {
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		String key = s.next();
		Float duration = FileCutsceneProvider.nextNumber(s);
		if (duration == null) {
			LOG.error("expected duration");
			return null;
		}
		return new FancyShow(key, duration);
	}
}
