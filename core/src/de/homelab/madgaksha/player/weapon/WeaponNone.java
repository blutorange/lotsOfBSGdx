package de.homelab.madgaksha.player.weapon;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;

public class WeaponNone extends AWeapon {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(WeaponNone.class);

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.WEAPON_NONE_ICON_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.WEAPON_NONE_ICON_SUB;
	}

}
