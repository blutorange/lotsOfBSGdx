package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;

public class FancyPeffect extends AFancyEvent {
	/** Initial version */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancyPeffect.class);

	private EParticleEffect particleEffect;
	private String key = StringUtils.EMPTY;
	private float unitsPerScreen = 1.0f;

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(key);
		out.writeObject(particleEffect);
		out.writeFloat(unitsPerScreen);
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		String key = in.readUTF();
		if (key == null || key.isEmpty()) throw new InvalidDataException("key cannot be null or empty");
		
		Object particleEffect = in.readObject();
		if (particleEffect == null || !(particleEffect instanceof EParticleEffect)) throw new InvalidDataException("unknown particle effect");
		
		this.key = key;
		this.particleEffect = (EParticleEffect) particleEffect;
		unitsPerScreen = in.readFloat();
	}
	
	public FancyPeffect(String key, EParticleEffect particleEffect, float unitsPerScreen) {
		super(true);
		this.key = key;
		this.unitsPerScreen = unitsPerScreen;
		this.particleEffect = particleEffect;
	}

	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		unitsPerScreen = 1.0f;
		particleEffect = null;
	}

	@Override
	public boolean begin(EventFancyScene scene) {
		scene.requestDrawableParticleEffect(key).setDrawable(particleEffect, unitsPerScreen);
		return false;
	}

	@Override
	public void render(Batch batch) {
	}

	@Override
	public void update(float currentTime) {
	}

	@Override
	public void end() {
	}

	@Override
	public void resize(int w, int h) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}
	
	@Override
	public void drawableChanged(EventFancyScene scene, String changedKey) {
	}

	@Override
	public void attachedToScene(EventFancyScene scene) {
		scene.requestDrawableParticleEffect(key);
	}

	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		String key = s.next();

		Float unitsPerScreen = FileCutsceneProvider.nextNumber(s);
		if (unitsPerScreen == null) {
			LOG.error("expected unitsPerScreen");
			return null;
		}

		EParticleEffect effect = FileCutsceneProvider.nextParticleEffect(s);
		if (effect == null) {
			LOG.error("expected particle effect");
			return null;
		}

		return new FancyPeffect(key, effect, unitsPerScreen);
	}
}
