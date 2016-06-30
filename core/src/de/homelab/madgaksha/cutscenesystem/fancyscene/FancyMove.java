package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.path.APath;
import de.homelab.madgaksha.path.EPath;

public class FancyMove extends DrawableFancy {
	private final static Logger LOG = Logger.getLogger(FancyMove.class);

	private APath path;
	private Interpolation interpolation = Interpolation.linear;
	private Vector2 vector = new Vector2();
	private boolean isDone = false;
	
	public FancyMove(String key, APath path, Interpolation interpolation) {
		super(key);
		this.path = path;
		this.interpolation = interpolation;
	}
	
	@Override
	public void resetSubclass() {
		path = null;
		isDone = false;
		interpolation = Interpolation.linear;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		path.setOrigin(drawable.getPosition);
		isDone = false;
		return true;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float passedTime) {
		if (passedTime >= path.getTMax()) {
			passedTime = path.getTMax();
			isDone = true;
		}
		path.applyWithInterpolation(passedTime, vector, interpolation);
		drawable.setPosition(vector);
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
		// Make sure the sprite ends up at the correct position.
		update(path.getTMax());
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
		if (duration < 0.001f) {
			LOG.error("duration must be greater than 0");
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
