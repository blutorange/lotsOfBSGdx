package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

public class FancyOpacity extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyOpacity.class);

	private String key = StringUtils.EMPTY;
	private float opacity;
	
	public FancyOpacity(String key, float opacity) {
		super(true);
		this.key = key;
		this.opacity = opacity;
	}
	
	@Override
	public void reset() {
		opacity = 1.0f;
		key = StringUtils.EMPTY;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		efs.getDrawable(key).setOpacity(opacity);
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
		Float opacity = FileCutsceneProvider.nextNumber(s);
		if (opacity == null) {
			LOG.error("expected opacity");
			return null;
		}
		return new FancyOpacity(key, opacity);
	}

}
