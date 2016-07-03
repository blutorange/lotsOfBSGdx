package de.homelab.madgaksha.player.tokugi;

import com.badlogic.ashley.core.Entity;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EModel;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;

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

	@Override
	public EModel getModel() {
		throw new UnsupportedOperationException("TokugiNone cannot be a collectable item.");
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return null;
	}

	@Override
	public void fire(Entity player, float deltaTime) {
	}

	@Override
	protected long requestedRequiredScore() {
		return Long.MAX_VALUE;
	}

	@Override
	protected long requestedRemainingPainPoints() {
		return Long.MAX_VALUE;
	}

	@Override
	public boolean openFire(Entity player, float deltaTime) {
		return false;
	}

	@Override
	protected ETexture requestedSign() {
		return null;
	}

	@Override
	protected float requestedSignDelay() {
		return 0.0f;
	}
}
