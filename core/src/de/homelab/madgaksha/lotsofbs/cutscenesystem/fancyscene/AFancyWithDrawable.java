package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import org.apache.commons.lang3.StringUtils;

import de.homelab.madgaksha.lotsofbs.bettersprite.drawable.ADrawable;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Provides subclasses with access to the {@link ADrawable} for the key
 * given to the constructor via the the variable {@link #drawable}.
 * 
 * @author madgaksha
 *
 */
public abstract class AFancyWithDrawable extends AFancyEvent {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AFancyWithDrawable.class);

	private String key;
	protected ADrawable<?,?> drawable;

	public AFancyWithDrawable(String key) {
		super(true);
		this.key = key;
	}

	@Override
	public final boolean begin(EventFancyScene scene) {
		drawable = scene.getADrawable(key);
		return beginSubclass(scene);
	}
	
	@Override
	public final void reset() {
		key = StringUtils.EMPTY;
		drawable = null;
		resetSubclass();
	}

	@Override
	public final void attachedToScene(EventFancyScene scene) {
		drawable = scene.getADrawable(key);
	}
	
	@Override
	public final void drawableChanged(EventFancyScene scene, String changedKey) {
		if (this.key.equals(changedKey)) drawable = scene.getADrawable(key);
	}

	protected abstract void resetSubclass();
	protected abstract boolean beginSubclass(EventFancyScene scene);
}
