package de.homelab.madgaksha.player;

import java.util.EnumMap;
import java.util.EnumSet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.player.tokugi.ATokugi;
import de.homelab.madgaksha.player.weapon.AWeapon;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.util.MoreMathUtils;

public abstract class APlayer implements IPainBar{

	private final EAnimationList animationList;
	private final float movementAccelerationFactorLow;
	private final float movementAccelerationFactorHigh;
	private final float movementFrictionFactor;
	private final Color painBarColorLow = new Color();
	private final Color painBarColorMid = new Color();
	private final Color painBarColorHigh = new Color();
	private final int[] painPointsDigits = new int[10];
	
	/** x,y are the offset relative to the center of the sprites (bottom left). Radius is the circe's size. */
	final private Circle boundingCircle;
	final private Rectangle boundingBox;
	/** x,y are the offset relative to the center of the sprites (bottom left). Width and height are the dimensions. */ 
	final private IResource<? extends Enum<?>,?>[] requiredResources;
	
	/** Player current pain points. */
	private long painPoints;
	/** Player maximum paint points. */
	private long maxPainPoints;
	private float painPointsRatio;
	
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
		this.animationList = requestedAnimationList();
		this.boundingCircle = requestedBoundingCircle();
		this.boundingBox = requestedBoundingBox();
		this.painBarColorLow.set(requestedPainBarColorLow());
		this.painBarColorMid.set(requestedPainBarColorMid());
		this.painBarColorHigh.set(requestedPainBarColorHigh());
		
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
		this.maxPainPoints = requestedMaxPainPoints();
		
		if (this.maxPainPoints >= MoreMathUtils.pow(10L, painPointsDigits.length))
			this.maxPainPoints = MoreMathUtils.pow(10L, painPointsDigits.length) - 1L; 
		this.untakeDamage(this.maxPainPoints);		
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

	/** @return Radius of the sphere bounding the player. */
	protected abstract Circle requestedBoundingCircle();
	
	/** @return Radius of the sphere bounding the player. */
	protected abstract Rectangle requestedBoundingBox();

	/** @return Player maximum pain points (pp). */
	protected abstract int requestedMaxPainPoints();
	
	/** @return List of weapons the player can equip. May be null.*/
	protected abstract EWeapon[] requestedSupportedWeapons();
	/** @return List of tokugi the player can learn. May be null.*/ 
	protected abstract ETokugi[] requestedSupportedTokugi();
	
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

	public Circle getBoundingCircle() {
		return boundingCircle;
	}
	public Rectangle getBoundingBox() {
		return boundingBox;
	}
	
	public float getBoundingBoxCenterX() {
		return boundingBox.x + 0.5f*boundingBox.width;
	}
	
	public float getBoundingBoxCenterY() {
		return boundingBox.y + 0.5f*boundingBox.height;
	}
	
	@Override
	public long getPainPoints() {
		return painPoints;
	}
	
	@Override
	public long getMaxPainPoints() {
		return maxPainPoints;
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
	
	
	
	/**
	 * @param damage Amount of damage to take.
	 * @return Whether the player is now dead :(
	 */
	@Override
	public boolean takeDamage(long damage) {
		painPoints += damage;
		updatePainPoints();
		return isDead();
	}
	/**
	 * @param health 
	 * @return Whether the player is now completely undamaged :)
	 */
	@Override
	public boolean untakeDamage(long health) {
		painPoints -= health;
		updatePainPoints();
		return isUndamaged();
	}
	@Override
	public boolean takeDamage(int damage) {
		return takeDamage((long)damage);
	}
	@Override
	public boolean untakeDamage(int health) {
		return untakeDamage((long)health);
	}
	/** @return Whether the player is dead :( */
	@Override
	public boolean isDead() {
		return painPoints >= maxPainPoints;
	}
	/** @return Whether the player is completely healed :) */
	public boolean isUndamaged() {
		return painPoints <= 0;
	}
	/** @return The ratio currentPainPoints / maximumPaintPoints. */
	@Override
	public float getPainPointsRatio() {
		return painPointsRatio;
	}

	public int getPainPointsDigit(int i) {
		return painPointsDigits[i];
	}	

	private void updatePainPoints() {
		if (painPoints < 0) painPoints = 0;
		if (painPoints > maxPainPoints) painPoints = maxPainPoints;

		painPointsRatio = ((float)painPoints) / ((float)maxPainPoints);
		
		long digit = painPoints;
		for (int i = painPointsDigits.length; i --> 0;) {
			painPointsDigits[i] = (int)(digit % 10L);
			digit /= 10;
		}
	}

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
	
}
