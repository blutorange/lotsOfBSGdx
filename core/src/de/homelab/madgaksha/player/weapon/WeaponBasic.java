package de.homelab.madgaksha.player.weapon;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EModel;
import de.homelab.madgaksha.resourcecache.ETexture;

public class WeaponBasic extends AWeapon {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(WeaponBasic.class);

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.WEAPON_BASIC_ICON_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.WEAPON_BASIC_ICON_SUB;
	}

	@Override
	public EModel getModel() {
		return EModel.ITEM_WEAPON_BASIC;
	}

}
