package de.homelab.madgaksha.cutscenesystem.fancyscene;

import org.apache.commons.lang3.StringUtils;

import de.homelab.madgaksha.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.cutscenesystem.FancyDrawable;
import de.homelab.madgaksha.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.logging.Logger;

/**
 * Provides subclasses with access to the {@link FancyDrawable} for the key
 * given to the constructor via the the variable {@link #drawable}.
 * 
 * @author madgaksha
 *
 */
public abstract class AFancyWithDrawable extends AFancyEvent {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AFancyWithDrawable.class);

	protected String key;
	protected FancyDrawable drawable;

	public AFancyWithDrawable(String key) {
		super(true);
		this.key = key;
	}

	@Override
	public void reset() {
		key = StringUtils.EMPTY;
		drawable = null;
		resetSubclass();
	}

	@Override
	public void attachedToScene(EventFancyScene scene) {
		drawable = scene.getDrawable(key);
	}

	protected abstract void resetSubclass();
}
