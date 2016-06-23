package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.utils.Pool;

import de.homelab.madgaksha.cutscenesystem.event.EventTextbox;
import de.homelab.madgaksha.logging.Logger;

public class EventTextboxPool extends Pool<EventTextbox> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EventTextboxPool.class);

	private final static int INITIAL_SIZE = 10;
	private final static int MAX_SIZE = 200;

	// Singleton
	private static class SingletonHolder {
		private static final EventTextboxPool INSTANCE = new EventTextboxPool();
	}

	public static EventTextboxPool getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private EventTextboxPool() {
		super(INITIAL_SIZE, MAX_SIZE);
	}

	@Override
	protected EventTextbox newObject() {
		return new EventTextbox();
	}

	// Enforce reset gets called.
	@Override
	protected void reset(EventTextbox box) {
		box.reset();
	}
}
