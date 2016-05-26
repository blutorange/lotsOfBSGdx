package de.homelab.madgaksha.player.weapon;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;

public abstract class AWeapon {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AWeapon.class);
	
	private Sprite iconMain = null;
	private Sprite iconSubVertical = null;
	private Sprite iconSubHorizontal = null;
	
	public AWeapon() {
	}
	
	protected abstract ETexture requestedIconMain();
	protected abstract ETexture requestedIconSubHorizontal();
	protected abstract ETexture requestedIconSubVertical();
	
	public Sprite getIconMain() {
		return iconMain;
	}

	public Sprite getIconSubVertical() {
		return iconSubVertical;
	}

	public Sprite getIconSubHorizontal() {
		return iconSubHorizontal;
	}

	public boolean initialize() {
		iconMain = requestedIconMain().asSprite();
		iconSubHorizontal = requestedIconSubHorizontal().asSprite();
		iconSubVertical = requestedIconSubVertical().asSprite();
		return (iconMain != null) && (iconSubHorizontal != null) && (iconSubVertical != null);
	}
}
