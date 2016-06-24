package de.homelab.madgaksha.player.weapon;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.homelab.madgaksha.logging.Logger;

public enum EWeapon {
	NONE(WeaponNone.class),
	BASIC(WeaponBasic.class),
	MULTI(WeaponMulti.class)
	;

	private final static Logger LOG = Logger.getLogger(EWeapon.class);

	private final Class<? extends AWeapon> clazz;

	private EWeapon(Class<? extends AWeapon> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Gets the weapon object for this weapon type.
	 * 
	 * @return The weapon object for this weapon type. Null if it could not be
	 *         loaded.
	 */
	public AWeapon getWeapon() {
		AWeapon weapon;
		try {
			weapon = (AWeapon) ClassReflection.getConstructor(clazz).newInstance();
			weapon.setType(this);
			return weapon.initialize() ? weapon : null;
		} catch (ReflectionException e) {
			LOG.error("could not initialize weapon: " + this, e);
			return null;
		}
	}
}
