package de.homelab.madgaksha.cutscenesystem.event;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;

public class EventScore extends ACutsceneEvent {
	private final static Logger LOG = Logger.getLogger(EventScore.class);

	private long score = 0L;
	private boolean eventDone = false;

	/**
	 * An event that simply wait for some time and then proceeds to the next
	 * event.
	 * 
	 * @param timeToWait
	 *            Waiting time in seconds.
	 */
	public EventScore(long score) {
		this.score = score;
	}

	@Override
	public boolean isFinished() {
		return eventDone;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime) {
		GlobalBag.gameScore.increaseBy(score);
		SoundPlayer.getInstance().play(ESound.SCORE_BULLET_HIT);
		eventDone = true;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public boolean begin() {
		return true;
	}

	@Override
	public void end() {
	}

	public static ACutsceneEvent readNextObject(Scanner s, FileHandle fh) {
		Long score = FileCutsceneProvider.nextLong(s);
		if (score == null) {
			LOG.error("integer score expected");
			return null;
		}
		return new EventScore(score);
	}

	@Override
	public void reset() {
		score = 0L;
		eventDone = false;
	}
}
