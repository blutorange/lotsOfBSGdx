package de.homelab.madgaksha.entityengine.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
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
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.util.GeoUtil;

public abstract class EnemyMaker extends AEntityMaker implements IBehaviour, ITrigger, IReceive {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EnemyMaker.class);

	private PositionComponent positionComponent;
	private BoundingSphereComponent boundingSphereComponent;
	private BoundingBoxComponent boundingBoxComponent = new BoundingBoxComponent();
	private TemporalComponent temporalComponent = new TemporalComponent();
	private Class<Component> triggerComponentClass;

	@SuppressWarnings("unchecked")
	public EnemyMaker(Shape2D shape, ETrigger trigger, Vector2 initialPosition, Float initDir) {
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

		// TODO remove component when triggered to activate enemy ?
		// this.add(deactivatedComponent / inactiveComponent);
		// TODO
		// Or add a PropertiesToBeAddedLaterComponent?
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
	}

	protected abstract IResource<? extends Enum<?>, ?>[] requestedResources();

	protected abstract Rectangle requestedBoundingBox();

	protected abstract Circle requestedBoundingCircle();

	protected abstract void triggeredStartup();

	protected abstract void triggeredTouch(Entity e);

	protected abstract void triggeredScreen();

	protected abstract void triggeredTouched(Entity e);
}
