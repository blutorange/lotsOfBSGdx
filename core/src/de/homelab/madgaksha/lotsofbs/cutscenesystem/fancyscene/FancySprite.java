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
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;

public class FancySprite extends AFancyEvent {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancySprite.class);

	private String key = StringUtils.EMPTY;
	private float dpi;
	private ETexture texture;

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(key);
		out.writeObject(texture);
		out.writeFloat(dpi);
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		String key = in.readUTF();
		if (key == null || key.isEmpty()) throw new InvalidDataException("key cannot be null or empty");
		
		Object texture = in.readObject();
		if (texture == null || !(texture instanceof ETexture)) throw new InvalidDataException("unknown sprite");
		
		this.key = key;
		this.texture = (ETexture) texture;
		dpi = in.readFloat();
	}
	
	public FancySprite(String key, float dpi, ETexture texture) {
		super(true);
		this.dpi = dpi;
		this.key = key;
		this.texture = texture;
	}

	@Override
	public void reset() {
		dpi = 1.0f;
		key = StringUtils.EMPTY;
		texture = null;
	}

	@Override
	public boolean begin(EventFancyScene scene) {
		scene.requestDrawableSprite(key).setDrawable(texture, dpi);
		return false;
	}

	@Override
	public void render(Batch batch) {
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
	public void drawableChanged(EventFancyScene scene, String changedKey) {
	}

	@Override
	public void attachedToScene(EventFancyScene scene) {
		scene.requestDrawableSprite(key);
	}

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		if (!s.hasNext()) {
			LOG.error("expected sprite name");
			return null;
		}
		String key = s.next();

		Float dpi = FileCutsceneProvider.nextNumber(s);
		if (dpi == null) {
			LOG.error("expected dpi");
			return null;
		}
		ETexture texture = FileCutsceneProvider.nextTexture(s);
		if (texture == null) {
			LOG.error("expected texture");
			return null;
		}
		return new FancySprite(key, dpi, texture);
	}
}
