package de.homelab.madgaksha.cutscenesystem.event;

import java.util.Scanner;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.logging.Logger;

public class EventGameover extends ACutsceneEvent {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EventGameover.class);

	private boolean eventDone = false;

	public EventGameover() {
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
		GlobalBag.game.gameover();
		eventDone = true;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public boolean begin() {
		return true;
	}

	public void resetTimer() {
	}

	@Override
	public void reset() {
		eventDone = false;
	}

	@Override
	public void end() {
	}

	public static ACutsceneEvent readNextObject(Scanner s) {
		return new EventGameover();
	}
}
