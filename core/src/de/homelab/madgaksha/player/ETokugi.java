package de.homelab.madgaksha.player;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.tokugi.ATokugi;
import de.homelab.madgaksha.player.tokugi.TokugiNone;

public enum ETokugi {
	NONE(TokugiNone.class),
	;

	private final static Logger LOG = Logger.getLogger(ETokugi.class);
	
	private final Class<? extends ATokugi> clazz;
	
	private ETokugi(Class<? extends ATokugi> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Gets the weapon object for this weapon type.
	 * @return The weapon object for this weapon type. Null if it could not be loaded.
	 */
	public ATokugi getTokugi() {
		ATokugi tokugi;
		try {
			tokugi = (ATokugi) ClassReflection.getConstructor(clazz).newInstance();
			return tokugi.initialize() ? tokugi : null;
		} catch (ReflectionException e) {
			LOG.error("could not initialize tokugi: " + this, e);
			return null;
		}
	}
}
