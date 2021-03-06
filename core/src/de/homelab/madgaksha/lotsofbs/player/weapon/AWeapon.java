package de.homelab.madgaksha.lotsofbs.player.weapon;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableSprite;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.AItem;
import de.homelab.madgaksha.lotsofbs.player.APlayer;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

public abstract class AWeapon extends AItem {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AWeapon.class);

	private ESound soundOnAcquire;
	private CroppableSprite iconMain = null;
	private CroppableSprite iconSub = null;
	private EWeapon type = null;

	void setType(final EWeapon type) throws IllegalStateException {
		if (this.type != null) throw new IllegalStateException();
		this.type = type;
	}

	public EWeapon getType() {
		return type;
	}

	protected abstract ETexture requestedIconMain();

	protected abstract ETexture requestedIconSub();

	/**
	 * Can be overriden for custom sound when acquiring an item with a weapon of
	 * this type.
	 *
	 * @return The sound played when the player acquires an item with this
	 *         weapon.
	 */
	protected ESound requestedSoundOnAcquire() {
		return ESound.ACQUIRE_WEAPON;
	}

	public CroppableSprite getIconMain() {
		return iconMain;
	}

	public CroppableSprite getIconSub() {
		return iconSub;
	}

	@Override
	public boolean initialize() {
		iconMain = requestedIconMain().asSprite();
		iconSub = requestedIconSub().asSprite();
		soundOnAcquire = requestedSoundOnAcquire();
		final boolean success = ResourceCache.loadToRam(requestedRequiredResources());
		return (iconMain != null) && (iconSub != null) && success;
	}

	@Override
	public void setup(final Entity e, final MapProperties props, final ComponentQueueComponent queueForAcquisition) {
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
	public boolean isSupportedByPlayer(final APlayer player) {
		return type == null ? true : player.supportsWeapon(type);
	}

	@Override
	public void gotItem() {
		SoundPlayer.getInstance().play(soundOnAcquire);
		player.obtainWeapon(this);
	}

	public abstract void fire(Entity player, float deltaTime);
}
