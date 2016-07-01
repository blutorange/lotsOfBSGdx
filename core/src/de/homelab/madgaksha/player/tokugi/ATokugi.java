package de.homelab.madgaksha.player.tokugi;

import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.gameScore;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.playerHitCircleEntity;
import static de.homelab.madgaksha.GlobalBag.statusScreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector3;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.entity.ITimedCallback;
import de.homelab.madgaksha.entityengine.entity.MakerUtils;
import de.homelab.madgaksha.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.APlayer;
import de.homelab.madgaksha.player.IMapItem;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public abstract class ATokugi implements IMapItem {
	private final static Logger LOG = Logger.getLogger(ATokugi.class);

	private Sprite iconMain = null;
	private Sprite iconSub = null;
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

	public ATokugi() {
	}

	void setType(ETokugi type) {
		this.type = type;
	}

	public ETokugi getType() {
		return type;
	}
	
	public ETexture getSign() {
		return sign;
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
		statusScreen.updateWeaponAndTokugiLayout();
	}
	
	protected void dealFinalDamagePoint() {
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
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
				gameScore.decreaseBy(requestedRequiredScore());
				MakerUtils.addTimedRunnable(requestedSignDelay(), new ITimedCallback() {					
					@Override
					public void run(Entity entity, Object data) {
						statusScreen.addTokugiDisplay(tokugi);
					}
				});
	
				ComponentUtils.dealDamage(null, GlobalBag.playerHitCircleEntity, requestedRemainingPainPoints(), false);
				openFire(player, deltaTime);
			}
		}
	}
	
	public abstract void openFire(Entity player, float deltaTime);

}
