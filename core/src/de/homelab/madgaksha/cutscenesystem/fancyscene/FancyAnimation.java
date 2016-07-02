package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimation;

public class FancyAnimation extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyAnimation.class);

	private String key = StringUtils.EMPTY;
	private float dpi;
	private EAnimation animation;
	
	public FancyAnimation(String key, float dpi, EAnimation animation) {
		super(true);
		this.dpi = dpi;
		this.key = key;
		this.animation = animation;
	}
	
	@Override
	public void reset() {
		dpi = 1.0f;
		key = StringUtils.EMPTY;
		animation = null;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		efs.getDrawable(key).setDrawable(animation, dpi);
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
		EAnimation animation = FileCutsceneProvider.nextAnimation(s);
		if (animation == null) {
			LOG.error("expected animation");
			return null;
		}
		return new FancyAnimation(key, dpi, animation);
	}
}
