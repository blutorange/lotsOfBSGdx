package de.homelab.madgaksha.player.weapon;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;

public abstract class AWeapon {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AWeapon.class);
	
	private Sprite iconMain = null;
	private Sprite iconSub = null;
	
	public AWeapon() {
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
		return (iconMain != null) && (iconSub != null) && (iconSub != null);
	}
}
