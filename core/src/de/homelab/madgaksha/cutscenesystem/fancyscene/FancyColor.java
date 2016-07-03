package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

public class FancyColor extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyColor.class);

	private String key = StringUtils.EMPTY;
	private Color color = new Color(Color.WHITE);

	public FancyColor(String key, Color color) {
		super(true);
		this.key = key;
		this.color.set(color);
	}

	@Override
	public void reset() {
		color.set(Color.WHITE);
		key = StringUtils.EMPTY;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		efs.getDrawable(key).setColor(color);
		return false;
	}

	@Override
	public void render() {
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

	@Override
	public void attachedToScene(EventFancyScene scene) {
		scene.requestDrawable(key);
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
