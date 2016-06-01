package de.homelab.madgaksha.player.weapon;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.player.EWeapon;
import de.homelab.madgaksha.player.IMapItem;
import de.homelab.madgaksha.resourcecache.ETexture;

public abstract class AWeapon implements IMapItem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AWeapon.class);
	
	private Sprite iconMain = null;
	private Sprite iconSub = null;
	private EWeapon type = null;
	
	public AWeapon() {
	}
	
	public void setType(EWeapon type) {
		this.type = type;
	}
	
	protected abstract ETexture requestedIconMain();
	protected abstract ETexture requestedIconSub();
	
	public Sprite getIconMain() {
		return iconMain;
	}

	public Sprite getIconSub() {
		return iconSub;
	}

	@Override
	public boolean initialize() {
		iconMain = requestedIconMain().asSprite();
		iconSub = requestedIconSub().asSprite();
		return (iconMain != null) && (iconSub != null) && (iconSub != null);
	}

	@Override
	public void setup(Entity e) {			
	}

	/**
	 * Can be overridden for other values.
	 * @return The default axis of rotation around which the item model rotates.
	 */
	@Override
	public Vector3 getDefaultAxisOfRotation() {
		return new Vector3(1.0f,1.0f,0.0f);
	}

	/** Can be overridden for other values.
	 * @return The angular velocity at which the item model rotates.
	 */
	@Override
	public float getDefaultAngularVelocity() {
		return 45.0f;
	}
	
	@Override
	public boolean isSupportedByPlayer(APlayer player) {
		return type == null ? true : player.supportsWeapon(type);
	}
}
