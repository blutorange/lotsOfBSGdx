package de.homelab.madgaksha.cutscenesystem;

import de.homelab.madgaksha.logging.Logger;

public abstract class ACutsceneEvent {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ACutsceneEvent.class);
	
	/** @return Whether this cutscene event has finished and we may proceed to the next one */
	public abstract boolean isFinished();
	
	/**
	 * Called when this cutscene events needs to be rendered to the screen.
	 * @param deltaTime Time in seconds since the last call to render.
	 */
	public abstract void render();

	/**
	 * Called when this cutscene events needs to be updated.
	 * @param deltaTime Time in seconds since the last call to render.
	 */
	public abstract void update(float deltaTime);

	public abstract void resize(int width, int height);

	/**
	 * Called once when this cutscene starts.
	 * 
	 * @return Whether an error occurred and the cutscene event could not start.
	 *         If it could not start, this cutscene event will be skipped.
	 */
	public abstract boolean begin();
}
