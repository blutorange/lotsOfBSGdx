package de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang3.StringUtils;

import com.sun.media.sound.InvalidDataException;

import de.homelab.madgaksha.lotsofbs.bettersprite.drawable.ADrawable;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.event.EventFancyScene;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.Transient;

/**
 * Provides subclasses with access to the {@link ADrawable} for the key
 * given to the constructor via the the variable {@link #drawable}.
 * 
 * @author madgaksha
 *
 */
public abstract class AFancyWithDrawable extends AFancyEvent {
	/**
	 * Initial version.
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AFancyWithDrawable.class);

	/**
	 * Name of the drawable for this event. Must be non-null and non-empty.
	 * @serial
	 */
	private String key;
	@Transient protected ADrawable<?,?> drawable;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeUTF(key);
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		final String key = in.readUTF();
		if (key == null || key.isEmpty()) throw new InvalidDataException("key cannot be null or empty");
		this.key = key;
	}
	
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
