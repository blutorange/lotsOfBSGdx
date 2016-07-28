package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancyInclude extends AFancyEvent {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	
	private EventFancyScene scene;
	@Transient private float lastTime = 0.0f;
	@Transient private EventFancyScene parent;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(scene);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		Object scene = in.readObject();
		if (scene == null || !(scene instanceof EventFancyScene)) throw new InvalidDataException("invalid scene data");
		this.scene = (EventFancyScene) scene;
		
		parent = null;
		lastTime = 0.0f;
	}
	
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
	public void render(Batch batch) {
		scene.renderMain(batch);
	}

	@Override
	public void update(float currentTime) {
		if (lastTime != currentTime)
			scene.update(currentTime-lastTime, parent.isSpedup());
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
	public void drawableChanged(EventFancyScene scene, String changedKey) {
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
