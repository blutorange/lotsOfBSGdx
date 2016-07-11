package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ENinePatch;

public class FancyNinepatch extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyNinepatch.class);

	private String key = StringUtils.EMPTY;
	private float unitPerPixel = 1.0f;
	private ENinePatch ninePatch;

	public FancyNinepatch(String key, float unitPerPixel, ENinePatch ninePatch) {
		super(true);
		this.key = key;
		this.ninePatch = ninePatch;
	}

	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		ninePatch = null;
	}

	@Override
	public boolean begin(EventFancyScene scene) {
		scene.requestDrawableNinePatch(key).setDrawable(ninePatch, unitPerPixel);
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
	
	@Override
	public void drawableChanged(EventFancyScene scene, String changedKey) {
	}

	@Override
	public void attachedToScene(EventFancyScene scene) {
		scene.requestDrawableNinePatch(key);
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected nine patch name");
			return null;
		}
		String key = s.next();
		
		Float unitPerPixel = FileCutsceneProvider.nextNumber(s);
		if (unitPerPixel == null) {
			LOG.error("expected unit per pixel");
			return null;
		}
		
		ENinePatch ninePatch = FileCutsceneProvider.nextNinePatch(s);
		
		return (ninePatch != null) ? new FancyNinepatch(key, unitPerPixel, ninePatch) : null;
	}
}
