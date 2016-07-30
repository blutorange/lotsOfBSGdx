package de.homelab.madgaksha.lotsofbs.cutscenesystem.event;

import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

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
	public void update(float deltaTime, boolean allowSpeedup) {
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

	/**
	 * @param s Scanner from which to read.
	 * @param fh The file handle of the file being used. Should be used only for directories.
	 */
	public static ACutsceneEvent readNextObject(Scanner s, FileHandle fh) {
		return new EventGameover();
	}
}
