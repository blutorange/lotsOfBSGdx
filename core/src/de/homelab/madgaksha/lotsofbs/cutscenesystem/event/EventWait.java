package de.homelab.madgaksha.lotsofbs.cutscenesystem.event;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class EventWait extends ACutsceneEvent {
	private final static Logger LOG = Logger.getLogger(EventWait.class);

	private float remainingWaitTime;
	private float timeToWait;

	/**
	 * An event that simply wait for some time and then proceeds to the next
	 * event.
	 * 
	 * @param timeToWait
	 *            Waiting time in seconds.
	 */
	public EventWait(float timeToWait) {
		this.timeToWait = timeToWait;
	}

	@Override
	public boolean isFinished() {
		return remainingWaitTime <= 0.0f;
	}

	@Override
	public void render() {
	}

	@Override
	public void update(float deltaTime, boolean allowSpeedup) {
		remainingWaitTime -= deltaTime;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public boolean begin() {
		remainingWaitTime = timeToWait;
		return true;
	}

	public void resetTimer() {
		remainingWaitTime = timeToWait;
	}

	public void cancelTimer() {
		remainingWaitTime = 0.0f;
	}

	@Override
	public void reset() {
		this.timeToWait = 0.0f;
	}

	@Override
	public void end() {
	}

	/**
	 * @param s Scanner from which to read.
	 * @param fh The file handle of the file being used. Should be used only for directories.
	 */
	public static ACutsceneEvent readNextObject(Scanner s, FileHandle fh) {
		Float number = FileCutsceneProvider.nextNumber(s);
		if (number == null) {
			LOG.error("expected wait time");
			return null;
		}
		return new EventWait(number);
	}
}
