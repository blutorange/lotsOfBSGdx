package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyOrigin extends AFancyWithDrawable {
	private final static Logger LOG = Logger.getLogger(FancyOrigin.class);

	private Vector2 origin = new Vector2();

	public FancyOrigin(String key, float x, float y) {
		super(key);
		this.origin.set(x, y);
	}

	@Override
	public void resetSubclass() {
		origin.set(0.0f, 0.0f);
	}

	@Override
	public boolean beginSubclass(EventFancyScene efs) {
		drawable.setOrigin(origin);
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
		Float x = FileCutsceneProvider.nextNumber(s);
		Float y = FileCutsceneProvider.nextNumber(s);
		if (x == null || y == null) {
			LOG.error("expected position");
			return null;
		}
		return new FancyOrigin(key, x, y);
	}

}
