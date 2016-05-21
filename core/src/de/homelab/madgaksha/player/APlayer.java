package de.homelab.madgaksha.player;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.entityengine.component.ShadowComponent;
import de.homelab.madgaksha.resourcecache.EAnimationList;
import de.homelab.madgaksha.resourcecache.IResource;

public abstract class APlayer {

	final private EAnimationList animationList;
	final private float movementSpeed;
	/** x,y are the offset relative to the center of the sprites (bottom left). Radius is the circe's size. */
	final private Circle boundingCircle;
	final private Rectangle boundingBox;
	/** x,y are the offset relative to the center of the sprites (bottom left). Width and height are the dimensions. */ 
	final private IResource[] requiredResources;
	
	/** Player current pain points. */
	private int painPoints;
	/** Player maximum paint points. */
	private int maxPainPoints;
	
	public APlayer() {
		this.movementSpeed = requestedMovementSpeed();
		this.animationList = requestedAnimationList();
		this.boundingCircle = requestedBoundingCircle();
		this.boundingBox = requestedBoundingBox();
		this.requiredResources = requestedRequiredResources();
		this.maxPainPoints = requestedMaxPainPoints();
		this.painPoints = this.maxPainPoints;
	}

	// ====================================
	//          Abstract methods
	// ====================================
	/** @return The animated sprite used for the player. */
	protected abstract EAnimationList requestedAnimationList();

	/** @return Speed of the character. */
	protected abstract float requestedMovementSpeed();
	
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
	protected abstract IResource[] requestedRequiredResources();
	
	// ====================================
	//          Implementations
	// ====================================
	
	public ShadowComponent makeShadow() {
		return null; // no shadow
	}
	
	public EAnimationList getAnimationList() {
		return animationList;
	}
	public float getMovementSpeed() {
		return movementSpeed;
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
	
	public IResource[] getRequiredResources() {
		return requiredResources;
	}
	
	public int getPainPoints() {
		return painPoints;
	}
	
	public int getMaxPainPoints() {
		return maxPainPoints;
	}
	
	/**
	 * @param damage Amount of damage to take.
	 * @return Whether the player is now dead :(
	 */
	public boolean takeDamage(int damage) {
		painPoints += damage;
		if (painPoints > maxPainPoints) painPoints = maxPainPoints;
		return isDead();
	}
	/**
	 * @param health 
	 * @return Whether the player is now completely undamaged :)
	 */
	public boolean untakeDamage(int health) {
		painPoints -= health;
		if (painPoints < 0) painPoints = 0;
		return isUndamaged();
	}
	/** @return Whether the player is dead :( */
	public boolean isDead() {
		return painPoints >= maxPainPoints;
	}
	/** @return Whether the player is completely healed :) */
	public boolean isUndamaged() {
		return painPoints <= 0;
	}
}
