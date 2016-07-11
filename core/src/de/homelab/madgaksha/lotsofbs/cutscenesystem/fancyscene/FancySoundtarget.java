package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;

import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;

public class FancySoundtarget extends AFancyEvent {
	private final static Logger LOG = Logger.getLogger(FancySoundtarget.class);

	private VoiceRetriever voiceRetriever = null;
	private ESound sound;
	private float volume;
	private boolean isDone;

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

	public static enum VoiceRetriever {
		onLightDamage {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onLightDamage;
			}
		},
		onHeavyDamage {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onHeavyDamage;
			}
		},
		onBattleModeExit {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onBattleModeExit;
			}
		},
		onBattleModeFlee {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onBattleModeFlee;
			}
		},
		onBattleModeStart {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onBattleModeStart;
			}
		},
		onDeath {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onDeath;
			}
		},
		onEnemyKilled {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onEnemyKilled;
			}
		},
		onSpawn {
			@Override
			public ESound fetch(VoiceComponent vc) {
				return vc.onSpawn;
			}
		},;
		public abstract ESound fetch(VoiceComponent vc);
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
	public void render() {
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
	public void attachedToScene(EventFancyScene scene) {
	}

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
