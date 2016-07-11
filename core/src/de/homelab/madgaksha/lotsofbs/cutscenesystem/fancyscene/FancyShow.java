package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyShow extends AFancyWithDrawable {
	private final static Logger LOG = Logger.getLogger(FancyShow.class);

	private float duration;
	private float lastTime = 0.0f;
	private boolean isDone = false;

	public FancyShow(String key, float duration) {
		super(key);
		this.duration = duration;
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
