package de.homelab.madgaksha.lotsofbs.cutscenesystem.provider;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.layer.CutsceneLayer;

/**
 * For loading cutscene events dynamically.
 * 
 * @author madgaksha
 * @see {@link CutsceneLayer}
 *
 */
public interface CutsceneEventProvider {
	/**
	 * Retrieves the next cutscene event.
	 *
	 * @param n
	 *            Number of cutscene events that have finished since the
	 *            cutscene started.
	 * @return The next cutscene event about to be started.
	 */
	public ACutsceneEvent nextCutsceneEvent(int n);

	/**
	 * Called when the cutscene layer is added to the stack and this provider
	 * must finish all necessary setup.
	 */
	public void initialize();

	/**
	 * Called when the cutscene layer is removed from the the stack and this
	 * provider must finish all necessary cleanup.
	 */
	public void end();

	/**
	 * Called when the cutscene event has finished is not needed any longer.
	 * Must call any required cleanup on cutscene events. If this provider
	 * caches cutscene events, eg. because they are reused, cleanup must be done
	 * during {@link #end()}.
	 * 
	 * @param currentCutsceneEvent
	 *            The cutscene event that has just finished.
	 */
	public abstract void eventDone(ACutsceneEvent currentCutsceneEvent);
}
