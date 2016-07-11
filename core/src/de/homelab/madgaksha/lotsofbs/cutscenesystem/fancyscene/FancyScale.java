package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyScale extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyScale.class);

	private String key = StringUtils.EMPTY;
	private Vector2 scale = new Vector2();

	public FancyScale(String key, float x, float y) {
		super(true);
		this.key = key;
		this.scale.set(x, y);
	}

	@Override
	public void reset() {
		scale.set(0.0f, 0.0f);
		key = StringUtils.EMPTY;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		efs.getDrawable(key).setScale(scale);
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
		Float x = FileCutsceneProvider.nextNumber(s);
		if (x == null) {
			LOG.error("expected scale");
			return null;
		}
		Float y = FileCutsceneProvider.nextNumber(s);
		if (y == null)
			y = x;
		return new FancyScale(key, x, y);
	}

}
