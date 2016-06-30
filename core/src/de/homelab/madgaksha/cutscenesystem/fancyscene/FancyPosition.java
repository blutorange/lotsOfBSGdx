package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

public class FancyPosition extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyPosition.class);

	private String key = StringUtils.EMPTY;
	private Vector2 position = new Vector2();
	
	public FancyPosition(String key, float x, float y) {
		super(true);
		this.key = key;
		this.position.set(x, y);
	}
	
	@Override
	public void reset() {
		position.set(0.0f,0.0f);
		key = StringUtils.EMPTY;
	}

	@Override
	public boolean begin(EventFancyScene scene) {
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
		return (x != null && y != null) ? new FancyPosition(key, x, y) : null;
	}

}
