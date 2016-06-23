package de.homelab.madgaksha.player.tokugi;

import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.statusScreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.player.IMapItem;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public abstract class ATokugi implements IMapItem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ATokugi.class);

	private Sprite iconMain = null;
	private Sprite iconSub = null;
	private ESound soundOnAcquire;
	private ETokugi type = null;

	protected abstract ETexture requestedIconMain();

	protected abstract ETexture requestedIconSub();

	/**
	 * Can be overridden for custom sound when acquiring an item with a weapon
	 * of this type.
	 * 
	 * @return The sound played when the player acquires an item with this
	 *         weapon.
	 */
	protected ESound requestedSoundOnAcquire() {
		return ESound.ACQUIRE_WEAPON;
	}

	public ATokugi() {
	}

	void setType(ETokugi type) {
		this.type = type;
	}

	public ETokugi getType() {
		return type;
	}

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
		soundOnAcquire = requestedSoundOnAcquire();
		boolean success = ResourceCache.loadToRam(requestedRequiredResources());
		return (iconMain != null) && (iconSub != null) && (soundOnAcquire != null) && success;
	}

	@Override
	public void setup(Entity e, MapProperties props) {
	}

	/**
	 * Can be overridden for other values.
	 * 
	 * @return The default axis of rotation around which the item model rotates.
	 */
	@Override
	public Vector3 getDefaultAxisOfRotation() {
		return new Vector3(1.0f, 1.0f, 0.0f);
	}

	/**
	 * Can be overridden for other values.
	 * 
	 * @return The angular velocity at which the item model rotates.
	 */
	@Override
	public float getDefaultAngularVelocity() {
		return 45.0f;
	}

	@Override
	public boolean isSupportedByPlayer(APlayer player) {
		return type == null ? true : player.supportsTokugi(type);
	}

	/**
	 * Can be overridden for other values.
	 * 
	 * @see IMapItem#getActivationAreaScaleFactor()
	 */
	@Override
	public float getActivationAreaScaleFactor() {
		return 3.5f;
	}

	@Override
	public void gotItem() {
		SoundPlayer.getInstance().play(soundOnAcquire);
		player.learnTokugi(this);
		statusScreen.updateWeaponAndTokugiLayout();
	}
}
