package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.EParticleEffect;

public class FancyPeffect extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyPeffect.class);

	private EParticleEffect particleEffect;
	private String key = StringUtils.EMPTY;
	private float unitsPerScreen = 1.0f;

	public FancyPeffect(String key, EParticleEffect particleEffect, float unitsPerScreen) {
		super(true);
		this.key = key;
		this.unitsPerScreen = unitsPerScreen;
		this.particleEffect = particleEffect;
	}

	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		unitsPerScreen = 1.0f;
		particleEffect = null;
	}

	@Override
	public boolean begin(EventFancyScene scene) {
		scene.getDrawable(key).setImage(particleEffect, unitsPerScreen);
		return false;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float currentTime) {
	}

	@Override
	public void end() {
	}

	@Override
	public void resize(int w, int h) {
	}

	@Override
	public boolean isFinished() {
		return true;
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

		Float unitsPerScreen = FileCutsceneProvider.nextNumber(s);
		if (unitsPerScreen == null) {
			LOG.error("expected unitsPerScreen");
			return null;
		}

		EParticleEffect effect = FileCutsceneProvider.nextParticleEffect(s);
		if (effect == null) {
			LOG.error("expected particle effect");
			return null;
		}

		return new FancyPeffect(key, effect, unitsPerScreen);
	}
}
