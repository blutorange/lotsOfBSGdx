package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.statusScreen;
import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.player.IPainBar;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.util.GeoUtil;
import de.homelab.madgaksha.util.MoreMathUtils;

public abstract class EntityEnemy extends AEntity implements IBehaviour, ITrigger, IReceive, IPainBar {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EntityEnemy.class);

	private PositionComponent positionComponent;
	private BoundingSphereComponent boundingSphereComponent;
	private BoundingBoxComponent boundingBoxComponent = new BoundingBoxComponent();
	private TemporalComponent temporalComponent = new TemporalComponent();
	private Class<Component> triggerComponentClass;
	
	private final Sprite iconMain;
	private final Sprite iconSubHorizontal;
	private final Sprite iconSubVertical;

	private long painPoints;
	private long maxPainPoints;
	private float painPointsRatio;

	
	@SuppressWarnings("unchecked")
	public EntityEnemy(Shape2D shape, ETrigger trigger, Vector2 initialPosition, Float initDir) {
		super();

		Component tc = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);
		PositionComponent pc = MakerUtils.makePositionAtCenter(shape);
		triggerComponentClass = (Class<Component>) tc.getClass();

		boundingBoxComponent = new BoundingBoxComponent(requestedBoundingBox());
		boundingSphereComponent = new BoundingSphereComponent(requestedBoundingCircle());
		positionComponent = new PositionComponent(initialPosition.x + pc.x, initialPosition.y + pc.y);

		// Initially the bounding box is the area the player needs to
		// touch to spawn the enemy. When
		// Bounding box needs to be relative to the enemy's origin.
		Rectangle r = GeoUtil.getBoundingBox(shape);
		r.x -= pc.x;
		r.y -= pc.y;

		add(new CameraFocusComponent());
		add(new DirectionComponent(initDir));
		add(new InactiveComponent());
		add(new InvisibleComponent());
		add(new BoundingBoxComponent(r));
		add(pc);
		if (tc != null)
			add(tc);
		
		iconMain = requestedIconMain().asSprite();
		iconSubHorizontal = requestedIconSubHorizontal().asSprite();
		iconSubVertical = requestedIconSubVertical().asSprite();

		maxPainPoints = requestedMaxPainPoints();
		untakeDamage(maxPainPoints);

	}
	
	@Override
	public void reinitializeEntity() {
	}

	@Override
	public void behave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void callbackStartup() {
		sinSpawn();
		triggeredStartup();
	}

	@Override
	public void callbackTouch(Entity e) {
		sinSpawn();
		triggeredTouch(e);
	}

	@Override
	public void callbackScreen() {
		sinSpawn();
		triggeredScreen();
	}

	@Override
	public void callbackTouched(Entity e) {
		sinSpawn();
		triggeredTouched(e);
	}

	private void sinSpawn() {
		// Enemy spawning, add real bounding box.
		remove(BoundingBoxComponent.class);
		remove(InactiveComponent.class);
		remove(InvisibleComponent.class);
		remove(PositionComponent.class);
		remove(triggerComponentClass);
		add(positionComponent);
		add(boundingBoxComponent);
		add(boundingSphereComponent);
		add(temporalComponent);
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size())
			statusScreen.targetEnemy(this);
		else 
			statusScreen.targetEnemy(cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex));
	}

	protected abstract ETexture requestedIconMain();
	protected abstract ETexture requestedIconSubHorizontal();
	protected abstract ETexture requestedIconSubVertical();
	
	protected abstract IResource<? extends Enum<?>, ?>[] requestedResources();

	protected abstract Rectangle requestedBoundingBox();

	protected abstract Circle requestedBoundingCircle();

	/** @return Enemy maximum pain points (pp). */
	protected abstract int requestedMaxPainPoints();
	
	protected abstract void triggeredStartup();

	protected abstract void triggeredTouch(Entity e);

	protected abstract void triggeredScreen();

	protected abstract void triggeredTouched(Entity e);
	
	
	public Sprite getIconMain() {
		return iconMain;
	}
	public Sprite getIconSubHorizontal() {
		return iconSubHorizontal;
	}
	public Sprite getIconSubVertical() {
		return iconSubVertical;
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
	/** @return Whether the enemy is dead :) */
	@Override
	public boolean isDead() {
		return painPoints >= maxPainPoints;
	}
	/** @return Whether the enemy is completely healed :( */
	@Override
	public boolean isUndamaged() {
		return painPoints <= 0;
	}
	/** @return The ratio currentPainPoints / maximumPaintPoints. */
	@Override
	public float getPainPointsRatio() {
		return painPointsRatio;
	}
	
	@Override
	public long getPainPoints() {
		return painPoints;
	}
	
	@Override
	public long getMaxPainPoints() {
		return maxPainPoints;
	}

	private void updatePainPoints() {
		if (painPoints < 0) painPoints = 0;
		if (painPoints > maxPainPoints) painPoints = maxPainPoints;

		painPointsRatio = ((float)painPoints) / ((float)maxPainPoints);
	}
}
