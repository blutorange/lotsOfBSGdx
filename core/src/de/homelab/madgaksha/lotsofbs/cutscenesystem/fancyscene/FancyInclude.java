package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class FancyInclude extends AFancyEvent {

	private EventFancyScene scene;
	private EventFancyScene parent;
	private float lastTime = 0.0f;

	public FancyInclude(EventFancyScene scene) {
		super(true);
		this.scene = scene;
	}

	private final static Logger LOG = Logger.getLogger(FancyInclude.class);

	@Override
	public void reset() {
		scene = null;
		parent = null;
		lastTime = 0.0f;
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		parent = efs;
		scene.setParent(parent);
		lastTime = 0.0f;
		return scene.begin();
	}

	@Override
	public void render() {
		scene.renderMain();
	}

	@Override
	public void update(float currentTime) {
		if (lastTime != currentTime)
			scene.update(parent.getDeltaTime(), parent.isSpedup());
		lastTime = currentTime;
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

	@Override
	public void attachedToScene(EventFancyScene scene) {
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
