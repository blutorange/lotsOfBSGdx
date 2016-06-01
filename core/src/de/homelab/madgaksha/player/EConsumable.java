package de.homelab.madgaksha.player;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.consumable.AConsumable;

public enum EConsumable {
	;

	private final static Logger LOG = Logger.getLogger(EConsumable.class);
	private final Class<? extends AConsumable> clazz;
		
	private EConsumable(Class<? extends AConsumable> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Gets the weapon object for this consumable type.
	 * @return The consumable object for this consumable type. Null if it could not be loaded.
	 */
	public AConsumable getConsumable() {
		AConsumable consumable;
		try {
			consumable = (AConsumable) ClassReflection.getConstructor(clazz).newInstance();
			consumable.setType(this);
			return consumable.initialize() ? consumable : null;
		} catch (ReflectionException e) {
			LOG.error("could not initialize consumable: " + this, e);
			return null;
		}
	}
}
