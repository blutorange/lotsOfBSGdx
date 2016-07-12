package de.homelab.madgaksha.lotsofbs.player.weapon;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EModel;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;

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

	@Override
	public EModel getModel() {
		throw new UnsupportedOperationException("WeaponNone cannot be a collectable item.");
	}

	@Override
	public void fire(Entity player, float deltaTime) {
		// No weapon can do no fire...
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		// TODO Auto-generated method stub
		return null;
	}

}
