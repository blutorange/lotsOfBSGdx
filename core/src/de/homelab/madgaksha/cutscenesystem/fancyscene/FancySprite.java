package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;

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
		key = StringUtils.EMPTY;
		texture = null;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		efs.setSpriteTexture(key, texture, dpi);
		return false;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime, float passedTime) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void end() {
	}
	
	@Override
	public boolean configure(EventFancyScene efs) {
		efs.addSprite(key);
		return true;
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
		return (texture != null) ? new FancySprite(key, dpi, texture) : null;
	}
}