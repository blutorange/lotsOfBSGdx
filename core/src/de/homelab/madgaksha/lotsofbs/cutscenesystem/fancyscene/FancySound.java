package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.audiosystem.VoicePlayer;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancySound extends AFancyEvent {
	/** Initial version. */
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(FancySound.class);

	private ESound sound;
	private float volume;
	private String entityName;
	
	@Transient private VoicePlayer voicePlayer;
	@Transient private boolean isDone = false;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeUTF(entityName);
		out.writeObject(sound);
		out.writeFloat(volume);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		String entityName = in.readUTF();
		Entity e = FancySound.entityFromName(entityName);
		VoiceComponent vc = e != null ? Mapper.voiceComponent.get(e) : null;
		if (vc != null && vc.voicePlayer != null)
			this.voicePlayer = vc.voicePlayer;
		else
			this.voicePlayer = new VoicePlayer();
		
		Object sound = in.readObject();
		if (sound == null || !(sound instanceof ESound)) throw new InvalidDataException("no such sound");
		this.sound = (ESound) sound;
		
		this.volume = in.readFloat();
		
		this.isDone = false;
	}


	
	/**
	 * A sound for the given entity or a general sound.
	 * 
	 * @param e
	 *            The entity making this sound, or null for an environmental
	 *            sound.
	 * @param sound
	 *            The sound that should be played.
	 * @param volume
	 *            Volume of the sound.
	 */
	public FancySound(String entityName, ESound sound, float volume) {
		super(true);
		Entity e = FancySound.entityFromName(entityName);
		VoiceComponent vc = e != null ? Mapper.voiceComponent.get(e) : null;
		if (vc != null && vc.voicePlayer != null)
			this.voicePlayer = vc.voicePlayer;
		else
			this.voicePlayer = new VoicePlayer();
		this.entityName = entityName;
		this.sound = sound;
		this.volume = volume;
	}

	/**
	 * A sound for the given entity or a general sound at a volume of 1.0f.
	 * 
	 * @param e
	 *            The entity making this sound, or null for an environmental
	 *            sound.
	 * @param sound
	 *            The sound that should be played.
	 */
	public FancySound(String entityName, ESound sound) {
		this(entityName, sound, 1.0f);
	}

	@Override
	public boolean begin(EventFancyScene efs) {
		voicePlayer.playUnconditionally(sound, volume);
		return true;
	}

	@Override
	public void reset() {
		voicePlayer = null;
		sound = null;
		volume = 1.0f;
		isDone = false;
	}

	@Override
	public void render(Batch batch) {
	}

	@Override
	public void update(float passedTime) {
		isDone = passedTime >= sound.getDuration();
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
	}

	@Override
	public void drawableChanged(EventFancyScene scene, String changedKey) {
	}
	
	@Override
	public void attachedToScene(EventFancyScene scene) {
	}

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		try {
			if (!s.hasNext()) {
				LOG.error("expected entity name");
				return null;
			}
			String entityName = s.next().toLowerCase(Locale.ROOT);
			ESound sound = FileCutsceneProvider.nextSound(s);
			return (sound != null) ? new FancySound(entityName, sound) : null;
		} catch (IllegalArgumentException e) {
			LOG.error("could not read entity with voice player", e);
			return null;
		}
	}

	private static Entity entityFromName(String entityName) throws IllegalArgumentException {
		if (entityName.equals("player"))
			return playerHitCircleEntity;
		else if (entityName.equals("general"))
			return null;
		else {
			LOG.error("no such entity: " + entityName);
			return null;
		}
	}
}
