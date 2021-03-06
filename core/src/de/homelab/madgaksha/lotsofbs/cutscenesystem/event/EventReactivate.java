package de.homelab.madgaksha.lotsofbs.cutscenesystem.event;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.idEntityMap;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.ACutsceneEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.CallbackComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TimedCallbackComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.CallbackMaker;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

public class EventReactivate extends ACutsceneEvent {
	private final static Logger LOG = Logger.getLogger(EventReactivate.class);

	private String guid = StringUtils.EMPTY;
	private Entity entity = null;
	private boolean eventDone = false;

	/**
	 * An event that simply wait for some time and then proceeds to the next
	 * event.
	 * 
	 * @param timeToWait
	 *            Waiting time in seconds.
	 */
	public EventReactivate(String guid) {
		this.guid = guid;
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
		if (!eventDone) {
			EventReactivate.reactivateEvent(entity);
			// final TimedCallbackComponent tcc =
			// gameEntityEngine.createComponent(TimedCallbackComponent.class);
			// final ComponentQueueComponent cqc =
			// gameEntityEngine.createComponent(ComponentQueueComponent.class);
			//
			// tcc.setup(CallbackMaker.LOOP_CALLBACK_ONCE, 0, -1);
			// cqc.remove.add(InactiveComponent.class);
			//
			// entity.add(tcc);
			// entity.add(cqc);

			eventDone = true;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public boolean begin() {
		entity = idEntityMap.get(guid);
		if (entity != null) {
			final CallbackComponent cc = Mapper.callbackComponent.get(entity);
			if (cc == null) {
				LOG.error("cannot reactivate callback: not a callback entity");
				return false;
			}
		}
		return entity != null;
	}

	@Override
	public void reset() {
		guid = StringUtils.EMPTY;
		entity = null;
	}

	@Override
	public void end() {
	}

	/**
	 * @param s Scanner from which to read.
	 * @param fh The file handle of the file being used. Should be used only for directories.
	 */
	public static ACutsceneEvent readNextObject(Scanner s, FileHandle fh) {
		String guid = FileCutsceneProvider.readNextGuid(s);
		if (guid == null) {
			LOG.error("expected guid");
			return null;
		}
		return new EventReactivate(guid);
	}

	public static void reactivateEvent(String eventGuid) {
		reactivateEvent(idEntityMap.get(eventGuid));
	}

	public static void reactivateEvent(Entity entity) {
		if (entity != null) {
			final TimedCallbackComponent tcc = gameEntityEngine.createComponent(TimedCallbackComponent.class);
			final ComponentQueueComponent cqc = gameEntityEngine.createComponent(ComponentQueueComponent.class);

			tcc.setup(CallbackMaker.LOOP_CALLBACK_ONCE, 0, -1);
			cqc.remove.add(InactiveComponent.class);

			entity.add(tcc);
			entity.add(cqc);
		}
	}
}
