package de.homelab.madgaksha.lotsofbs.cutscenesystem.event;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.idEntityMap;

import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.IEntityCallback;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.MakerUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;

public class EventStage extends ACutsceneEvent {
	private final static Logger LOG = Logger.getLogger(EventStage.class);

	private State state = State.STARTED;
	private String guid = StringUtils.EMPTY;
	private Entity entity = null;
	private boolean isAppearing = true;
	private final EParticleEffect[] particleEffectList = new EParticleEffect[] { null, null };
	private final ESound[] soundList = new ESound[] { null, null, null, null };

	private static enum State {
		STARTED,
		SWITCH_STAGE,
		POST,
		FINISH,
		DONE,
		EFFECT_PLAYING;
	}

	public EventStage() {
	}

	/**
	 * Creates a new event for making an NPC appear.
	 * 
	 * @param guid
	 *            Entity that should appear.
	 * @param pec
	 *            Particle effect to play. Null for none.
	 */
	public EventStage(String guid, boolean isAppearing) {
		this.guid = guid;
		this.isAppearing = isAppearing;
	}

	@Override
	public void reset() {
		state = State.STARTED;
		particleEffectList[0] = null;
		particleEffectList[1] = null;
		soundList[0] = soundList[1] = soundList[2] = soundList[3] = null;
		guid = StringUtils.EMPTY;
		entity = null;
	}

	@Override
	public boolean isFinished() {
		return state == State.DONE;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime, boolean allowSpeedup) {
		final PositionComponent pc;
		switch (state) {
		case STARTED:
			SoundPlayer.getInstance().play(soundList[0]);
			pc = Mapper.positionComponent.get(entity);
			if (pc != null && particleEffectList[0] != null) {
				MakerUtils.addParticleEffectGame(particleEffectList[0], pc, ON_PARTICLE_EFFECT_BEFORE_DONE);
				state = State.EFFECT_PLAYING;
			} else
				state = State.SWITCH_STAGE;
			break;
		case SWITCH_STAGE:
			SoundPlayer.getInstance().play(soundList[1]);
			if (isAppearing) {
				entity.remove(InactiveComponent.class);
				entity.remove(InvisibleComponent.class);
			} else {
				entity.add(gameEntityEngine.createComponent(InactiveComponent.class));
				entity.add(gameEntityEngine.createComponent(InvisibleComponent.class));
			}
			state = State.POST;
			break;
		case POST:
			SoundPlayer.getInstance().play(soundList[2]);
			pc = Mapper.positionComponent.get(entity);
			if (pc != null && particleEffectList[1] != null) {
				MakerUtils.addParticleEffectGame(particleEffectList[1], pc, ON_PARTICLE_EFFECT_AFTER_DONE);
				state = State.EFFECT_PLAYING;
			} else
				state = State.FINISH;
			break;
		case FINISH:
			SoundPlayer.getInstance().play(soundList[3]);
			state = State.DONE;
			break;
		case DONE:
		case EFFECT_PLAYING:
		default:
			break;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public boolean begin() {
		entity = idEntityMap.get(guid);
		if (entity == null)
			LOG.error("cannot begin EventAppear, no such entity: " + guid);
		return entity != null;
	}

	@Override
	public void end() {
	}

	private final IEntityCallback ON_PARTICLE_EFFECT_BEFORE_DONE = new IEntityCallback() {
		@Override
		public void run(Entity entity, Object data) {
			state = State.SWITCH_STAGE;
		}
	};
	private final IEntityCallback ON_PARTICLE_EFFECT_AFTER_DONE = new IEntityCallback() {
		@Override
		public void run(Entity entity, Object data) {
			state = State.FINISH;
		}
	};

	private static enum Command {
		SOUNDBEFORE1 {
			@Override
			public void setup(Scanner s, EventStage e) {
				final ESound sound = FileCutsceneProvider.nextSound(s);
				e.soundList[0] = sound;
			}
		},
		SOUNDBEFORE2 {
			@Override
			public void setup(Scanner s, EventStage e) {
				final ESound sound = FileCutsceneProvider.nextSound(s);
				e.soundList[1] = sound;
			}

		},
		SOUNDAFTER1 {
			@Override
			public void setup(Scanner s, EventStage e) {
				final ESound sound = FileCutsceneProvider.nextSound(s);
				e.soundList[2] = sound;
			}

		},
		SOUNDAFTER2 {
			@Override
			public void setup(Scanner s, EventStage e) {
				final ESound sound = FileCutsceneProvider.nextSound(s);
				e.soundList[3] = sound;
			}

		},
		EFFECTBEFORE {
			@Override
			public void setup(Scanner s, EventStage e) {
				final EParticleEffect pec = FileCutsceneProvider.nextParticleEffect(s);
				e.particleEffectList[0] = pec;
			}
		},
		EFFECTAFTER {
			@Override
			public void setup(Scanner s, EventStage e) {
				final EParticleEffect pec = FileCutsceneProvider.nextParticleEffect(s);
				e.particleEffectList[1] = pec;
			}
		};
		public abstract void setup(Scanner s, EventStage e);
	}

	/**
	 * @param s Scanner from which to read.
	 * @param fh The file handle of the file being used. Should be used only for directories.
	 */
	public static ACutsceneEvent readNextObject(Scanner s, FileHandle fh) {
		// Read entity name
		String guid = FileCutsceneProvider.readNextGuid(s);
		if (guid == null) {
			LOG.error("expected entity name");
			return null;
		}

		if (!s.hasNext()) {
			LOG.error("expected appear/disappear flag");
			return null;
		}

		boolean isAppearing = !s.next().equalsIgnoreCase("exit");

		EventStage event = new EventStage(guid, isAppearing);
		// Set particle effect when given.
		while (s.hasNext()) {
			try {
				Command c = Command.valueOf(s.next().toUpperCase(Locale.ROOT));
				c.setup(s, event);
			} catch (IllegalArgumentException e) {
				LOG.error("unknown command for event stage");
				return null;
			}
		}
		return event;
	}
}
