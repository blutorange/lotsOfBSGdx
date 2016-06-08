package de.homelab.madgaksha.cutscenesystem.provider;

import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.layer.CutsceneLayer;

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
	 * @param n Number of cutscene events that have finished since the cutscene started.
	 * @return The next cutscene event about to be started.
	 */
	public ACutsceneEvent nextCutsceneEvent(int i);
	
	public void initialize();
	
	public void end();
}
