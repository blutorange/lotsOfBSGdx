package de.homelab.madgaksha.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.logging.Logger;

public class FancyInclude extends AFancyEvent {

	private EventFancyScene scene;

	public FancyInclude(EventFancyScene scene) {
		super(true);
		this.scene = scene;
	}

	private final static Logger LOG = Logger.getLogger(FancyInclude.class);

	@Override
	public void reset() {
		this.scene = null;
	}

	@Override
	public boolean configure(EventFancyScene efs) {
		return true;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		return scene.begin();
	}

	@Override
	public void render() {
		scene.renderMain();
	}

	@Override
	public void update(float deltaTime, float passedTime) {
		scene.update(deltaTime);
	}

	@Override
	public void resize(int w, int h) {
		scene.resize(w, h);
	}

	@Override
	public boolean isFinished() {
		return scene.isFinished();
	}

	@Override
	public void end() {
		scene.end();
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		EventFancyScene scene = EventFancyScene.readNextObject(s, parentFile);
		if (scene == null) {
			LOG.error("could not read include file");
			return null;
		}
		return new FancyInclude(scene);
	}
}
