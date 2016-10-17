package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent.VoiceRetriever;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.util.Transient;

public class FancySoundtarget extends AFancyEvent {
	/** Initial version. */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(FancySoundtarget.class);

	private ESound sound;
	private VoiceRetriever voiceRetriever = null;
	private float volume;

	@Transient private boolean isDone;

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(sound);
		out.writeObject(voiceRetriever);
		out.writeFloat(volume);
	}

	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		final Object sound = in.readObject();
		if (sound == null || !(sound instanceof ESound))
			throw new InvalidDataException("unknown sound");

		final Object voiceRetriever = in.readObject();
		if (voiceRetriever == null || !(voiceRetriever instanceof VoiceRetriever))
			throw new InvalidDataException("unknown sound type");

		this.sound = (ESound) sound;
		this.voiceRetriever = (VoiceRetriever) voiceRetriever;
		volume = in.readFloat();
	}

	public FancySoundtarget(final VoiceRetriever voiceRetriever, final float volume) {
		this(0, 0, voiceRetriever, volume);
	}

	public FancySoundtarget(final float startTime, final int z, final VoiceRetriever voiceRetriever, final float volume) {
		super(startTime, z, true);
		this.voiceRetriever = voiceRetriever;
		this.volume = volume;
	}

	@Override
	public void reset() {
		voiceRetriever = null;
		sound = null;
		volume = 1.0f;
		isDone = false;
	}



	@Override
	public boolean begin(final EventFancyScene efs) {
		// Get voice component for current target.
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
			LOG.error("no such target");
			return false;
		}
		final Entity target = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);
		final VoiceComponent vc = Mapper.voiceComponent.get(target);
		if (vc == null) {
			LOG.error("target does not possess voice component");
			return false;
		}

		sound = voiceRetriever.fetch(vc);
		if (sound == null) {
			LOG.error("target does not possess this type of sound");
			return false;
		}

		if (vc.voicePlayer != null)
			vc.voicePlayer.playUnconditionally(sound, volume);

		isDone = false;

		return true;
	}

	@Override
	public void render(final Batch batch) {
	}

	@Override
	public void update(final float passedTime) {
		isDone = sound == null || passedTime >= sound.getDuration();
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
		if (!s.hasNext()) {
			LOG.error("expected sound type");
			return null;
		}
		final String soundType = s.next();

		VoiceRetriever voiceRetriever = null;
		try {
			voiceRetriever = VoiceRetriever.valueOf(soundType);
		} catch (final IllegalArgumentException e) {
			LOG.error("no such sound type", e);
		}
		if (voiceRetriever == null)
			return null;

		Float volume = FileCutsceneProvider.nextNumber(s);
		if (volume == null)
			volume = 1.0f;

		return new FancySoundtarget(voiceRetriever, volume);
	}
}
