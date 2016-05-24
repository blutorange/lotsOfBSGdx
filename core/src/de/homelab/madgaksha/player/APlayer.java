package de.homelab.madgaksha.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.util.MoreMathUtils;

public abstract class APlayer {

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
	
	public IResource<? extends Enum<?>,?>[] getRequiredResources() {
		return requiredResources;
	}
	
	public long getPainPoints() {
		return painPoints;
	}
	
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
	public boolean takeDamage(long damage) {
		painPoints += damage;
		updatePainPoints();
		return isDead();
	}
	/**
	 * @param health 
	 * @return Whether the player is now completely undamaged :)
	 */
	public boolean untakeDamage(long health) {
		painPoints -= health;
		updatePainPoints();
		return isUndamaged();
	}
	public boolean takeDamage(int damage) {
		return takeDamage((long)damage);
	}
	public boolean untakeDamage(int health) {
		return untakeDamage((long)health);
	}
	/** @return Whether the player is dead :( */
	public boolean isDead() {
		return painPoints >= maxPainPoints;
	}
	/** @return Whether the player is completely healed :) */
	public boolean isUndamaged() {
		return painPoints <= 0;
	}
	/** @return The ratio currentPainPoints / maximumPaintPoints. */
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
	
	public float getMovementFrictionFactor() {
		return movementFrictionFactor;
	}

	public float getMovementAccelerationFactorLow() {
		return movementAccelerationFactorLow;
	}
	public float getMovementAccelerationFactorHigh() {
		return movementAccelerationFactorHigh;
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

	
}
