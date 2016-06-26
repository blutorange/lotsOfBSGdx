package de.homelab.madgaksha.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.FancySpriteWrapper;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

public class FancyShow extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyShow.class);

	private String key = StringUtils.EMPTY;
	private FancySpriteWrapper sprite;
	private float duration;
	private boolean isDone = false;
	private float scaleX = 640.0f / 8.0f;
	private float scaleY = scaleX * 9.0f / 8.0f;
	private float scaleDpi = 1.0f;

	public FancyShow(String key, float duration) {
		super(true);
		this.key = key;
		this.duration = duration;
	}

	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		duration = 0.0f;
		sprite = null;
		isDone = false;
		scaleX = 640.0f / 8.0f;
		scaleY = scaleX * 9.0f / 8.0f;
		scaleDpi = 1.0f;
	}

	@Override
	public boolean configure(EventFancyScene efs) {
		efs.addSprite(key);
		return true;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		this.sprite = efs.getSprite(key);
		this.sprite.sprite.setOriginCenter();
		setScale();
		return true;
	}

	@Override
	public void render() {
		switch (sprite.mode) {
		case TEXTURE:
			sprite.sprite.draw(batchPixel);
			break;
		case NINE_PATCH:
			float x = (sprite.position.x + 4.0f) * scaleX;
			float y = (sprite.position.y + 4.5f) * scaleY;
			float w = sprite.ninePatchDimensions.x * scaleX;
			float h = sprite.ninePatchDimensions.y * scaleY;
			sprite.ninePatch.draw(batchPixel, x-w, y-h, w+w, h+h);
			break;
		}

	}

	@Override
	public void update(float deltaTime, float passedTime) {
		switch (sprite.mode) {
		case TEXTURE:
			sprite.sprite.setScale(scaleDpi);
			sprite.sprite.setAlpha(sprite.opacity);
			sprite.sprite.setCenter((sprite.position.x + 4.0f) * scaleX, (sprite.position.y + 4.5f) * scaleY);
			break;
		case NINE_PATCH:
			Color color = sprite.ninePatch.getColor();
			color.a = sprite.opacity;
			sprite.ninePatch.setColor(color);
			break;
		}
		if (passedTime >= duration)
			isDone = true;
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
		reset();
	}

	@Override
	public void resize(int w, int h) {
		setScale();
	}

	private void setScale() {
		scaleX = viewportGame.getScreenWidth() / 8.0f;
		scaleY = viewportGame.getScreenHeight() / 9.0f;
		if (sprite != null) scaleDpi = viewportGame.getScreenWidth() / (8.0f * sprite.spriteDpi);
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
		return new FancyShow(key, duration);
	}
}
