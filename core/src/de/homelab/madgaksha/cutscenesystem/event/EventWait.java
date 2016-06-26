package de.homelab.madgaksha.cutscenesystem.event;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.logging.Logger;

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
	public void update(float deltaTime) {
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

	public static ACutsceneEvent readNextObject(Scanner s, FileHandle fh) {
		Float number = FileCutsceneProvider.nextNumber(s);
		if (number == null) {
			LOG.error("expected wait time");
			return null;
		}
		return new EventWait(number);
	}
}
