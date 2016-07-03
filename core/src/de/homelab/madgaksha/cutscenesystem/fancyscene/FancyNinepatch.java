package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ENinePatch;

public class FancyNinepatch extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyNinepatch.class);

	private String key = StringUtils.EMPTY;
	private Vector2 dimensions = new Vector2();
	private ENinePatch ninePatch;

	public FancyNinepatch(String key, Vector2 dimensions, ENinePatch ninePatch) {
		super(true);
		this.key = key;
		this.dimensions.set(dimensions);
		this.ninePatch = ninePatch;
	}

	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		ninePatch = null;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		efs.getDrawable(key).setDrawable(ninePatch, dimensions);
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
			LOG.error("expected nine patch name");
			return null;
		}
		String key = s.next();
		Float width = FileCutsceneProvider.nextNumber(s);
		Float height = FileCutsceneProvider.nextNumber(s);
		if (width == null || height == null) {
			LOG.error("expected nine patch dimensions");
			return null;
		}
		ENinePatch ninePatch = FileCutsceneProvider.nextNinePatch(s);
		return (ninePatch != null) ? new FancyNinepatch(key, new Vector2(width, height), ninePatch) : null;
	}
}
