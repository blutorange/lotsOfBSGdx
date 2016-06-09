package de.homelab.madgaksha.layer;

import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.provider.CutsceneEventProvider;
import de.homelab.madgaksha.entityengine.entityutils.SystemUtils;
import de.homelab.madgaksha.logging.Logger;

/**
 * Takes care of handling a sequence of textboxes. Useful for short story sequences.
 * 
 * It draws them and advances to the next box upon pressing a key.
 * 
 * @author madgaksha
 *
 */
public class CutsceneLayer extends ALayer {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CutsceneLayer.class);
	
	private final CutsceneEventProvider cutsceneEventProvider;
	private ACutsceneEvent currentCutsceneEvent;
	private int cutsceneCount = -1;
	
	/**
	 * Adds a new layer with a set of cutscenes. 
	 * @param cutsceneEventList List of cutscenes.
	 */
	public CutsceneLayer(CutsceneEventProvider cutsceneEventProvider) {
		this.cutsceneEventProvider = cutsceneEventProvider;
	}
	
	@Override
	public void draw(float deltaTime) {
		if (currentCutsceneEvent != null) currentCutsceneEvent.render();
	}

	@Override
	public void update(float deltaTime) {
		if (currentCutsceneEvent == null) return;
		
		// Check if current event is finished and proceed to next event.
		if (currentCutsceneEvent.isFinished()) {
			if (!proceedToNextEvent()) return;
		}

		// Update event.
		currentCutsceneEvent.update(deltaTime);
	}
	
	@Override
	public void removedFromStack() {
		SystemUtils.enableAction();
		currentCutsceneEvent = null;		
		cutsceneEventProvider.end();
	}

	@Override
	public void addedToStack() {
		SystemUtils.disableActionExceptGrantPosition();		
		cutsceneEventProvider.initialize();
		proceedToNextEvent();
	}
	
	public boolean proceedToNextEvent() {
		do {
			if (currentCutsceneEvent != null) cutsceneEventProvider.eventDone(currentCutsceneEvent);
			currentCutsceneEvent = cutsceneEventProvider.nextCutsceneEvent(++cutsceneCount);
			if (currentCutsceneEvent == null) {
				removeSelf();
				return false;
			}
		} while (!currentCutsceneEvent.begin());
		return true;
	}

	@Override
	public boolean isBlockDraw() {
		return false;
	}

	@Override
	public boolean isBlockUpdate() {
		return false;
	}
	
	@Override
	public void resize(int width, int height) {
		if (currentCutsceneEvent != null) currentCutsceneEvent.resize(width, height);
	}
}
