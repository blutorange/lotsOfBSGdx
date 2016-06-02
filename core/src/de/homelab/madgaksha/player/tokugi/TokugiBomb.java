package de.homelab.madgaksha.player.tokugi;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EModel;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;

public class TokugiBomb extends ATokugi {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TokugiBomb.class);

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.TOKUGI_NONE_ICON_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.TOKUGI_NONE_ICON_SUB;
	}

	@Override
	public EModel getModel() {
		return EModel.ITEM_WEAPON_BASIC;
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		// TODO Auto-generated method stub
		return null;
	}
}
