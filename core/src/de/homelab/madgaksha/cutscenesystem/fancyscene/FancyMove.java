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
import path.APath;
import path.EPath;

public class FancyMove extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancyMove.class);

	private APath path;
	private String key = StringUtils.EMPTY;
	private Interpolation interpolation = Interpolation.linear;
	private boolean isDone = false;
	private FancySpriteWrapper sprite;
	
	public FancyMove(String key, APath path, Interpolation interpolation) {
		super(true);
		this.path = path;
		this.key = key;
		this.interpolation = interpolation;
	}
	
	@Override
	public void reset() {
		path = null;
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
		path.setOrigin(sprite.position);
		return true;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime, float passedTime) {
		if (passedTime >= path.tmax) {
			passedTime = path.tmax;
			isDone = true;
		}
		path.applyWithInterpolation(passedTime, sprite.position, interpolation);
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
		
		EPath path = FileCutsceneProvider.nextPath(s);
		if (path == null) return null;
		
		Float duration = FileCutsceneProvider.nextNumber(s);
		if (duration == null) {
			LOG.error("expected duration");
			return null;
		}
		
		Interpolation interpolation = FileCutsceneProvider.readNextInterpolation(s);
		if (interpolation == null) interpolation = Interpolation.linear;

		if (!s.hasNext()) {
			LOG.error("expected relative/absolute flag");
			return null;
		}
		boolean relativePath = s.next().equalsIgnoreCase("r");
		
		APath newPath = path.readNextObject(duration, relativePath, 1.0f, 1.0f, s);
		if (newPath == null) {
			LOG.error("expected path data");
			return null;
		}
		
		return new FancyMove(key, newPath, interpolation);
	}


}
