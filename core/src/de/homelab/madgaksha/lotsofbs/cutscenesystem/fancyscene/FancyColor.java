package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyColor extends AFancyWithDrawable {
	private final static Logger LOG = Logger.getLogger(FancyColor.class);

	private Color color = new Color(Color.WHITE);

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
