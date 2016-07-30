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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(sound);
		out.writeObject(voiceRetriever);
		out.writeFloat(volume);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		Object sound = in.readObject();
		if (sound == null || !(sound instanceof ESound))
			throw new InvalidDataException("unknown sound");

		Object voiceRetriever = in.readObject();
		if (voiceRetriever == null || !(voiceRetriever instanceof VoiceRetriever))
			throw new InvalidDataException("unknown sound type");

		this.sound = (ESound) sound;
		this.voiceRetriever = (VoiceRetriever) voiceRetriever;
		this.volume = in.readFloat();
	}

	public FancySoundtarget(VoiceRetriever voiceRetriever, float volume) {
		super(true);
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
	public boolean begin(EventFancyScene efs) {
		// Get voice component for current target.
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
			LOG.error("no such target");
			return false;
		}
		Entity target = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);
		VoiceComponent vc = Mapper.voiceComponent.get(target);
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
	public void render(Batch batch) {
	}

	@Override
	public void update(float passedTime) {
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
		if (!s.hasNext()) {
			LOG.error("expected sound type");
			return null;
		}
		String soundType = s.next();

		VoiceRetriever voiceRetriever = null;
		try {
			voiceRetriever = VoiceRetriever.valueOf(soundType);
		} catch (IllegalArgumentException e) {
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
