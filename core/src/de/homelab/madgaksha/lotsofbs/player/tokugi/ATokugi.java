package de.homelab.madgaksha.lotsofbs.player.tokugi;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.battleModeActive;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameScore;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.statusScreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableSprite;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.ITimedCallback;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.MakerUtils;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.player.APlayer;
import de.homelab.madgaksha.lotsofbs.player.IMapItem;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

public abstract class ATokugi implements IMapItem {
	private final static Logger LOG = Logger.getLogger(ATokugi.class);

	private CroppableSprite iconMain = null;
	private CroppableSprite iconSub = null;
	private ESound soundOnAcquire;
	private ETokugi type = null;
	private ETexture sign;

	protected abstract ETexture requestedIconMain();

	protected abstract ETexture requestedIconSub();

	protected abstract long requestedRequiredScore();

	protected abstract ETexture requestedSign();

	protected abstract float requestedSignDelay();

	protected abstract long requestedRemainingPainPoints();

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

	void setType(ETokugi type) throws IllegalStateException {
		if (this.type != null) throw new IllegalStateException();
		this.type = type;
	}

	public ETokugi getType() {
		return type;
	}

	public ETexture getSign() {
		return sign;
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
		sign = requestedSign();
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
	}

	protected void dealFinalDamagePoint() {
		if (!isSomethingTargetted()) {
			LOG.error("no such target");
			return;
		}
		Entity target = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);
		ComponentUtils.dealDamage(null, target, 10, false);
	}

	public void fire(Entity player, float deltaTime) {
		if (gameScore.getScore() >= requestedRequiredScore()) {
			PainPointsComponent ppc = Mapper.painPointsComponent.get(playerHitCircleEntity);
			DamageQueueComponent dqc = Mapper.damageQueueComponent.get(playerHitCircleEntity);
			long queuedDamage = dqc == null ? 0L : dqc.queuedDamage;
			final ATokugi tokugi = this;
			if (ppc != null && (ppc.maxPainPoints - ppc.painPoints - queuedDamage) > requestedRemainingPainPoints()) {
				if (!openFire(player, deltaTime))
					return;
				gameScore.decreaseBy(requestedRequiredScore());
				MakerUtils.addTimedRunnable(requestedSignDelay(), new ITimedCallback() {
					@Override
					public void run(Entity entity, Object data) {
						statusScreen.addTokugiSign(tokugi);
					}
				});
				ComponentUtils.dealDamage(null, GlobalBag.playerHitCircleEntity, requestedRemainingPainPoints(), false);
			}
		}
	}

	protected boolean isSomethingTargetted() {
		return battleModeActive && cameraTrackingComponent.trackedPointIndex < cameraTrackingComponent.focusPoints.size();
	}
	
	/**
	 * Fires the tokugi. May return false when tokugi cannot be used right now.
	 * 
	 * @param player
	 *            Player entity.
	 * @param deltaTime
	 *            Time since the last frame in seconds.
	 * @return Whether the tokugi has been fired.
	 */
	public abstract boolean openFire(Entity player, float deltaTime);

}
