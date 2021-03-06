package de.homelab.madgaksha.lotsofbs.player;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.statusScreen;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ConeDistributionComponent.ConeDistributionParameters;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.DamageSystem;
import de.homelab.madgaksha.lotsofbs.player.consumable.EConsumable;
import de.homelab.madgaksha.lotsofbs.player.tokugi.ATokugi;
import de.homelab.madgaksha.lotsofbs.player.tokugi.ETokugi;
import de.homelab.madgaksha.lotsofbs.player.tokugi.TokugiNone;
import de.homelab.madgaksha.lotsofbs.player.weapon.AWeapon;
import de.homelab.madgaksha.lotsofbs.player.weapon.EWeapon;
import de.homelab.madgaksha.lotsofbs.player.weapon.WeaponNone;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimationList;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;

public abstract class APlayer {

	private final EAnimationList normalAnimationList;
	private final EAnimationList battleAnimationList;
	private final EAnimationList damageAnimationList;
	private final EAnimationList deathAnimationList;
	private final float movementAccelerationFactorLow;
	private final float movementAccelerationFactorHigh;
	private final float movementFrictionFactor;
	private final float movementBattleSpeedLow;
	private final float movementBattleSpeedHigh;
	private final Color painBarColorLow = new Color();
	private final Color painBarColorMid = new Color();
	private final Color painBarColorHigh = new Color();

	/**
	 * x,y are the offset relative to the center of the sprites (bottom left).
	 * Radius is the circe's size.
	 */
	private final Shape2D exactShapeCollision;
	private final Circle boundingCircle;
	private final Rectangle boundingBoxMap;
	private final Rectangle boundingBoxRender;
	private final Rectangle boundingBoxCollision;
	private final Vector2 offsetPlayerToHitCircle;
	private final Vector2 spriteOrigin;
	/**
	 * x,y are the offset relative to the center of the sprites (bottom left).
	 * Width and height are the dimensions.
	 */
	private final IResource<? extends Enum<?>, ?>[] requiredResources;

	/** Maximum number of items this player can hold at once, including the items
	 * that is currently active. */
	private final int maximumHoldableItems;
	/** Player maximum paint points. */
	private final long maxPainPoints;
	/** Bullet attack power. Default 1.0 */
	private final float bulletAttack;
	/** Bullet resistance. Default 1.0 */
	private final float bulletResistance;

	private final float battleStigmaAngularVelocity;

	/**
	 * The texture used as the player's hit circle / point. It is placed at the
	 * center of the player's hit box.
	 * 
	 * @see #requestedBoundingBoxCollision()
	 */
	private final ETexture hitCircleTexture;
	private final ETexture battleStigmaTexture;

	private final Color battleStigmaColorWhenHit;

	private final ConeDistributionParameters itemCircleParameters;
	
	private final ESound voiceOnBattleModeStart;
	private final ESound voiceOnBattleModeEnd;
	private final ESound voiceOnBattleModeFlee;
	private final ESound voiceOnEnemyKilled;
	private final ESound voiceOnConsumableUse;
	private final ESound voiceOnHeavyDamage;
	private final ESound voiceOnLightDamage;
	private final ESound voiceOnLightHeal;
	private final ESound voiceOnHeavyHeal;
	private final ESound voiceOnDeath;

	private final EParticleEffect particleEffectOnDeath;

	private final EnumSet<EWeapon> supportedWeaponSet = EnumSet.of(EWeapon.NONE);
	private final List<AWeapon> obtainedWeaponList = new ArrayList<AWeapon>();

	private final EnumSet<ETokugi> supportedTokugiSet = EnumSet.of(ETokugi.NONE);
	private final List<ATokugi> obtainedTokugiList = new ArrayList<ATokugi>();

	private final EnumSet<EConsumable> supportedConsumableSet = EnumSet.noneOf(EConsumable.class);

	private final EParticleEffect battleModeEnterParticleEffect;
	private final EParticleEffect battleModeExitParticleEffect;

	/** Current weapon equipped. */
	private AWeapon currentWeapon = null;

	/** The current tokugi. */
	private ATokugi currentTokugi = null;

	public APlayer() {
		this.maximumHoldableItems = requestedMaximumHoldableItems();
		this.itemCircleParameters = requestedItemCircleParamters();
		this.movementAccelerationFactorLow = requestedMovementAccelerationFactorLow();
		this.movementAccelerationFactorHigh = requestedMovementAccelerationFactorHigh();
		this.movementFrictionFactor = requestedMovementFrictionFactor();
		this.movementBattleSpeedLow = requestedMovementBattleSpeedLow();
		this.movementBattleSpeedHigh = requestedMovementBattleSpeedHigh();
		this.normalAnimationList = requestedNormalAnimationList();
		this.battleAnimationList = requestedBattleAnimationList();
		this.damageAnimationList = requestedDamageAnimationList();
		this.deathAnimationList = requestedDeathAnimationList();
		this.spriteOrigin = requestedSpriteOrigin();
		this.offsetPlayerToHitCircle = requestedOffsetPlayerToHitCircle();
		this.boundingCircle = requestedBoundingCircle();
		this.boundingBoxMap = requestedBoundingBoxMap();
		this.boundingBoxRender = requestedBoundingBoxRender();
		this.boundingBoxCollision = requestedBoundingBoxCollision();
		this.exactShapeCollision = requestedExactShapeCollision();
		this.painBarColorLow.set(requestedPainBarColorLow());
		this.painBarColorMid.set(requestedPainBarColorMid());
		this.painBarColorHigh.set(requestedPainBarColorHigh());
		this.maxPainPoints = MathUtils.clamp(requestedMaxPainPoints(), 1L, DamageSystem.MAX_PAIN_POINTS);
		this.bulletAttack = requestedBulletAttack();
		this.bulletResistance = requestedBulletResistance();
		this.hitCircleTexture = requestedHitCircleTexture();
		this.battleStigmaTexture = requestedBattleStigmaTexture();
		this.battleStigmaAngularVelocity = requestedBattleStigmaAngularVelocity();
		this.voiceOnBattleModeStart = requestedVoiceOnBattleModeStart();
		this.voiceOnBattleModeEnd = requestedVoiceOnBattleModeEnd();
		this.voiceOnBattleModeFlee = requestedVoiceOnBattleModeFlee();
		this.voiceOnEnemyKilled = requestedVoiceOnEnemyKilled();
		this.voiceOnConsumableUse = requestedVoiceOnConsumableUse();
		this.voiceOnLightDamage = requestedVoiceOnLightDamage();
		this.voiceOnHeavyDamage = requestedVoiceOnHeavyDamage();
		this.voiceOnLightHeal = requestedVoiceOnLightHeal();
		this.voiceOnHeavyHeal = requestedVoiceOnHeavyHeal();
		this.voiceOnDeath = requestedVoiceOnDeath();
		this.battleStigmaColorWhenHit = requestedBattleStigmaColorWhenHit();
		this.particleEffectOnDeath = requestedParticleEffectOnDeath();
		this.battleModeEnterParticleEffect = requestedBattleModeEnterParticleEffect();
		this.battleModeExitParticleEffect = requestedBattleModeExitParticleEffect();

		// Make sure subclasses cannot remove EWeapon.NONE
		EWeapon[] ew = requestedSupportedWeapons();
		if (ew != null)
			for (EWeapon w : ew)
				supportedWeaponSet.add(w);

		// Make sure subclasses cannot remove ETokugi.NONE
		ETokugi[] et = requestedSupportedTokugi();
		if (et != null)
			for (ETokugi t : et)
				supportedTokugiSet.add(t);

		EConsumable[] ec = requestedSupportedConsumable();
		if (ec != null)
			for (EConsumable c : ec)
				supportedConsumableSet.add(c);

		IResource<?, ?> customResources[] = requestedRequiredResources();
		IResource<?, ?> additionalResources[] = new IResource<?, ?>[] {
				EParticleEffect.DEFAULT_PLAYER_DEATH.getTextureAtlas(),
				EParticleEffect.ALL_MY_ITEM_ARE_BELONG_TO_ME.getTextureAtlas() };
		this.requiredResources = ArrayUtils.addAll(customResources, additionalResources);
	}

	// ====================================
	// Abstract methods
	// ====================================
	/** @return The animated sprite used for the player. */
	protected abstract EAnimationList requestedNormalAnimationList();

	/** @return The animated sprite used for the player during battle. */
	protected abstract EAnimationList requestedBattleAnimationList();

	/** @return The animated sprite used for the player when taking damage. */
	protected abstract EAnimationList requestedDamageAnimationList();

	/** @return The animated sprite used for the player when dead. */
	protected abstract EAnimationList requestedDeathAnimationList();

	/** @return Friction factor of the player. */
	protected abstract float requestedMovementFrictionFactor();

	/**
	 * @return High acceleration of the player, when the speed trigger button is
	 *         pressed.
	 */
	protected abstract float requestedMovementAccelerationFactorHigh();

	/** Shape of the circle/ellipsis of the collected item floating around the player. */
	protected abstract ConeDistributionParameters requestedItemCircleParamters();
	
	/** Maximum number of items this player can hold at once. */
	protected abstract int requestedMaximumHoldableItems();
	
	/** @return Low acceleration of the player. */
	protected abstract float requestedMovementAccelerationFactorLow();

	/** @return Lower battle speed while dodging bullets. */
	protected abstract float requestedMovementBattleSpeedLow();

	/** @return Higher battle speed while dodging bullets. */
	protected abstract float requestedMovementBattleSpeedHigh();

	/**
	 * @return The point to be considered the sprite's origin, used for drawing.
	 *         When the player's position is (0,0), this pixel will be at the
	 *         world position (0,0).
	 */
	protected abstract Vector2 requestedSpriteOrigin();

	protected abstract Vector2 requestedOffsetPlayerToHitCircle();

	/** @return Radius of the sphere bounding the player. */
	protected abstract Circle requestedBoundingCircle();

	/**
	 * @return Bounding box for map related checking, ie. checking for blocking
	 *         tiles.
	 */
	protected abstract Rectangle requestedBoundingBoxMap();

	/** @return Bounding box for collision checking (bullets, hitboxes). */
	protected abstract Rectangle requestedBoundingBoxCollision();

	/**
	 * @return Bounding box for render-related checking, ie. whether to draw the
	 *         entity.
	 */
	protected abstract Rectangle requestedBoundingBoxRender();

	/** @return Player maximum pain points (pp). */
	protected abstract int requestedMaxPainPoints();

	/**
	 * @return Texture used to display the hit circle / point. Placed at the
	 *         center of the player's hit box.
	 */
	protected abstract ETexture requestedHitCircleTexture();

	/** @return The stigma appearing below the playing when in battle mode. */
	protected abstract ETexture requestedBattleStigmaTexture();

	/** @return Color of the battle stigma when hit. It blinks several times. */
	protected abstract Color requestedBattleStigmaColorWhenHit();

	/** @return The speed at which the battle stigma will rotate. */
	protected abstract float requestedBattleStigmaAngularVelocity();

	/**
	 * @return Player bullet attack power. Damage is multiplied by this factor.
	 */
	protected abstract float requestedBulletAttack();

	/** @return Player bullet attack power. Damage is divided by this factor. */
	protected abstract float requestedBulletResistance();

	/** @return List of weapons the player can equip. May be null. */
	protected abstract EWeapon[] requestedSupportedWeapons();

	/** @return List of tokugi the player can learn. May be null. */
	protected abstract ETokugi[] requestedSupportedTokugi();

	/** @return List of consumable the player can use. May be null. */
	protected abstract EConsumable[] requestedSupportedConsumable();

	/** @return Voice played when battle mode starts. */
	protected abstract ESound requestedVoiceOnBattleModeStart();

	/** @return Voice played when battle mode ends. */
	protected abstract ESound requestedVoiceOnBattleModeEnd();

	protected abstract ESound requestedVoiceOnLightHeal();
	
	protected abstract ESound requestedVoiceOnHeavyHeal();
	
	/**
	 * @return Voice played when battle mode ends as a result of the player
	 *         fleeing.
	 */
	protected abstract ESound requestedVoiceOnBattleModeFlee();

	/** @return Voice played when player kills an enemy. */
	protected abstract ESound requestedVoiceOnEnemyKilled();

	/** @return Voice played when player uses a consumable (item). */
	protected abstract ESound requestedVoiceOnConsumableUse();
	
	/**
	 * @see DamageSystem#THRESHOLD_LIGHT_HEAVY_DAMAGE
	 * @return Voice played when taking heavy damage.
	 */
	protected abstract ESound requestedVoiceOnHeavyDamage();

	/**
	 * @see DamageSystem#THRESHOLD_LIGHT_HEAVY_DAMAGE
	 * @return Voice played when taking light damage.
	 */
	protected abstract ESound requestedVoiceOnLightDamage();

	/** @return Voice played when dying. */
	protected abstract ESound requestedVoiceOnDeath();

	/**
	 * Can be overridden for custom effects.
	 * 
	 * @return The particle effect played when the player dies.
	 */
	protected EParticleEffect requestedParticleEffectOnDeath() {
		return EParticleEffect.DEFAULT_PLAYER_DEATH;
	}

	/**
	 * Must return a list of all resources that the level requires. They will
	 * then be loaded into RAM before the level is started.
	 * 
	 * @return List of all required resources.
	 */
	protected abstract IResource<? extends Enum<?>, ?>[] requestedRequiredResources();

	/**
	 * Can be overridden for custom effects.
	 * 
	 * @return The particle effect over the player when battle mode activates.
	 */
	protected EParticleEffect requestedBattleModeEnterParticleEffect() {
		return EParticleEffect.PLAYER_BATTLE_MODE_ENTER_BURST;
	}

	/**
	 * Can be overridden for custom effects.
	 * 
	 * @return The particle effect over the player when battle mode deactivates.
	 */
	protected EParticleEffect requestedBattleModeExitParticleEffect() {
		return EParticleEffect.PLAYER_BATTLE_MODE_EXIT_BURST;
	}

	// ====================================
	// Implementations
	// ====================================

	public boolean loadToRam() {
		if (!ResourceCache.loadToRam(requiredResources))
			return false;
		// We've always got at least nothing.
		if (currentWeapon == null)
			currentWeapon = EWeapon.NONE.getWeapon();
		if (currentTokugi == null)
			currentTokugi = ETokugi.NONE.getTokugi();
		obtainWeapon(currentWeapon);
		learnTokugi(currentTokugi);
		return true;
	}

	public abstract void setupShadow(ShadowComponent kc);

	public EAnimationList getNormalAnimationList() {
		return normalAnimationList;
	}

	public EAnimationList getDamageAnimationList() {
		return damageAnimationList;
	}

	public EAnimationList getBattleAnimationList() {
		return battleAnimationList;
	}

	public Vector2 getSpriteOrigin() {
		return spriteOrigin;
	}

	public Circle getBoundingCircle() {
		return boundingCircle;
	}

	public Rectangle getBoundingBoxCollision() {
		return boundingBoxCollision;
	}

	public Vector2 getOffsetPlayerToHitCircle() {
		return offsetPlayerToHitCircle;
	}

	public Rectangle getBoundingBoxRender() {
		return boundingBoxRender;
	}

	public Rectangle getBoundingBoxMap() {
		return boundingBoxMap;
	}

	public Shape2D getExactShapeCollision() {
		return exactShapeCollision;
	}

	public ETexture getHitCircleTexture() {
		return hitCircleTexture;
	}

	public ETexture getBattleStigmaTexture() {
		return battleStigmaTexture;
	}

	public Color getBattleStigmaColorWhenHit() {
		return battleStigmaColorWhenHit;
	}

	public ConeDistributionParameters getItemCircleParameters() {
		return itemCircleParameters;
	}
	
	public float getBattleStigmaAngularVelocity() {
		return battleStigmaAngularVelocity;
	}

	public EParticleEffect getBattleModeEnterParticleEffect() {
		return battleModeEnterParticleEffect;
	}

	public EParticleEffect getBattleModeExitParticleEffect() {
		return battleModeExitParticleEffect;
	}

	public long getMaxPainPoints() {
		return maxPainPoints;
	}
	
	public float getBulletAttack() {
		return bulletAttack;
	}

	public float getBulletResistance() {
		return bulletResistance;
	}
	
	/**
	 * Can be overridden for a custom HP bar color.
	 * 
	 * @return The color when the pain bar is low.
	 */
	protected Color requestedPainBarColorLow() {
		return new Color(0.0f, 204.0f / 255.0f, 102.0f / 255.0f, 1.0f);
	}

	/**
	 * Can be overridden for a custom HP bar color.
	 * 
	 * @return The color when the pain bar is halfway to full.
	 */
	protected Color requestedPainBarColorMid() {
		return new Color(255.0f, 153.0f / 255.0f, 51.0f / 255.0f, 1.0f);
	}

	/**
	 * Can be overridden for a custom HP bar color.
	 * 
	 * @return The color when the pain bar is high.
	 */
	protected Color requestedPainBarColorHigh() {
		return new Color(255.0f, 80.0f / 255.0f, 80.0f / 255.0f, 1.0f);
	}

	protected abstract Shape2D requestedExactShapeCollision();

	public Color getPainBarColorLow() {
		return painBarColorLow;
	}

	public Color getPainBarColorMid() {
		return painBarColorMid;
	}

	public Color getPainBarColorHigh() {
		return painBarColorHigh;
	}

	public float getMovementFrictionFactor() {
		return movementFrictionFactor;
	}

	public float getMovementAccelerationFactorLow() {
		return movementAccelerationFactorLow;
	}

	public float getMovementAccelerationFactorHigh() {
		return movementAccelerationFactorHigh;
	}

	public float getMovementBattleSpeedLow() {
		return movementBattleSpeedLow;
	}

	public float getMovementBattleSpeedHigh() {
		return movementBattleSpeedHigh;
	}

	public boolean supportsWeapon(EWeapon weapon) {
		return supportedWeaponSet.contains(weapon);
	}

	public boolean supportsTokugi(ETokugi tokugi) {
		return supportedTokugiSet.contains(tokugi);
	}

	public boolean supportsConsumable(EConsumable consumable) {
		return supportedConsumableSet.contains(consumable);
	}

	public void obtainWeapon(AWeapon weapon) {
		if (supportsWeapon(weapon.getType()) && !ownsWeapon(weapon)) {
			obtainedWeaponList.add(weapon);
			if (currentWeapon != null && currentWeapon.getType() == EWeapon.NONE)
				equipWeapon(weapon);
		}
	}

	public boolean ownsWeapon(AWeapon weapon) {
		return obtainedWeaponList.contains(weapon);
	}

	public void loseWeapon(AWeapon weapon) {
		obtainedWeaponList.remove(weapon);
	}

	public void equipWeapon(AWeapon weapon) {
		if (supportsWeapon(weapon.getType()) && ownsWeapon(weapon)) {
			currentWeapon = weapon;
			if (statusScreen != null) statusScreen.updateWeaponAndTokugiLayout();
		}
	}

	public AWeapon getEquippedWeapon() {
		return currentWeapon;
	}

	public boolean cycleWeapon(int amount) {
		int index = obtainedWeaponList.indexOf(currentWeapon);
		if (index >= 0 && obtainedWeaponList.size() > 1) {
			equipWeapon(obtainedWeaponList.get(Math.floorMod(index + amount, obtainedWeaponList.size())));
			// Do not switch to WeaponNone when the player has got more than one
			// weapon.
			if ((currentWeapon instanceof WeaponNone) && obtainedWeaponList.size() > 2) {
				equipWeapon(obtainedWeaponList.get(Math.floorMod(index + amount + 1, obtainedWeaponList.size())));
			}
			return true;
		}
		return false;
	}

	public boolean cycleWeaponForward() {
		return cycleWeapon(1);
	}

	public boolean cycleWeaponBackward() {
		return cycleWeapon(-1);
	}

	public void learnTokugi(ATokugi tokugi) {
		if (supportsTokugi(tokugi.getType()) && !knowsTokugi(tokugi)) {
			obtainedTokugiList.add(tokugi);
			if (currentTokugi != null && currentTokugi.getType() == ETokugi.NONE)
				equipTokugi(tokugi);
		}
	}

	public boolean knowsTokugi(ATokugi tokugi) {
		return obtainedTokugiList.contains(tokugi);
	}

	public void forgetTokugi(ATokugi tokugi) {
		obtainedTokugiList.remove(tokugi);
	}

	public void equipTokugi(ATokugi tokugi) {
		if (supportsTokugi(tokugi.getType()) && knowsTokugi(tokugi)) {
			currentTokugi = tokugi;
			if (statusScreen != null) statusScreen.updateWeaponAndTokugiLayout();
		}
	}

	public ATokugi getEquippedTokugi() {
		return currentTokugi;
	}

	public boolean cycleTokugi(int amount) {
		int index = obtainedTokugiList.indexOf(currentTokugi);
		if (index >= 0 && obtainedTokugiList.size() > 1) {
			equipTokugi(obtainedTokugiList.get(Math.floorMod(index + amount, obtainedTokugiList.size())));
			if ((currentTokugi instanceof TokugiNone) && obtainedTokugiList.size() > 2) {
				equipTokugi(obtainedTokugiList.get(Math.floorMod(index + amount + 1, obtainedTokugiList.size())));
			}
			return true;
		}
		return false;
	}

	public boolean cycleTokugiForward() {
		return cycleTokugi(1);
	}

	public boolean cycleTokugiBackward() {
		return cycleTokugi(-1);
	}

	public ESound getVoiceOnBattleModeStart() {
		return voiceOnBattleModeStart;
	}

	public ESound getVoiceOnBattleModeEnd() {
		return voiceOnBattleModeEnd;
	}

	public ESound getVoiceOnBattleModeFlee() {
		return voiceOnBattleModeFlee;
	}

	public ESound getVoiceOnEnemyKilled() {
		return voiceOnEnemyKilled;
	}
	
	public ESound getVoiceOnConsumableUse() {
		return voiceOnConsumableUse;
	}

	public ESound getVoiceOnLightDamage() {
		return voiceOnLightDamage;
	}

	public ESound getVoiceOnHeavyDamage() {
		return voiceOnHeavyDamage;
	}
	
	public ESound getVoiceOnLightHeal() {
		return voiceOnLightHeal;
	}
	
	public ESound getVoiceOnHeavyHeal() {
		return voiceOnHeavyHeal;
	}


	public ESound getVoiceOnDeath() {
		return voiceOnDeath;
	}

	public EParticleEffect getParticleEffectOnDeath() {
		return particleEffectOnDeath;
	}

	public EAnimationList getDeathAnimationList() {
		return deathAnimationList;
	}
	
	public int getMaximumHoldableItems() {
		return maximumHoldableItems;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof APlayer))
			return false;
		final APlayer you = (APlayer) object;
		return you.getClass().equals(getClass());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}