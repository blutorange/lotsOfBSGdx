package de.homelab.madgaksha.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.GlobalBag.playerHitCircleEntity;

import java.util.Locale;
import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.audiosystem.VoicePlayer;
import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;

public class FancySound extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancySound.class);

	private VoicePlayer voicePlayer;
	private ESound sound;
	private float volume;
	private boolean isDone = false;
	
	/**
	 * A sound for the given entity or a general sound.
	 * @param e The entity making this sound, or null for an environmental sound.
	 * @param sound The sound that should be played.
	 * @param volume Volume of the sound.
	 */
	public FancySound(Entity e, ESound sound, float volume) {
		super(true);
		VoiceComponent vc = Mapper.voiceComponent.get(e);
		if (vc != null && vc.voicePlayer != null) this.voicePlayer = vc.voicePlayer;
		else this.voicePlayer = new VoicePlayer();
		this.sound = sound;
		this.volume = volume;
	}
	/**
	 * A sound for the given entity or a general sound at a volume of 1.0f.
	 * @param e The entity making this sound, or null for an environmental sound.
	 * @param sound The sound that should be played.
	 */
	public FancySound(Entity e, ESound sound) {
		this(e, sound, 1.0f);
	}
	
	@Override
	public boolean begin(EventFancyScene efs) {
		voicePlayer.play(sound, volume);
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
	public void render() {
	}

	@Override
	public void update(float deltaTime, float passedTime) {
		isDone = passedTime >= sound.getDuration();
	}

	@Override
	public boolean isFinished() {
		return isDone;
	}

	@Override
	public void end() {
	}
	
	public static AFancyEvent readNextObject(Scanner s, FileHandle parentFile) {
		try {
			Entity entity = nextEntity(s);
			ESound sound = FileCutsceneProvider.nextSound(s);
			return (sound != null) ? new FancySound(entity, sound) : null;
		}
		catch (IllegalArgumentException e) {
			LOG.error("could not read entity", e);
			return null;
		}
	}
	
	private static Entity nextEntity(Scanner s) throws IllegalArgumentException {
		if (!s.hasNext()) return null;
		String name = s.next().toLowerCase(Locale.ROOT);
		if (name.equals("player")) {
			return playerHitCircleEntity;
		}
		else if (name.equals("general")) return null;
		else throw new IllegalArgumentException("no such entity");
	}
	@Override
	public boolean configure(EventFancyScene efs) {
		return true;
	}
}
