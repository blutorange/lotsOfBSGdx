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
	private final String entityName;

	@Transient private VoicePlayer voicePlayer;
	@Transient private boolean isDone = false;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeUTF(entityName);
		out.writeObject(sound);
		out.writeFloat(volume);
	}
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final String entityName = in.readUTF();
		final Entity e = FancySound.entityFromName(entityName);
		final VoiceComponent vc = e != null ? Mapper.voiceComponent.get(e) : null;
		if (vc != null && vc.voicePlayer != null)
			voicePlayer = vc.voicePlayer;
		else
			voicePlayer = new VoicePlayer();

		final Object sound = in.readObject();
		if (sound == null || !(sound instanceof ESound)) throw new InvalidDataException("no such sound");
		this.sound = (ESound) sound;

		volume = in.readFloat();

		isDone = false;
	}


	public FancySound(final String entityName, final ESound sound, final float volume) {
		this(0, 0, entityName, sound, volume);
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
	public FancySound(final float startTime, final int z, final String entityName, final ESound sound, final float volume) {
		super(startTime, z, true);
		final Entity e = FancySound.entityFromName(entityName);
		final VoiceComponent vc = e != null ? Mapper.voiceComponent.get(e) : null;
		if (vc != null && vc.voicePlayer != null)
			voicePlayer = vc.voicePlayer;
		else
			voicePlayer = new VoicePlayer();
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
	public FancySound(final String entityName, final ESound sound) {
		this(entityName, sound, 1.0f);
	}

	@Override
	public boolean begin(final EventFancyScene efs) {
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
	public void render(final Batch batch) {
	}

	@Override
	public void update(final float passedTime) {
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
	public void drawableChanged(final EventFancyScene scene, final String changedKey) {
	}

	@Override
	public void attachedToScene(final EventFancyScene scene) {
	}

	/**
	 * @param s Scanner from which to read.
	 * @param parentFile The file handle of the file being used. Should be used only for directories.
	 */
	public static AFancyEvent readNextObject(final Scanner s, final FileHandle parentFile) {
		try {
			if (!s.hasNext()) {
				LOG.error("expected entity name");
				return null;
			}
			final String entityName = s.next().toLowerCase(Locale.ROOT);
			final ESound sound = FileCutsceneProvider.nextSound(s);
			return sound != null ? new FancySound(entityName, sound) : null;
		} catch (final IllegalArgumentException e) {
			LOG.error("could not read entity with voice player", e);
			return null;
		}
	}

	private static Entity entityFromName(final String entityName) throws IllegalArgumentException {
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
