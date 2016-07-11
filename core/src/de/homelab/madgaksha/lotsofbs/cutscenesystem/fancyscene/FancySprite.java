package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;

public class FancySprite extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancySprite.class);

	private String key = StringUtils.EMPTY;
	private float dpi;
	private ETexture texture;

	public FancySprite(String key, float dpi, ETexture texture) {
		super(true);
		this.dpi = dpi;
		this.key = key;
		this.texture = texture;
	}

	@Override
	public void reset() {
		dpi = 1.0f;
		key = StringUtils.EMPTY;
		texture = null;
	}

	@Override
	public boolean begin(EventFancyScene scene) {
		scene.getDrawable(key).setDrawable(texture, dpi);
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

		Float dpi = FileCutsceneProvider.nextNumber(s);
		if (dpi == null) {
			LOG.error("expected dpi");
			return null;
		}
		ETexture texture = FileCutsceneProvider.nextTexture(s);
		if (texture == null) {
			LOG.error("expected texture");
			return null;
		}
		return new FancySprite(key, dpi, texture);
	}
}
