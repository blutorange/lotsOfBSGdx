package de.homelab.madgaksha.lotsofbs.layer;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.KeyMapDesktop;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.CutsceneEventProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.SystemUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Takes care of handling a sequence of textboxes. Useful for short story
 * sequences.
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
	private Runnable callbackOnDone;
	private boolean allowSpeedup = false;

	/**
	 * Adds a new layer with a set of cutscenes.
	 * 
	 * @param cutsceneEventList
	 *            List of cutscenes.
	 */
	public CutsceneLayer(CutsceneEventProvider cutsceneEventProvider) {
		this.cutsceneEventProvider = cutsceneEventProvider;
	}

	public CutsceneLayer(CutsceneEventProvider cutsceneEventProvider, Runnable callbackOnDone) {
		this.cutsceneEventProvider = cutsceneEventProvider;
		this.callbackOnDone = callbackOnDone;
	}

	@Override
	public void draw(float deltaTime) {
		if (currentCutsceneEvent != null)
			currentCutsceneEvent.render();
	}

	@Override
	public void update(float deltaTime) {
		allowSpeedup = allowSpeedup || !KeyMapDesktop.isSpeedupPressed();
		if (currentCutsceneEvent == null)
			return;

		// Check if current event is finished and proceed to next event.
		if (currentCutsceneEvent.isFinished()) {
			if (!proceedToNextEvent())
				return;
		}

		// Update event.
		currentCutsceneEvent.update(deltaTime, allowSpeedup && KeyMapDesktop.isSpeedupPressed());
	}

	@Override
	public void removedFromStack() {
		SystemUtils.enableAction();
		currentCutsceneEvent = null;
		cutsceneEventProvider.end();
		if (callbackOnDone != null)
			callbackOnDone.run();
	}

	@Override
	public void addedToStack() {
		SystemUtils.disableAction();
		stopPlayerMovement();
		cutsceneEventProvider.initialize();
		allowSpeedup = !KeyMapDesktop.isSpeedupPressed();
		proceedToNextEvent();
	}

	private void stopPlayerMovement() {
		VelocityComponent vc = Mapper.velocityComponent.get(GlobalBag.playerEntity);
		vc.x = vc.y = 0;
	}

	public boolean proceedToNextEvent() {
		do {
			if (currentCutsceneEvent != null)
				cutsceneEventProvider.eventDone(currentCutsceneEvent);
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
		if (currentCutsceneEvent != null)
			currentCutsceneEvent.resize(width, height);
	}
}
