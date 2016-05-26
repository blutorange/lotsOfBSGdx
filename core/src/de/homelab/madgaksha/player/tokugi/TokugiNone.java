package de.homelab.madgaksha.player.tokugi;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;

public class TokugiNone extends ATokugi {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TokugiNone.class);

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.TOKUGI_NONE_ICON_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.TOKUGI_NONE_ICON_SUB;
	}
}
