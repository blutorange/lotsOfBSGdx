package de.homelab.madgaksha.player;

import java.util.EnumMap;
import java.util.EnumSet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.entityengine.entitysystem.DamageSystem;
import de.homelab.madgaksha.player.tokugi.ATokugi;
import de.homelab.madgaksha.player.weapon.AWeapon;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public abstract class APlayer {

	private final EAnimationList animationList;
	private final float movementAccelerationFactorLow;
	private final float movementAccelerationFactorHigh;
	private final float movementFrictionFactor;
	private final float movementBattleSpeedLow;
	private final float movementBattleSpeedHigh;
	private final Color painBarColorLow = new Color();
	private final Color painBarColorMid = new Color();
	private final Color painBarColorHigh = new Color();
		
	/** x,y are the offset relative to the center of the sprites (bottom left). Radius is the circe's size. */
	final private Shape2D exactShapeCollision;
	final private Circle boundingCircle;
	final private Rectangle boundingBoxMap;
	final private Rectangle boundingBoxRender;
	final private Rectangle boundingBoxCollision;
	final private Vector2 spriteOrigin;
	/** x,y are the offset relative to the center of the sprites (bottom left). Width and height are the dimensions. */ 
	final private IResource<? extends Enum<?>,?>[] requiredResources;
	
	/** Player maximum paint points. */
	private final long maxPainPoints;
	/** Bullet attack power. Default 1.0 */
	private final float bulletAttack;
	/** Bullet resistance. Default 1.0 */
	private final float bulletResistance;
	
	private final float battleStigmaAngularVelocity;
	
	/** The texture used as the player's hit circle / point. 
	 * It is placed at the center of the player's hit box.
	 * @see #requestedBoundingBoxCollision() 
	 */
	private final ETexture hitCircleTexture;
	private final ETexture battleStigmaTexture;
	
	private final Color battleStigmaColorWhenHit;
	
	private final EMusic voiceOnBattleStart;
	private final EMusic voiceOnHeavyDamage;
	private final EMusic voiceOnLightDamage;
	private final EMusic voiceOnDeath;
	
	private final EnumSet<EWeapon> supportedWeaponSet = EnumSet.of(EWeapon.NONE); 
	private final EnumMap<EWeapon,AWeapon> supportedWeaponMap = new EnumMap<EWeapon,AWeapon>(EWeapon.class);
	
	private final EnumSet<ETokugi> supportedTokugiSet = EnumSet.of(ETokugi.NONE); 
	private final EnumMap<ETokugi,ATokugi> supportedTokugiMap = new EnumMap<ETokugi,ATokugi>(ETokugi.class);
	
	/** Current weapon equipped. */
	private AWeapon currentWeapon = null;
	
	/** The current tokugi. */
	private ATokugi currentTokugi = null;
	
	public APlayer() {
		this.movementAccelerationFactorLow = requestedMovementAccelerationFactorLow();
		this.movementAccelerationFactorHigh = requestedMovementAccelerationFactorHigh();
		this.movementFrictionFactor = requestedMovementFrictionFactor();
		this.movementBattleSpeedLow = requestedMovementBattleSpeedLow();
		this.movementBattleSpeedHigh = requestedMovementBattleSpeedHigh();
		this.animationList = requestedAnimationList();
		this.spriteOrigin = requestedSpriteOrigin();
		this.boundingCircle = requestedBoundingCircle();
		this.boundingBoxMap = requestedBoundingBoxMap();
		this.boundingBoxRender = requestedBoundingBoxRender();
		this.boundingBoxCollision = requestedBoundingBoxCollision();
		this.exactShapeCollision = requestedExactShapeCollision();
		this.painBarColorLow.set(requestedPainBarColorLow());
		this.painBarColorMid.set(requestedPainBarColorMid());
		this.painBarColorHigh.set(requestedPainBarColorHigh());
		this.maxPainPoints = MathUtils.clamp(requestedMaxPainPoints(),1L, DamageSystem.MAX_PAIN_POINTS);
		this.bulletAttack = requestedBulletAttack();
		this.bulletResistance = requestedBulletResistance();
		this.hitCircleTexture = requestedHitCircleTexture();
		this.battleStigmaTexture = requestedBattleStigmaTexture();
		this.battleStigmaAngularVelocity = requestedBattleStigmaAngularVelocity();
		this.voiceOnBattleStart = requestedVoiceOnBattleStart();
		this.voiceOnLightDamage = requestedVoiceOnLightDamage();
		this.voiceOnHeavyDamage = requestedVoiceOnHeavyDamage();
		this.voiceOnDeath = requestedVoiceOnDeath();
		this.battleStigmaColorWhenHit = requestedBattleStigmaColorWhenHit();
		
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
		
		this.requiredResources = requestedRequiredResources();
	}


	


	// ====================================
	//          Abstract methods
	// ====================================
	/** @return The animated sprite used for the player. */
	protected abstract EAnimationList requestedAnimationList();


	/** @return Friction factor of the player. */
	protected abstract float requestedMovementFrictionFactor();

	/** @return High acceleration of the player, when the speed trigger button is pressed. */
	protected abstract float requestedMovementAccelerationFactorHigh();

	/** @return Low acceleration of the player. */
	protected abstract float requestedMovementAccelerationFactorLow();	

	/** @return Lower battle speed while dodging bullets. */
	protected abstract float requestedMovementBattleSpeedLow();
	/** @return Higher battle speed while dodging bullets. */
	protected abstract float requestedMovementBattleSpeedHigh();
	
	/** @return The point to be considered the sprite's origin, used for drawing. When the player's position is (0,0), this pixel will be at the world position (0,0).*/
	protected abstract Vector2 requestedSpriteOrigin();
	
	/** @return Radius of the sphere bounding the player. */
	protected abstract Circle requestedBoundingCircle();
	
	/** @return Bounding box for map related checking, ie. checking for blocking tiles. */
	protected abstract Rectangle requestedBoundingBoxMap();
	/** @return Bounding box for collision checking (bullets, hitboxes). */
	protected abstract Rectangle requestedBoundingBoxCollision();
	/** @return Bounding box for render-related checking, ie. whether to draw the entity. */
	protected abstract Rectangle requestedBoundingBoxRender();

	/** @return Player maximum pain points (pp). */
	protected abstract int requestedMaxPainPoints();

	/** @return Texture used to display the hit circle / point. Placed at the center of the player's hit box. */
	protected abstract ETexture requestedHitCircleTexture();
	
	/** @return The stigma appearing below the playing when in battle mode. */
	protected abstract ETexture requestedBattleStigmaTexture();
	
	/** @return Color of the battle stigma when hit. It blinks several times. */
	protected abstract Color requestedBattleStigmaColorWhenHit();
	
	/** @return The speed at which the battle stigma will rotate. */
	protected abstract float requestedBattleStigmaAngularVelocity();
	
	/** @return Player bullet attack power. Damage is multiplied by this factor. */
	protected abstract float requestedBulletAttack();
	/** @return Player bullet attack power. Damage is divided by this factor. */
	protected abstract float requestedBulletResistance();

	
	/** @return List of weapons the player can equip. May be null.*/
	protected abstract EWeapon[] requestedSupportedWeapons();
	/** @return List of tokugi the player can learn. May be null.*/ 
	protected abstract ETokugi[] requestedSupportedTokugi();
	
	/** @return Voice played when battle starts. */
	protected abstract EMusic requestedVoiceOnBattleStart() ;
	/**
	 * @see DamageSystem#THRESHOLD_LIGHT_HEAVY_DAMAGE 
	 * @return Voice played when taking heavy damage.
	 */
	protected abstract EMusic requestedVoiceOnHeavyDamage();
	/**
	 * @see DamageSystem#THRESHOLD_LIGHT_HEAVY_DAMAGE
	 * @return Voice played when taking light damage.
	 */
	protected abstract EMusic requestedVoiceOnLightDamage() ;
	/** @return Voice played when dying. */
	protected abstract EMusic requestedVoiceOnDeath();
	
	/**
	 * Must return a list of all resources that the level requires. They will
	 * then be loaded into RAM before the level is started.
	 * 
	 * @return List of all required resources.
	 */
	protected abstract IResource<? extends Enum<?>,?>[] requestedRequiredResources();
	
	// ====================================
	//          Implementations
	// ====================================
	
	public boolean loadToRam() {
		if (!ResourceCache.loadToRam(requiredResources)) return false;
		for (EWeapon ew : supportedWeaponSet) {
			AWeapon aw = ew.getWeapon();
			if (aw == null) return false;
			supportedWeaponMap.put(ew,aw);
		}
		for (ETokugi et : supportedTokugiSet) {
			ATokugi at = et.getTokugi();
			if (at == null) return false;
			supportedTokugiMap.put(et,at);
		}
		
		if (currentWeapon == null) currentWeapon = supportedWeaponMap.get(EWeapon.NONE);
		if (currentTokugi == null) currentTokugi = supportedTokugiMap.get(ETokugi.NONE);
		
		return true;
	}
	
	public ShadowComponent makeShadow() {
		return null; // no shadow
	}
	
	public EAnimationList getAnimationList() {
		return animationList;
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
	public float getBattleStigmaAngularVelocity() {
		return battleStigmaAngularVelocity;
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
	
	/** Can be overridden for a custom HP bar color.
	 * 
	 * @return The color when the pain bar is low.
	 */
	protected Color requestedPainBarColorLow() {
		return new Color(0.0f, 204.0f/255.0f, 102.0f/255.0f, 1.0f);
	}
	/** Can be overridden for a custom HP bar color.
	 * 
	 * @return The color when the pain bar is halfway to full.
	 */
	protected Color requestedPainBarColorMid() {
		return new Color(255.0f, 153.0f/255.0f, 51.0f/255.0f, 1.0f);
	}
	/** Can be overridden for a custom HP bar color.
	 * 
	 * @return The color when the pain bar is high.
	 */
	protected Color requestedPainBarColorHigh() {
		return new Color(255.0f, 80.0f/255.0f, 80.0f/255.0f, 1.0f);
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

	public void switchWeapon(EWeapon weapon) {
		if (supportedWeaponSet.contains(weapon))
			currentWeapon = supportedWeaponMap.get(weapon);
	}
	public AWeapon getWeapon() {
		return currentWeapon;
	}

	public void switchTokugi(ETokugi tokugi) {
		if (supportedTokugiSet.contains(tokugi))
			currentTokugi = supportedTokugiMap.get(tokugi);
	}
	public ATokugi getTokugi() {
		return currentTokugi;
	}

	public EMusic getVoiceOnBattleStart() {
		return voiceOnBattleStart;
	}
	public EMusic getVoiceOnLightDamage() {
		return voiceOnLightDamage;
	}
	public EMusic getVoiceOnHeavyDamage() {
		return voiceOnHeavyDamage;
	}
	public EMusic getVoiceOnDeath() {
		return voiceOnDeath;
	}

}