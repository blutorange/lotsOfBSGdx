package de.homelab.madgaksha.layer;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.EntitySystem;

import de.homelab.madgaksha.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.cutscenesystem.CutsceneEventProvider;
import de.homelab.madgaksha.entityengine.entitysystem.AiSystem;
import de.homelab.madgaksha.entityengine.entitysystem.InputPlayerDesktopSystem;
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
		gameEntityEngine.getSystem(AiSystem.class).setProcessing(true);
		final EntitySystem inputDesktop = gameEntityEngine.getSystem(InputPlayerDesktopSystem.class);
		if (inputDesktop != null) inputDesktop.setProcessing(true);

		currentCutsceneEvent = null;
	}

	@Override
	public void addedToStack() {
		gameEntityEngine.getSystem(AiSystem.class).setProcessing(false);
		final EntitySystem inputDesktop = gameEntityEngine.getSystem(InputPlayerDesktopSystem.class);
		if (inputDesktop != null) inputDesktop.setProcessing(false);
		
		cutsceneEventProvider.initialize();
		proceedToNextEvent();
	}
	
	public boolean proceedToNextEvent() {
		do {
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
