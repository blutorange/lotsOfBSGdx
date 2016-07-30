package de.homelab.madgaksha.lotsofbs.cutscenesystem.event;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;

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
	public void update(float deltaTime, boolean allowSpeedup) {
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

	/**
	 * @param s Scanner from which to read.
	 * @param fh The file handle of the file being used. Should be used only for directories.
	 */
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
