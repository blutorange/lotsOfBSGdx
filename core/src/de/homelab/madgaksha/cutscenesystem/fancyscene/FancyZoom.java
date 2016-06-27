package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.FancySpriteWrapper;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

public class FancyZoom extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyZoom.class);

	private String key = StringUtils.EMPTY;
	private float duration = 1.0f;
	private float durationInverse = 1.0f;
	private Vector2 originalScale = new Vector2(1f,1f);
	private Vector2 targetScale = new Vector2(1f,1f);
	private Interpolation interpolation = Interpolation.linear;
	private boolean isDone = false;
	private FancySpriteWrapper sprite;
	
	public FancyZoom(String key, float targetScaleX, float targetScaleY, Interpolation interpolation, float duration) {
		super(true);
		this.targetScale.set(targetScaleX, targetScaleY);
		this.key = key;
		this.interpolation = interpolation;
		this.duration = duration;
		this.durationInverse = 1.0f/duration;
	}
	
	@Override
	public void reset() {
		targetScale.set(1f,1f);
		duration = durationInverse = 1.0f;
		isDone = false;
		key = StringUtils.EMPTY;
		sprite = null;
		interpolation = Interpolation.linear;
	}

	@Override
	public boolean configure(EventFancyScene efs) {
		return true;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		this.sprite = efs.getSprite(key);
		originalScale.set(sprite.scale);
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
		sprite.scale.set(originalScale).lerp(targetScale, interpolation.apply(passedTime * durationInverse));
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
		if (duration < 0.001f) {
			LOG.error("duration must be greater than 0");
			return null;
		}
		
		Interpolation interpolation = FileCutsceneProvider.readNextInterpolation(s);
		if (interpolation == null) interpolation = Interpolation.linear;

		
		Float targetScaleX = FileCutsceneProvider.nextNumber(s);
		if (targetScaleX == null) {
			LOG.error("expected target scale");
			return null;
		}
		
		Float targetScaleY = FileCutsceneProvider.nextNumber(s);
		if (targetScaleY == null) targetScaleY = targetScaleX; 
		
		return new FancyZoom(key, targetScaleX, targetScaleY, interpolation, duration);
	}
}
