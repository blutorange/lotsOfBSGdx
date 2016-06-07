package de.homelab.madgaksha.cutscenesystem.event;

import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.logging.Logger;

public class EventWait extends ACutsceneEvent {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EventWait.class);

	private float remainingWaitTime;
	private final float timeToWait;
	
	/**
	 * An event that simply wait for some time and then proceeds to the next event.
	 * @param timeToWait Waiting time in seconds.
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
}
