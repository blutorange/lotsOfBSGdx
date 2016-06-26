package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.FancySpriteWrapper;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

public class FancyFade extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyFade.class);

	private String key = StringUtils.EMPTY;
	private Interpolation interpolation = Interpolation.linear;
	private float duration = 1.0f;
	private float durationInverse = 1.0f;
	private boolean isDone = false;
	private float startOpacity = 1.0f;
	private float targetOpacity = 1.0f;
	private FancySpriteWrapper sprite;
	
	public FancyFade(String key, float targetOpacity, float duration, Interpolation interpolation) {
		super(true);
		if (duration < 0.01f) throw new IllegalArgumentException("duration must be greate than 0");
		this.targetOpacity = targetOpacity;
		this.duration = duration;
		this.durationInverse = 1.0f/duration;
		this.interpolation = interpolation;
		this.key = key;
	}
	
	@Override
	public void reset() {
		interpolation = Interpolation.linear;
		isDone = false;
		key = StringUtils.EMPTY;
		sprite = null;
		duration = durationInverse = 1.0f;
		startOpacity = targetOpacity = 1.0f;
	}

	@Override
	public boolean configure(EventFancyScene efs) {
		return true;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		this.sprite = efs.getSprite(key);
		this.startOpacity = sprite.opacity;
		return true;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime, float passedTime) {
		if (passedTime >= duration) {
			passedTime = duration;
			isDone = true;
		}
		sprite.opacity = interpolation.apply(startOpacity, targetOpacity, passedTime * durationInverse);
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
		reset();
	}
	
	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		String key = s.next();
		
		Float duration = FileCutsceneProvider.nextNumber(s);
		if (duration == null) {
			LOG.error("expected duration");
			return null;
		}
		
		Float targetOpacity = FileCutsceneProvider.nextNumber(s);
		if (targetOpacity == null) {
			LOG.error("expected target opacity");
		}
		
		Interpolation interpolation = FileCutsceneProvider.readNextInterpolation(s);
		if (interpolation == null) interpolation = Interpolation.linear;
		
		return new FancyFade(key, targetOpacity, duration, interpolation);
	}
}