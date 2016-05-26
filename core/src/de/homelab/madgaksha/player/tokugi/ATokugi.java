package de.homelab.madgaksha.player.tokugi;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;

public abstract class ATokugi {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ATokugi.class);
	
	private Sprite iconMain = null;
	private Sprite iconSub = null;
	
	public ATokugi() {
	}
	
	protected abstract ETexture requestedIconMain();
	protected abstract ETexture requestedIconSub();
	
	public Sprite getIconMain() {
		return iconMain;
	}

	public Sprite getIconSub() {
		return iconSub;
	}


	public boolean initialize() {
		iconMain = requestedIconMain().asSprite();
		iconSub = requestedIconSub().asSprite();
		return (iconMain != null) && (iconSub != null);
	}
}
