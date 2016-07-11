package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyPosition extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyPosition.class);

	private String key = StringUtils.EMPTY;
	private Vector2 position = new Vector2();
	private Vector2 delta = new Vector2();

	public FancyPosition(String key, float x, float y, float dx, float dy) {
		super(true);
		this.key = key;
		this.position.set(x, y);
		this.delta.set(Math.max(0.0f, dx), Math.max(0.0f, dy));
	}

	@Override
	public void reset() {
		position.set(0.0f, 0.0f);
		key = StringUtils.EMPTY;
	}

	@Override
	public boolean begin(EventFancyScene scene) {
		if (delta.x != 0.0f || delta.y != 0.0f) {
			float dx = MathUtils.random(-delta.x, delta.x);
			float dy = MathUtils.random(-delta.y, delta.y);
			scene.getDrawable(key).setPosition(position.x + dx, position.y + dy);
		} else
			scene.getDrawable(key).setPosition(position);
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

		Float x = FileCutsceneProvider.nextNumber(s);
		Float y = FileCutsceneProvider.nextNumber(s);

		if (x == null || y == null) {
			LOG.error("expected position");
			return null;
		}

		Float dx = FileCutsceneProvider.nextNumber(s);
		Float dy = FileCutsceneProvider.nextNumber(s);
		if (dx == null)
			dx = 0.0f;
		if (dy == null)
			dy = 0.0f;

		return new FancyPosition(key, x, y, dx, dy);
	}

}
